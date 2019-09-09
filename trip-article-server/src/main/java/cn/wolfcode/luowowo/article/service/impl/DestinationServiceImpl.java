package cn.wolfcode.luowowo.article.service.impl;

import cn.wolfcode.luowowo.article.domain.Destination;
import cn.wolfcode.luowowo.article.domain.Region;
import cn.wolfcode.luowowo.article.mapper.DestinationMapper;
import cn.wolfcode.luowowo.article.mapper.RegionMapper;
import cn.wolfcode.luowowo.article.query.QueryDestination;
import cn.wolfcode.luowowo.article.service.IDestinationService;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class DestinationServiceImpl implements IDestinationService {

    @Autowired
    private DestinationMapper destinationMapper;

    @Autowired
    private RegionMapper regionMapper;
    @Override
    public PageInfo<Destination> queryForList(QueryDestination qd) {
        PageHelper.startPage(qd.getCurrentPage(),qd.getPageSize());
        List<Destination> destinations = destinationMapper.queryForList(qd);
        return new PageInfo<>(destinations);
    }

    @Override
    public List<Destination> selectDestByDeep(int deep) {
        List<Destination> destinations = destinationMapper.selectDestByDeep(deep);
        return destinations;
    }

    @Override
    public List<Destination> getToasts(Long parentId) {

        List<Destination> toasts=new ArrayList<>();
        addToastToList(parentId, toasts);
        Collections.reverse(toasts);
        return toasts;
    }

    @Override
    public List<Destination> selectDestByRegionId(Long regionId) {
        List<Destination> destinations=null;
        if (regionId==-1){
            Long[] regionIds=new Long[]{1l};
            destinations = destinationMapper.selectDestsByRegionId(regionIds);
        }else {
        Region region = regionMapper.selectByPrimaryKey(regionId);
            Long[] refIds = region.getRefIds();
            destinations= destinationMapper.selectDestsByRegionId(refIds);
        }
        return destinations;
    }

    @Override
    public boolean isAbroad(Long id) {
        List<Destination> toasts=new ArrayList<>();
        addToastToList(id,toasts);
        Collections.reverse(toasts);
        Destination destination = toasts.get(0);
        if (destination.getId()==1) {
            return false;
        }else {
            return true;
        }
    }

    @Override
    public Destination getCountry(Long id) {
        //根 >> 中国 >> 省份 >> 广州
        List<Destination> toasts = this.getToasts(id);
        if(toasts != null && toasts.size() > 0 ){
            return toasts.get(0);
        }
        return null;
    }

    @Override
    public Destination getProvince(Long id) {
        if(this.isAbroad(id)){
            return null;
        }

        List<Destination> toasts = this.getToasts(id);
        if (toasts!=null && toasts.size()>1){
            return toasts.get(1);
        }
        return null;
    }

    @Override
    public List<Destination> list() {
        List<Destination> destinations = destinationMapper.selectAll();
        return destinations;
    }


    //定义一个方法将查询出来的数据存入list集合中
    private void addToastToList(Long parentId,List toasts){
        //如果传入的值为-1,找不到结果,直接结束
        Destination destination = destinationMapper.selectDestByParentId(parentId);
        if (destination==null){
            return;
        }
        toasts.add(destination);
        if (destination.getParent()!=null){
            addToastToList(destination.getParent().getId(),toasts);
        }
    }
}
