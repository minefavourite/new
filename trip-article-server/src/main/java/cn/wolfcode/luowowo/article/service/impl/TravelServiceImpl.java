package cn.wolfcode.luowowo.article.service.impl;

import cn.wolfcode.luowowo.article.domain.Travel;
import cn.wolfcode.luowowo.article.domain.TravelContent;
import cn.wolfcode.luowowo.article.mapper.TravelContentMapper;
import cn.wolfcode.luowowo.article.mapper.TravelMapper;
import cn.wolfcode.luowowo.article.query.TravelQuery;
import cn.wolfcode.luowowo.article.service.ITravelService;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

@Service
public class TravelServiceImpl implements ITravelService {
    @Autowired
    private TravelMapper travelMapper;

    @Autowired
    private TravelContentMapper contentMapper;

    @Override
    public PageInfo<Travel> queryForList(TravelQuery qo) {
        PageHelper.startPage(qo.getCurrentPage(),qo.getPageSize(),qo.getOrderBy());
        List<Travel> travels = travelMapper.queryForList(qo);
        return new PageInfo<>(travels);
    }

    @Override
    public Travel getTravel(Long id) {
        Travel travel = travelMapper.selectByPrimaryKey(id);
        TravelContent travelContent = contentMapper.selectById(travel.getId());
        travel.setTravelContent(travelContent);
        return travel;
    }

    @Override
    public List<Travel> queryTravelByDestIdTop3(Long destId) {
        List<Travel> travels = travelMapper.queryTravelByDestIdTop3(destId);
        return travels;
    }

    @Override
    public Long insert(Travel travel) {
        //截取字段作为概括
        String content = travel.getTravelContent().getContent();
        if (content.length()>200){
            travel.setSummary(content.substring(0,200));
        }else {
            travel.setSummary(content);
        }
        //最后更新时间
        travel.setLastUpdateTime(new Date());
        if (travel.getId()!=null){
            travelMapper.updateByPrimaryKey(travel);
            TravelContent travelContent=travel.getTravelContent();
            travelContent.setId(travel.getId());
            contentMapper.insert(travelContent);
        }else {
            //写游记的时间
            travel.setCreateTime(new Date());
            travelMapper.insert(travel);
            TravelContent travelContent=travel.getTravelContent();
            travelContent.setId(travel.getId());
            contentMapper.insert(travelContent);
        }

        return travel.getId();
    }

    @Override
    public void updateState(Long id, Integer state) {
        travelMapper.updateState(id,state);
    }

    @Override
    public TravelContent queryContent(Long id) {
        TravelContent travelContent = contentMapper.selectById(id);
        return travelContent;
    }

    @Override
    public List<Travel> list() {
        List<Travel> travels = travelMapper.selectAll();
        return travels;
    }
}
