package cn.wolfcode.luowowo.article.mapper;

import cn.wolfcode.luowowo.article.domain.Travel;
import cn.wolfcode.luowowo.article.query.TravelQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TravelMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Travel record);

    Travel selectByPrimaryKey(Long id);

    List<Travel> selectAll();

    int updateByPrimaryKey(Travel record);

    List<Travel> queryForList(TravelQuery qo);

    List<Travel> queryTravelByDestIdTop3(Long destId);

    void updateState(@Param("id") Long id, @Param("state") Integer state);
}