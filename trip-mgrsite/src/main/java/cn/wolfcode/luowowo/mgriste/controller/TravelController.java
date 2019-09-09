package cn.wolfcode.luowowo.mgriste.controller;

import cn.wolfcode.luowowo.article.domain.Travel;
import cn.wolfcode.luowowo.article.domain.TravelContent;
import cn.wolfcode.luowowo.article.query.TravelQuery;
import cn.wolfcode.luowowo.article.service.ITravelService;
import cn.wolfcode.luowowo.common.utils.JsonResult;
import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/travel")
public class TravelController {
    @Reference
    private ITravelService travelService;

    @RequestMapping("/list")
    public String listAll(Model model, @ModelAttribute("qo") TravelQuery qt){
        PageInfo<Travel> pageInfos = travelService.queryForList(qt);
        model.addAttribute("pageInfo",pageInfos);
        return "travel/list";
    }

    //更改状态
    @RequestMapping("/updateState")
    @ResponseBody
    public JsonResult updateState(Long id, Integer state){
        JsonResult result=new JsonResult();
        travelService.updateState(id,state);
        return result;

    }

    //查看文章
    @RequestMapping("/searchArticle")
    @ResponseBody
    public JsonResult searchArticle(Long id){
        TravelContent travelContent = travelService.queryContent(id);
        JsonResult result=new JsonResult();
        result.setObject(travelContent);
        return result;
    }
}
