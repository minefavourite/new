package cn.wolfcode.luowowo.search.service.impl;

import cn.wolfcode.luowowo.search.query.SearchQueryObject;
import cn.wolfcode.luowowo.search.repository.TravelRepository;
import cn.wolfcode.luowowo.search.service.ITravelTemplateService;
import cn.wolfcode.luowowo.search.template.TravelTemplate;
import com.alibaba.dubbo.config.annotation.Service;
import org.elasticsearch.client.transport.TransportClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;

import java.util.List;

@Service
public class TravelTemplateServiceImpl implements ITravelTemplateService {

    @Autowired
    private TravelRepository travelRepository;

    @Autowired
    private ElasticsearchTemplate template;

    //转换端口中心
    @Autowired
    private TransportClient client;

    @Override
    public void save(TravelTemplate t) {
travelRepository.save(t);
    }

    @Override
    public List<TravelTemplate> findByDestName(SearchQueryObject qo) {
        List<TravelTemplate> travelTemplates = travelRepository.findByDestName(qo.getKeyword());
        return travelTemplates;
    }
}
