package cn.wolfcode.luowowo.search.repository;

import cn.wolfcode.luowowo.search.template.TravelTemplate;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TravelRepository extends ElasticsearchRepository<TravelTemplate,Long>{

    /**
     * 查询相关的游记
     * @param keyword
     * @return
     */
    List<TravelTemplate> findByDestName(String keyword);
}
