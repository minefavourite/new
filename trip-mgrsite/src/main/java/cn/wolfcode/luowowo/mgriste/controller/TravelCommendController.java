package cn.wolfcode.luowowo.mgriste.controller;

import cn.wolfcode.luowowo.article.domain.TravelCommend;
import cn.wolfcode.luowowo.article.query.TravelCommendQuery;
import cn.wolfcode.luowowo.article.service.IDestinationService;
import cn.wolfcode.luowowo.article.service.ITravelCommendService;
import cn.wolfcode.luowowo.article.service.ITravelService;
import cn.wolfcode.luowowo.common.utils.JsonResult;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("travelCommend")
public class TravelCommendController {

    @Reference
    private ITravelCommendService travelCommendService;

    @Reference
    private IDestinationService destinationService;

    @Reference
    private ITravelService travelDetailService;




    @RequestMapping("/list")
    public String list(Model model, @ModelAttribute("qo")TravelCommendQuery qo){

        //pageInfo

        model.addAttribute("pageInfo", travelCommendService.query(qo));

        model.addAttribute("details", travelDetailService.list());

        return "travelCommend/list";
    }


    @RequestMapping("/saveOrUpdate")
    @ResponseBody
    public Object saveOrUpdate(TravelCommend travelCommend){

        travelCommendService.saveOrUpdate(travelCommend);

        return new JsonResult();
    }

}
