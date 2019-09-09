package cn.wolfcode.luowowo.mgriste.controller;

import cn.wolfcode.luowowo.article.domain.Strategy;
import cn.wolfcode.luowowo.article.query.StrategyQuery;
import cn.wolfcode.luowowo.article.service.IDestinationService;
import cn.wolfcode.luowowo.article.service.IStrategyService;
import cn.wolfcode.luowowo.common.utils.JsonResult;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("strategy")
public class StrategyController {

    @Reference
    private IStrategyService strategyService;

    @Reference
    private IDestinationService destinationService;




    @RequestMapping("/list")
    public String list(Model model, @ModelAttribute("qo")StrategyQuery qo){

        //pageInfo

        model.addAttribute("pageInfo", strategyService.query(qo));

        return "strategy/list";
    }


    @RequestMapping("/getDestByDeep")
    @ResponseBody
    public Object getDestByDeep(int deep){
        return destinationService.selectDestByDeep(deep);
    }

    @RequestMapping("/saveOrUpdate")
    @ResponseBody
    public Object saveOrUpdate(Strategy  strategy){

        strategyService.saveOrUpdate(strategy);

        return new JsonResult();
    }

}
