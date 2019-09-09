package cn.wolfcode.luowowo.search.service.impl;

import cn.wolfcode.luowowo.search.query.SearchQueryObject;
import cn.wolfcode.luowowo.search.repository.UserInfoRepository;
import cn.wolfcode.luowowo.search.service.IUserInfoTemplateService;
import cn.wolfcode.luowowo.search.template.UserInfoTemplate;
import com.alibaba.dubbo.config.annotation.Service;
import org.elasticsearch.client.transport.TransportClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;

import java.util.List;

@Service
public class UserInfoTemplateServiceImpl implements IUserInfoTemplateService {

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private ElasticsearchTemplate template;

    //转换端口中心
    @Autowired
    private TransportClient client;

    @Override
    public void save(UserInfoTemplate t) {
        userInfoRepository.save(t);
    }

    @Override
    public List<UserInfoTemplate> findByDestName(SearchQueryObject qo) {
        List<UserInfoTemplate> userinfos = userInfoRepository.findByDestName(qo.getKeyword());
        return userinfos;
    }
}
