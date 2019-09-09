package cn.wolfcode.luowowo.mgriste.controller;

import cn.wolfcode.luowowo.article.domain.Destination;
import cn.wolfcode.luowowo.article.query.QueryRegion;
import cn.wolfcode.luowowo.article.service.IDestinationService;
import cn.wolfcode.luowowo.article.service.IRegionService;
import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("region")
public class RegionController {

    @Reference
    private IRegionService regionService;

    @Reference
    private IDestinationService destinationService;

    @RequestMapping("/list")
    public String list(Model model,@ModelAttribute("qo") QueryRegion qr){

        PageInfo pageInfo = regionService.selectByPage(qr);
        model.addAttribute("pageInfo",pageInfo);
        return "region/list";
    }

    //查詢所有的目的地
    @RequestMapping("/getDestByDeep")
    @ResponseBody
    public Object getDestByDeep(int deep){
        List<Destination> destinations = destinationService.selectDestByDeep(deep);
        return destinations;
    }
}
