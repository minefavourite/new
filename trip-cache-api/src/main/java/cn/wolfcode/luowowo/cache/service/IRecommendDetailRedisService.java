package cn.wolfcode.luowowo.cache.service;

import cn.wolfcode.luowowo.cache.vo.StrategyStatisVO;

import java.util.List;

/**
 * 攻略推荐
 */
public interface IRecommendDetailRedisService {

    /**
     * 国内外排行榜分数加减
     * @param name
     * @param num
     */
    void addCommendNum(String name, int num);

    /**
     * 查询所有的攻略推荐
     * @return
     */
    List<StrategyStatisVO> queryAllStrategyCommend();

    /**
     * 初始化时查询zset中是否有该vokey
     * @param keyZset
     * @return true 表示有, false 表示没有
     */
    boolean queryCommendByZset(String keyZset,Long sid);

    /**
     * 热门推荐分数加分
     * @param name
     * @param num
     */
    void addHotNum(String name, int num);

    /**
     * 热门推荐查询
     * @return
     */
    List<StrategyStatisVO> queryAllHotDetail();
}
