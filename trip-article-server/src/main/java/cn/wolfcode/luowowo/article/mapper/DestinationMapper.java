package cn.wolfcode.luowowo.article.mapper;

import cn.wolfcode.luowowo.article.domain.Destination;
import cn.wolfcode.luowowo.article.query.QueryDestination;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DestinationMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Destination record);

    Destination selectByPrimaryKey(Long id);

    List<Destination> selectAll();

    int updateByPrimaryKey(Destination record);

    List<Destination> queryForList(QueryDestination qd);

    List<Destination> selectDestByDeep(int deep);

    Destination selectDestByParentId(@Param("parentId") Long parentId);

    List<Destination> selectDestsByRegionId(Long[] regionId);

}