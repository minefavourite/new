package cn.wolfcode.luowowo.search.repository;

import cn.wolfcode.luowowo.search.template.UserInfoTemplate;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserInfoRepository extends ElasticsearchRepository<UserInfoTemplate,Long>{
    /**
     * 查询用户
     * @param keyword
     * @return
     */
    List<UserInfoTemplate> findByDestName(String keyword);
}
