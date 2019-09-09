package cn.wolfcode.luowowo.website.controller;

import cn.wolfcode.luowowo.article.domain.*;
import cn.wolfcode.luowowo.article.query.StrategyCatalogQuery;
import cn.wolfcode.luowowo.article.query.TravelQuery;
import cn.wolfcode.luowowo.article.service.*;
import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import org.springframework.data.annotation.Id;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/destination")
public class DestinationController {

    @Reference
    private IDestinationService destinationService;

    @Reference
    private IRegionService regionService;

    @Reference
    private IStrategyCatalogService strategyCatalogService;

    @Reference
    private IStrategyDetailService strategyDetailService;

    @Reference
    private ITravelService travelService;

    @RequestMapping("")
    public String selectDestination(Model model){
        List<Region> regions = regionService.selectHotRegion();
        model.addAttribute("hotRegions",regions);
        return "destination/index";
    }


    @RequestMapping("/getHotDestByRegionId")
    public String getHotDestByRegionId(Model model,Long regionId){
        List<Destination> destinations = destinationService.selectDestByRegionId(regionId);
        int size = destinations.size();
        //进行分组
        model.addAttribute("leftDests",destinations.subList(0,size/2+1));
        model.addAttribute("rightDests",destinations.subList(size/2+1,size));
        model.addAttribute("regionId",regionId);
        return "destination/hotdestTpl";

    }
    @RequestMapping("/guide")
    public String guide( Model model,Long id){
        List<Destination> toasts = destinationService.getToasts(id);
        Destination destination = toasts.remove(toasts.size() - 1);
        model.addAttribute("toasts",toasts);
        model.addAttribute("dest",destination);

        //查询概况下的内容
        //查询攻略分类
        List<StrategyCatalog> catalogs = strategyCatalogService.queryCatalogByDestId(id);
        model.addAttribute("catalogs",catalogs);

        //攻略明细的前三篇 根据目的地id
        List<StrategyDetail> details = strategyDetailService.queryDetailByDestIdTop3(id);
        model.addAttribute("strategyDetails",details);

        return "destination/guide";
    }


    //查询攻略分类的内容界面
    @RequestMapping("/surveyPage")
    public String surveyPage(Model model, @ModelAttribute("qo")StrategyCatalogQuery qo){
        //吐司操作
        List<Destination> toasts = destinationService.getToasts(qo.getDestId());
        Destination destination = toasts.remove(toasts.size() - 1);
        model.addAttribute("toasts",toasts);
        model.addAttribute("dest",destination);
        return "destination/survey";

    }
    @RequestMapping("/survey")
    public String survey(Model model,@ModelAttribute("qo")StrategyCatalogQuery qo){
        //查询攻略分类
        List<StrategyCatalog> catalogs = strategyCatalogService.queryCatalogByDestId(qo.getDestId());
        model.addAttribute("catalogs",catalogs);

        //查询单个的攻略分类
        StrategyCatalog catalog = strategyCatalogService.get(qo.getCatalogId());
        model.addAttribute("catalog",catalog);
        //查询攻略明细,并显示第一篇
        List<StrategyDetail> details = strategyDetailService.queryDetailByCatalogId(qo.getCatalogId());
        StrategyDetail detail = details.get(0);
        StrategyContent content = strategyDetailService.getContent(detail.getId());
        detail.setStrategyContent(content);
        model.addAttribute("detail",detail);

        return "destination/surveyTpl";
    }

    @RequestMapping("/travels")
    public String travel(Model model, @ModelAttribute("qo") TravelQuery qo){
        //将状态设置为已发布的
        // 前端界面只查询已发布的,且公开的
        qo.setState(Travel.STATE_RELEASE);
        PageInfo<Travel> pageInfo = travelService.queryForList(qo);
        model.addAttribute("pageInfo",pageInfo);
        return "destination/travelTpl";
    }


}
