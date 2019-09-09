package cn.wolfcode.luowowo.search.repository;

import cn.wolfcode.luowowo.search.template.DestinationTemplate;
import cn.wolfcode.luowowo.search.template.UserInfoTemplate;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DestinationRepository extends ElasticsearchRepository<DestinationTemplate,Long>{
    /**
     * 查询相关目的地信息
     * @param keyword
     * @return
     */
    List<DestinationTemplate> findByName(String keyword);
}
