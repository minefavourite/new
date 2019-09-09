package cn.wolfcode.luowowo.comment.service;

import cn.wolfcode.luowowo.comment.domain.StrategyComment;
import cn.wolfcode.luowowo.comment.query.StrategyCommentQuery;
import org.springframework.data.domain.Page;

public interface IStrategyCommentService {
    /**
     * 保存评论
     * @param comment
     */
    void saveOrUpdate(StrategyComment comment);

    /**
     * 评论的分页查询
     * @param qo
     * @return
     */
    Page queryForList(StrategyCommentQuery qo);

    /**
     * 点赞功能
     * @param toId
     * @param userId
     */
    StrategyComment ThumbUp(String toId, Long userId);
}
