package cn.wolfcode.luowowo.comment.repository;

import cn.wolfcode.luowowo.comment.domain.TravelComment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TravelCommentRepository extends MongoRepository<TravelComment,String> {

}
