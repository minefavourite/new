package cn.wolfcode.luowowo.search.service;

import cn.wolfcode.luowowo.search.query.SearchQueryObject;
import cn.wolfcode.luowowo.search.template.DestinationTemplate;
import cn.wolfcode.luowowo.search.template.TravelTemplate;

import java.util.List;

/**
 * 游记搜索
 */
public interface ITravelTemplateService {

    void save(TravelTemplate t);

    List<TravelTemplate> findByDestName(SearchQueryObject qo);
}
