package cn.wolfcode.luowowo.search.service.impl;

import cn.wolfcode.luowowo.search.query.SearchQueryObject;
import cn.wolfcode.luowowo.search.repository.DestinationRepository;
import cn.wolfcode.luowowo.search.service.IDestinationTemplateService;
import cn.wolfcode.luowowo.search.template.DestinationTemplate;
import com.alibaba.dubbo.config.annotation.Service;
import org.elasticsearch.client.transport.TransportClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;

import javax.print.attribute.standard.Destination;
import java.util.List;

@Service
public class DestinationTemplateServiceImpl implements IDestinationTemplateService {

    @Autowired
    private DestinationRepository destinationRepository;

    @Autowired
    private ElasticsearchTemplate template;

    //转换端口中心
    @Autowired
    private TransportClient client;

    @Override
    public void save(DestinationTemplate t) {
        destinationRepository.save(t);
    }

    @Override
    public  List<DestinationTemplate> findByDestName(SearchQueryObject qo) {

        List<DestinationTemplate> destinationTemplates = destinationRepository.findByName(qo.getKeyword());
        return destinationTemplates;
    }
}
