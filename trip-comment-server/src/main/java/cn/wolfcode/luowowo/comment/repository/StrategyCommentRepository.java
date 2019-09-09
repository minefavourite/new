package cn.wolfcode.luowowo.comment.repository;

import cn.wolfcode.luowowo.comment.domain.StrategyComment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StrategyCommentRepository extends MongoRepository<StrategyComment,String> {

}
