package cn.wolfcode.luowowo.article.mapper;

import cn.wolfcode.luowowo.article.domain.Region;
import cn.wolfcode.luowowo.article.query.QueryRegion;
import java.util.List;

public interface RegionMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Region record);

    Region selectByPrimaryKey(Long id);

    int updateByPrimaryKey(Region record);

    List<Region> pageRegion(QueryRegion qr);

    List<Region> selectHotRegion();

}