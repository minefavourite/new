package cn.wolfcode.luowowo.website.controller;

import cn.wolfcode.luowowo.article.domain.StrategyDetail;
import cn.wolfcode.luowowo.article.domain.Travel;
import cn.wolfcode.luowowo.article.domain.TravelCommend;
import cn.wolfcode.luowowo.article.query.TravelQuery;
import cn.wolfcode.luowowo.article.service.IStrategyDetailService;
import cn.wolfcode.luowowo.article.service.ITravelCommendService;
import cn.wolfcode.luowowo.article.service.ITravelService;
import cn.wolfcode.luowowo.member.domain.UserInfo;
import cn.wolfcode.luowowo.member.service.IUserService;
import cn.wolfcode.luowowo.search.query.SearchQueryObject;
import cn.wolfcode.luowowo.search.service.*;
import cn.wolfcode.luowowo.search.template.DestinationTemplate;
import cn.wolfcode.luowowo.search.template.StrategyTemplate;
import cn.wolfcode.luowowo.search.template.TravelTemplate;
import cn.wolfcode.luowowo.search.template.UserInfoTemplate;
import cn.wolfcode.luowowo.search.vo.SearchResultVO;
import cn.wolfcode.luowowo.website.utils.CookieUtil;
import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class IndexController {

    @Reference
    private IUserService userService;

    @Reference
    private ITravelCommendService commendService;

    @Reference
    private ITravelService travelService;

    @Reference
    private IStrategyDetailService strategyDetailService;

    //游记模块
    @Reference
    private ITravelTemplateService travelTemplateService;

    //目的地模块
    @Reference
    private IDestinationTemplateService destinationTemplateService;

    //攻略模块
    @Reference
    private IStrategyTemplateService strategyTemplateService;

    //用户模块
    @Reference
    private IUserInfoTemplateService userInfoTemplateService;

    //高亮显示操作
    @Reference
    private EsSearchUtilTemplateService searchUtilTemplateService;

    //跳转到主页面
    @RequestMapping("")
    public String getIndex(Model model,HttpServletRequest req){
        String key = CookieUtil.getCookie(req);
        UserInfo user = userService.getUserByCookie(key);
        req.getSession().setAttribute("userInfo",user);
        //推荐前五篇
        List<TravelCommend> travelCommends = commendService.queryCommendTop5();
        model.addAttribute("tcs",travelCommends);
        //推荐一篇攻略 scs
        List<StrategyDetail> list = strategyDetailService.list();
        model.addAttribute("scs",list.subList(0,1));
        return "index/index";
    }

    //查询游记
    @RequestMapping("/index/travelPage")
    public String travelPage(Model model, @ModelAttribute("qo") TravelQuery qo){
        qo.setState(Travel.STATE_RELEASE);
        PageInfo<Travel> pageInfo = travelService.queryForList(qo);
        model.addAttribute("pageInfo",pageInfo);
        return "index/travelPageTpl";
    }

    //全文搜索
    @RequestMapping("/q")
    public String query(Model model , @ModelAttribute("qo") SearchQueryObject qo){
        //查询全部
        switch (qo.getType()){
            //查询目的地
            case SearchQueryObject.SEARCH_CONTENT_DESTS:
                return destsSearch(model,qo);
            //查询攻略
            case SearchQueryObject.SEARCH_CONTENT_STRATEGY:
                return strategySearch(model,qo);
            //查询游记
            case SearchQueryObject.SEARCH_CONTENT_TRAVEL:
                return travelSearch(model,qo);
            //查询用户
            case SearchQueryObject.SEARCH_CONTENT_USER:
                return userSearch(model,qo);
                default: return  allSearch(model,qo);
        }

    }

    //查询目的地
    private String destsSearch(Model model,SearchQueryObject qo){
        //查询目的地的内容
        List<DestinationTemplate> dests = destinationTemplateService.findByDestName(qo);
        //查询目的地下相关的攻略
        List<StrategyTemplate> sts = strategyTemplateService.findByDestName(qo);

        //查询相关游记
        List<TravelTemplate> ts=travelTemplateService.findByDestName(qo);

        //查询相关的用户
        List<UserInfoTemplate> us = userInfoTemplateService.findByDestName(qo);
        //将数据全部存放到一个集合中,便于前端获取
        SearchResultVO data=new SearchResultVO();
        data.setDests(dests);
        data.setStrategys(sts);
        data.setTravels(ts);
        data.setUsers(us);
        Integer total = dests.size() + sts.size() + ts.size() + us.size();
        Long value = Long.valueOf(total);
        data.setTotal(value);
        model.addAttribute("data",data);
        return "index/searchDest";
    }


    //查询游记
    private String travelSearch(Model model,SearchQueryObject qo){
        Page<TravelTemplate> travel = searchUtilTemplateService.queryWithHighLight(TravelTemplate.INDEX_NAME, TravelTemplate.TYPE_NAME,
                TravelTemplate.class, qo, "title", "summary");
        model.addAttribute("page",travel);
        return "index/searchTravel";
    }

    //查询攻略
    private String strategySearch(Model model,SearchQueryObject qo){
        Page<StrategyTemplate> strategy = searchUtilTemplateService.queryWithHighLight(StrategyTemplate.INDEX_NAME, StrategyTemplate.TYPE_NAME,
                StrategyTemplate.class, qo, "title", "subTitle","summary");
        model.addAttribute("page",strategy);
        return "index/searchStrategy";
    }

    //查询用户
    private String userSearch(Model model,SearchQueryObject qo){
        //传递四个参数,一个索引,一个是类型,一个是条件,返回的对象类型
        Page<UserInfoTemplate> userPage = searchUtilTemplateService.queryWithHighLight(UserInfoTemplate.INDEX_NAME, UserInfoTemplate.TYPE_NAME,
                UserInfoTemplate.class, qo, "nickname", "destName");
        model.addAttribute("page",userPage);
        return "index/searchUser";
    }

    //查询全部
    private String allSearch(Model model,SearchQueryObject qo){

        //查询目的地的内容
        List<DestinationTemplate> dests = destinationTemplateService.findByDestName(qo);

        Page<TravelTemplate> travel = searchUtilTemplateService.queryWithHighLight(TravelTemplate.INDEX_NAME, TravelTemplate.TYPE_NAME,
                TravelTemplate.class, qo, "title", "summary");

        Page<StrategyTemplate> strategy = searchUtilTemplateService.queryWithHighLight(StrategyTemplate.INDEX_NAME, StrategyTemplate.TYPE_NAME,
                StrategyTemplate.class, qo, "title", "subTitle","summary");

        Page<UserInfoTemplate> userPage = searchUtilTemplateService.queryWithHighLight(UserInfoTemplate.INDEX_NAME, UserInfoTemplate.TYPE_NAME,
                UserInfoTemplate.class, qo, "nickname", "destName");

        SearchResultVO data=new SearchResultVO();
        data.setUsers(userPage.getContent());
        data.setTravels(travel.getContent());
        data.setStrategys(strategy.getContent());
        data.setDests(dests);
        long total = travel.getTotalElements() + strategy.getTotalElements() + userPage.getTotalElements() + dests.size();
        data.setTotal(total);
        model.addAttribute("data",data);
        return "index/searchAll";
    }
}
