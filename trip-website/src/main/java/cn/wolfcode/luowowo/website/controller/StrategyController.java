package cn.wolfcode.luowowo.website.controller;

import cn.wolfcode.luowowo.article.domain.*;
import cn.wolfcode.luowowo.article.query.StrategyDetailQuery;
import cn.wolfcode.luowowo.article.service.IDestinationService;
import cn.wolfcode.luowowo.article.service.IStrategyCommendService;
import cn.wolfcode.luowowo.article.service.IStrategyDetailService;
import cn.wolfcode.luowowo.article.service.IStrategyTagService;
import cn.wolfcode.luowowo.cache.service.IRecommendDetailRedisService;
import cn.wolfcode.luowowo.cache.service.IStrategyDetailRedisService;
import cn.wolfcode.luowowo.cache.vo.StrategyStatisVO;
import cn.wolfcode.luowowo.comment.domain.StrategyComment;
import cn.wolfcode.luowowo.comment.query.StrategyCommentQuery;
import cn.wolfcode.luowowo.comment.service.IStrategyCommentService;
import cn.wolfcode.luowowo.common.annoations.CheckLogin;
import cn.wolfcode.luowowo.common.annoations.UserParam;
import cn.wolfcode.luowowo.common.enums.ConstantEnum;
import cn.wolfcode.luowowo.common.utils.JsonResult;
import cn.wolfcode.luowowo.member.domain.UserInfo;
import cn.wolfcode.luowowo.search.vo.StrategyQueryTemplate;
import cn.wolfcode.luowowo.search.query.SearchQueryObject;
import cn.wolfcode.luowowo.search.service.IStrategyTemplateService;
import cn.wolfcode.luowowo.search.template.StrategyTemplate;
import cn.wolfcode.luowowo.search.vo.StatisVO;
import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 游记接口
 */
@Controller
@RequestMapping("/strategy")
public class StrategyController {

    @Reference
    private IStrategyDetailService strategyDetailService;

    @Reference
    private IStrategyTagService strategyTagService;

    @Reference
    private IDestinationService destinationService;

    @Reference
    private IStrategyCommentService commentService;

    @Reference
    private IStrategyDetailRedisService redisService;

    @Reference
    private IStrategyCommendService commendService;

    @Reference
    private IRecommendDetailRedisService commendRedisService;

    //调用ES查找主题排行
    @Reference
    private IStrategyTemplateService templateService;

    //攻略推荐首页
    @RequestMapping("")
    public String index(Model model){
        List<StrategyCommend> commends = commendService.queryCommendTop5();
        model.addAttribute("commends",commends);

        //查询攻略推荐
        List<StrategyStatisVO> list = commendRedisService.queryAllStrategyCommend();
       //设计两个集合
        List<StrategyStatisVO> abroadCds= new ArrayList<>();
        List<StrategyStatisVO> unabroadCds= new ArrayList<>();
        //判断是国外还是国内
        for (StrategyStatisVO vo : list) {
            if (abroadCds.size()==10 && unabroadCds.size()==10){
                break;
            }
            if (vo.isIsabroad()){
                if (abroadCds.size()<10){
                abroadCds.add(vo);
                }
            }else {
                if (unabroadCds.size()<10){
                    unabroadCds.add(vo);
                }
            }
        }
        model.addAttribute("abroadCds",abroadCds);
        model.addAttribute("unabroadCds",unabroadCds);

        //热门推荐
        List<StrategyStatisVO> hotList = commendRedisService.queryAllHotDetail();
        model.addAttribute("hotCds",hotList);

        //主题推荐
       List<Map<String,Object>> themeList= templateService.queryThemeAndDest();
       model.addAttribute("themeCds",themeList);

       //查询国内的相关攻略
        List<StatisVO> chinas = templateService.queryStrategyGroup(StrategyQueryTemplate.STRATEGY_CHINA_COMMMEND);
        model.addAttribute("chinas",chinas);

        //查询国外的攻略
        List<StatisVO> abroads = templateService.queryStrategyGroup(StrategyQueryTemplate.STRATEGY_ABROAD_COMMMEND);
        model.addAttribute("abroads",abroads);

        //查询主题攻略
        List<StatisVO> themes = templateService.queryStrategyGroup(StrategyQueryTemplate.STRATEGY_THEME_COMMMEND);
        model.addAttribute("themes",themes);


        return "strategy/index";
    }

    //根据id查询游记明细文章
    @RequestMapping("/detail")
    public String showtrategy(Model model,Long id){
        StrategyDetail detail = strategyDetailService.get(id);
        StrategyContent content = strategyDetailService.getContent(id);
        detail.setStrategyContent(content);
        model.addAttribute("detail",detail);
        //添加阅读数
        redisService.increaseViewnum(id, 1);
        //查找Redis中的统计数
        StrategyStatisVO vo = redisService.getStrategyRedisVO(id);
        model.addAttribute("vo",vo);
        commendRedisService.addHotNum(ConstantEnum.append(ConstantEnum.STRATEGY_STATIS_VO.getName(),id.toString()),1);
        return "strategy/detail";
    }

    //根据目的地查询所有攻略
    @RequestMapping("/list")
    public String listDetail(Model model, @ModelAttribute("qo")StrategyDetailQuery qo){
        //吐司操作
        List<Destination> toasts = destinationService.getToasts(qo.getDestId());
        Destination destination = toasts.remove(toasts.size() - 1);
        model.addAttribute("toasts",toasts);
        model.addAttribute("dest",destination);
        //查询所有的标签
        List<StrategyTag> tags = strategyTagService.list();
        model.addAttribute("tags",tags);
        //查询所有相关目的地下的攻略
        PageInfo query = strategyDetailService.query(qo);
        model.addAttribute("pageInfo",query);
        return "strategy/list";
    }

    //添加评论
    @RequestMapping("/commentAdd")
    @ResponseBody
    public JsonResult addComment(StrategyComment comment, @UserParam UserInfo userInfo){
        if (userInfo==null){
            return new JsonResult(false,"/login.html");
        }
        comment.setCity(userInfo.getCity());
        comment.setLevel(userInfo.getLevel());
        comment.setHeadUrl(userInfo.getHeadImgUrl());
        comment.setUserId(userInfo.getId());
        comment.setUsername(userInfo.getNickname());
        commentService.saveOrUpdate(comment);
        //当添加一条评论时,评论计数就加1
        redisService.increaseReplyNum(comment.getDetailId(),1);
        //热门推荐分数加一
        commendRedisService.addHotNum(ConstantEnum.append(ConstantEnum.STRATEGY_STATIS_VO.getName(),comment.getDetailId().toString()),1);
        return new JsonResult().addData(redisService.getStrategyRedisVO(comment.getDetailId()).getReplynum());
    }

    //查询攻略评论
    @RequestMapping("/comment")
    public String comment(Model model, @ModelAttribute("qo") StrategyCommentQuery qo,@UserParam UserInfo userInfo){

        model.addAttribute("page", commentService.queryForList(qo));
        model.addAttribute("userInfo",userInfo);
        return "strategy/commentTpl";
    }


    //点赞操作
    //一个参数是评论的id
    //一个是当前用户的id
    @CheckLogin
    @RequestMapping("/commentThumbUp")
    @ResponseBody
    public JsonResult commentThumbUp(String toid,Long fromid){
        StrategyComment comment = commentService.ThumbUp(toid, fromid);
        JsonResult result=new JsonResult();
        result.setObject(comment);
        return result;
    }

    //收藏操作
    @RequestMapping("/favor")
    @ResponseBody
    public JsonResult favor(Long sid,@UserParam UserInfo userInfo){
        //点击收藏时,要表明是哪个用户收藏了该篇攻略
        if(userInfo==null){
            return new JsonResult("请先登录",JsonResult.CODE_TYPE_UNLOGIN);
        }
        boolean flag = redisService.increaseFavor(sid, userInfo.getId());
        StrategyStatisVO redisVO = redisService.getStrategyRedisVO(sid);

        //收藏时,推荐数加一
        commendRedisService.addCommendNum(ConstantEnum.append(ConstantEnum.STRATEGY_STATIS_VO.getName(),sid.toString()),flag?1:-1);

        return new JsonResult(flag,"").addData(redisVO);
    }

    @RequestMapping("/strategyThumbup")
    @ResponseBody
    public JsonResult thumbUp(Long sid,@UserParam UserInfo userInfo){
        if(userInfo==null){
            return new JsonResult("请先登录",JsonResult.CODE_TYPE_UNLOGIN);
        }
        boolean flag = redisService.thumbUp(sid, userInfo.getId());
        StrategyStatisVO redisVO = redisService.getStrategyRedisVO(sid);
        //点赞时,推荐数加一
        commendRedisService.addCommendNum(ConstantEnum.append(ConstantEnum.STRATEGY_STATIS_VO.getName(),sid.toString()),1);
        return new JsonResult(flag,"").addData(redisVO);
    }

    //查询每个攻略下的具体内容
    @RequestMapping("/searchPage")
    public String searchPage(Model model,@ModelAttribute("qo") SearchQueryObject qo){
        Page<StrategyTemplate> templates = templateService.queryStrategyByQo(qo);
        model .addAttribute("page",templates);
        return "strategy/searchPageTpl";
    }
}
