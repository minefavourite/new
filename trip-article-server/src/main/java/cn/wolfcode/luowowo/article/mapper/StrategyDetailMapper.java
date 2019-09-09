package cn.wolfcode.luowowo.article.mapper;

import cn.wolfcode.luowowo.article.domain.StrategyDetail;
import cn.wolfcode.luowowo.article.query.StrategyDetailQuery;
import cn.wolfcode.luowowo.article.vo.StrategyPersistenceStatisVO;
import cn.wolfcode.luowowo.cache.vo.StrategyStatisVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface StrategyDetailMapper {
    int deleteByPrimaryKey(Long id);

    int insert(StrategyDetail record);

    StrategyDetail selectByPrimaryKey(Long id);

    List<StrategyDetail> selectAll();

    int updateByPrimaryKey(StrategyDetail record);

    List<StrategyDetail> selectForList(StrategyDetailQuery qo);

    void updateState(@Param("id") Long id, @Param("state") int state);

    void insertRelation(@Param("detailId") Long detailId, @Param("tagId") Long tagId);

    void deleteRelation(Long id);


    List<StrategyDetail> selectDetailByCatalogId(Long catalogId);

    List<StrategyDetail> selectDetailsTop3(Long destId);


    void updateReplyNum(@Param("replynum") int replynum, @Param("id") Long id);

    void updateDetailByVo(StrategyPersistenceStatisVO vo);
}