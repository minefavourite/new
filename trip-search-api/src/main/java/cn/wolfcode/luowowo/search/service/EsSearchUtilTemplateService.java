package cn.wolfcode.luowowo.search.service;

import cn.wolfcode.luowowo.search.query.SearchQueryObject;
import org.springframework.data.domain.Page;

public interface EsSearchUtilTemplateService {

    /**
     * 高亮查询
     * @param indexName
     * @param typeName
     * @param clz
     * @param qo
     * @param <T>
     * @return
     */
    <T>Page<T> queryWithHighLight(String indexName, String typeName, Class<T> clz, SearchQueryObject qo,String...fields);
}
