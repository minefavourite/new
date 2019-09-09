package cn.wolfcode.luowowo.article.service.impl;

import cn.wolfcode.luowowo.article.domain.Region;
import cn.wolfcode.luowowo.article.mapper.RegionMapper;
import cn.wolfcode.luowowo.article.query.QueryRegion;
import cn.wolfcode.luowowo.article.service.IRegionService;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class RegionServiceImpl implements IRegionService {

    @Autowired
    private RegionMapper regionMapper;
    @Override
    public PageInfo selectByPage(QueryRegion qr) {
       PageHelper.startPage(qr.getCurrentPage(), qr.getPageSize());
        List<Region> regions = regionMapper.pageRegion(qr);
        return new PageInfo<>(regions);
    }

    @Override
    public List<Region> selectHotRegion() {
        List<Region> regions = regionMapper.selectHotRegion();
        return regions;
    }
}
