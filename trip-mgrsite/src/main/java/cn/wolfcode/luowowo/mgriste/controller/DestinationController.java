package cn.wolfcode.luowowo.mgriste.controller;

import cn.wolfcode.luowowo.article.domain.Destination;
import cn.wolfcode.luowowo.article.query.QueryDestination;
import cn.wolfcode.luowowo.article.service.IDestinationService;
import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
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

    @RequestMapping("/list")
    public String listDestination(Model model,@ModelAttribute("qo") QueryDestination qd){
        PageInfo<Destination> pageInfo = destinationService.queryForList(qd);
        model.addAttribute("pageInfo",pageInfo);
        List<Destination> toasts = destinationService.getToasts(qd.getParentId());
        model.addAttribute("toasts",toasts);
        return "destination/list";
    }
}
