package cn.wolfcode.luowowo.search.service;

import cn.wolfcode.luowowo.search.query.SearchQueryObject;
import cn.wolfcode.luowowo.search.template.DestinationTemplate;

import javax.print.attribute.standard.Destination;
import java.util.List;

/**
 * 用户搜索
 */
public interface IDestinationTemplateService {

    void save(DestinationTemplate t);

    /**
     * 查询目的地信息
     * @param qo
     * @return
     */
    List<DestinationTemplate> findByDestName(SearchQueryObject qo);
}
