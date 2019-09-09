package cn.wolfcode.luowowo.comment.service.impl;

import cn.wolfcode.luowowo.comment.domain.TravelComment;
import cn.wolfcode.luowowo.comment.query.TravelCommentQuery;
import cn.wolfcode.luowowo.comment.repository.TravelCommentRepository;
import cn.wolfcode.luowowo.comment.service.ITravelCommentService;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class TravelCommentServiceImpl implements ITravelCommentService {

    @Autowired
    private TravelCommentRepository commentRepository;

    @Autowired
    private MongoTemplate template;


    @Override
    public Page queryForList(TravelCommentQuery qo) {

        //查询条件
        Query query=new Query();
        //设置条件
        if (qo.getTravelId()!=null){
            query.addCriteria(Criteria.where("travelId").is(qo.getTravelId()));
        }

        //查询总条数
        long count = template.count(query, TravelComment.class);
        if (count==0){
                return Page.empty();
        }

        //设置分页
        Pageable page = PageRequest.of(qo.getCurrentPage()-1, qo.getPageSize(), Sort.by(Sort.Direction.DESC, "createTime"));

        //查询对应的集合
        List<TravelComment> comment = template.find(query, TravelComment.class, "travel_comment");

        //page的实现类
        PageImpl<TravelComment> pages = new PageImpl<>(comment, page, count);
        return pages;
    }

    @Override
    public TravelComment saveOrUpdate(TravelComment comment,int floor) {

        if (!StringUtils.hasLength(comment.getId())){
            comment.setCreateTime(new Date());
        }
        //判断评论的类型
        if(comment.getType()==TravelComment.TRAVLE_COMMENT_TYPE_COMMENT){
            commentRepository.save(comment);
        }else {
            Optional<TravelComment> travelComment = commentRepository.findById(comment.getRefComment().getId());
            comment.setRefComment(travelComment.get());
            commentRepository.save(comment);
        }

        return comment;

    }

    @Override
    public Page query(TravelCommentQuery qc) {
        Query query=new Query();

        //查询总条数
        long count = template.count(query, TravelComment.class);
        if (count==0){
            return Page.empty();
        }
        //设置分页
        Pageable page = PageRequest.of(qc.getCurrentPage()-1, qc.getPageSize(), Sort.by(Sort.Direction.DESC, "createTime"));
        //查询对应的集合
        List<TravelComment> comment = template.find(query, TravelComment.class, "travel_comment");
        //page的实现类
        PageImpl<TravelComment> pages = new PageImpl<>(comment, page, count);
        return pages;

    }


}
