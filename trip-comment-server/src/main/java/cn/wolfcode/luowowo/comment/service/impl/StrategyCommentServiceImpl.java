package cn.wolfcode.luowowo.comment.service.impl;

import cn.wolfcode.luowowo.article.service.IStrategyDetailService;
import cn.wolfcode.luowowo.comment.domain.StrategyComment;
import cn.wolfcode.luowowo.comment.query.StrategyCommentQuery;
import cn.wolfcode.luowowo.comment.repository.StrategyCommentRepository;
import cn.wolfcode.luowowo.comment.service.IStrategyCommentService;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class StrategyCommentServiceImpl implements IStrategyCommentService {

    @Autowired
    private StrategyCommentRepository commentRepository;

    @Reference
    private IStrategyDetailService detailService;

    @Autowired
    private MongoTemplate template;

    @Override
    public void saveOrUpdate(StrategyComment comment) {
        if (comment.getId()==null){
            comment.setCreateTime(new Date());
        }
        commentRepository.save(comment);
    }

    @Override
    public Page queryForList(StrategyCommentQuery qo) {

        //查询条件
        Query query=new Query();
        //设置条件
        if (qo.getDetailId()!=null){
            query.addCriteria(Criteria.where("detailId").is(qo.getDetailId()));
        }

        //查询总条数
        long count = template.count(query, StrategyComment.class);
        if (count==0){
                return Page.empty();
        }

        //设置分页
        Pageable page = PageRequest.of(qo.getCurrentPage()-1, qo.getPageSize(), Sort.by(Sort.Direction.DESC, "createTime"));

        //查询对应的集合
        List<StrategyComment> comment = template.find(query, StrategyComment.class, "strategy_comment");

        //page的实现类
        PageImpl<StrategyComment> pages = new PageImpl<>(comment, page, count);
        return pages;
    }

    @Override
    public StrategyComment ThumbUp(String toId, Long userId) {
        //点赞数 thumbupnum;
        //评论点赞人的id thumbuplist = new ArrayList<>();
        // 1. 根据评论的id查询评论
        Optional<StrategyComment> strategyComment = commentRepository.findById(toId);
        StrategyComment comment = strategyComment.get();
        List<Long> thumbuplist = comment.getThumbuplist();
        //查询出list集合中是否有该用户的id
        if (!thumbuplist.contains(userId)){
            //如果不包含说明是进行点赞
            thumbuplist.add(userId);
            comment.setThumbupnum(comment.getThumbupnum()+1);
        }else {
            //包含了该用户的id,就表示是取消点赞
            thumbuplist.remove(userId);
            comment.setThumbupnum(comment.getThumbupnum()-1);
        }
        commentRepository.save(comment);
        return comment;
    }
}
