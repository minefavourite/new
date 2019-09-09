package cn.wolfcode.luowowo.cache.service;

import cn.wolfcode.luowowo.cache.vo.TravelStatisVO;

/**
 * 游记统计数
 */
public interface ITravelDetailRedisService {

    /**
     * 点赞操作
     * @param tid
     * @param id
     * @return
     */
    boolean thumbUp(Long tid, Long id);

    /**
     * 查找travelVo对象
     * @param tid
     */
    TravelStatisVO getTravelRedisVO(Long tid);

    /**
     * 添加阅读量
     * @param id
     * @param num
     */
    void increaseViewnum(Long id, int num);

    /**
     * 添加评论量
     * @param id
     * @param num
     */
    void increaseReplyNum(Long id, int num);

    /**
     * 收藏操作
     *
     * @param tid
     * @param uid
     * @return
     */
    boolean increaseFavor(Long tid, Long uid);

    /**
     * 判断是否已经收藏游记
     * @param uid
     * @return  true 表示收藏,false 表示没有
     */
    boolean isUserHasTravel(Long uid,Long tid);
}
