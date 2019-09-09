package cn.wolfcode.luowowo.comment.service;

import cn.wolfcode.luowowo.comment.domain.TravelComment;
import cn.wolfcode.luowowo.comment.query.TravelCommentQuery;
import org.springframework.data.domain.Page;

public interface ITravelCommentService {

    /**
     * 评论的分页查询
     * @param qo
     * @return
     */
    Page queryForList(TravelCommentQuery qo);


    /**
     * 保存游记评论
     * @param comment
     * @param floor
     */
    TravelComment saveOrUpdate(TravelComment comment,int floor);

    /**
     * 查询最新的游记评论
     * @param qc
     * @return
     */
    Page query(TravelCommentQuery qc);
}
