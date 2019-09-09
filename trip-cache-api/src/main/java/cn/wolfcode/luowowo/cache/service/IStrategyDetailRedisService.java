package cn.wolfcode.luowowo.cache.service;

import cn.wolfcode.luowowo.cache.vo.StrategyStatisVO;

import java.util.List;

/**
 * 攻略统计数
 */
public interface IStrategyDetailRedisService {
    /**
     * 往Redis中添加攻略的阅读量
     * @param id
     * @param num
     */
    void increaseViewnum(Long id, int num);

    /**
     * 保存评论计数
     * @param detailId
     * @param num
     */
    void increaseReplyNum(Long detailId ,int num);

    /**
     * 查找Redis中vo对象
     * @param id
     * @return
     */
    StrategyStatisVO getStrategyRedisVO(Long id);

    /**
     * 收藏攻略操作
     * @param sid
     * @param uid
     * @return  true 表示收藏成功,false 表示收藏失败
     */
    boolean increaseFavor(Long sid, Long uid);

    /**
     * 点赞操作
     * @param sid
     * @param id
     * @return true 表示点赞成功 false 表示已经点赞过
     */
    boolean thumbUp(Long sid, Long id);

    /**
     * 判断Redis中是否存在该vo对象
     * @param id
     * @return true 表示存在 false 表示不存在
     */
    boolean queryStrategyRedisVo(Long id);

    /**
     * 同步到Redis中
     * @param vo
     */
    void saveStrategyToRedis(StrategyStatisVO vo);

    /**
     * 查询Redis中vo对象
     * @param name
     */
    List<StrategyStatisVO> queryStrategyFromRedisByPrefix(String name);
}
