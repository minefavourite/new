package cn.wolfcode.luowowo.article.service;

import cn.wolfcode.luowowo.article.domain.Region;
import cn.wolfcode.luowowo.article.query.QueryRegion;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface IRegionService {
    PageInfo selectByPage(QueryRegion qr);

    List<Region> selectHotRegion();
}
