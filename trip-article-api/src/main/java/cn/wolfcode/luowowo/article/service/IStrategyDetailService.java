package cn.wolfcode.luowowo.article.service;


import cn.wolfcode.luowowo.article.domain.StrategyContent;
import cn.wolfcode.luowowo.article.domain.StrategyDetail;
import cn.wolfcode.luowowo.article.query.StrategyDetailQuery;
import cn.wolfcode.luowowo.article.vo.StrategyPersistenceStatisVO;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 攻略明细服务
 */
public interface IStrategyDetailService {

    /**
     * 分页查询
     * @param qo
     * @return
     */
    PageInfo query(StrategyDetailQuery qo);


    /**
     * 添加/更新
     * @param strategyDetail
     * @param tags
     */
    void saveOrUpdate(StrategyDetail strategyDetail, String tags);

    /**
     * 查询所有
     * @return
     */
    List<StrategyDetail> list();

    /**
     * 修改状态
     * @param id
     * @param state
     */
    void changeState(Long id, int state);

    /**
     * 查单个
     * @param id
     * @return
     */
    StrategyDetail get(Long id);

    /**
     * 查内容
     * @param id
     * @return
     */
    StrategyContent getContent(Long id);


    /**
     * 根据攻略分类id查询攻略明细
     * @param catalogId
     * @return
     */
    List<StrategyDetail> queryDetailByCatalogId(Long catalogId);

    /**
     * 查询顶尖的三篇攻略明细
     * @param destId
     * @return
     */
    List<StrategyDetail> queryDetailByDestIdTop3(Long destId);


    /**
     * 保存评论数
     * 暂时使用
     * @param replynum
     */
    void updateReplyNum(int replynum,Long id);

    /**
     * 进行数据落地
     * @param vo
     */
    void insertStrategy(StrategyPersistenceStatisVO vo);
}
