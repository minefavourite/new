package cn.wolfcode.luowowo.search.repository;

import cn.wolfcode.luowowo.search.template.StrategyTemplate;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StrategyRepository extends ElasticsearchRepository<StrategyTemplate,Long>{

    /**
     * 查询相关攻略
     * @param keyword
     * @return
     */
    List<StrategyTemplate> findByDestName(String keyword);
}
