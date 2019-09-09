package cn.wolfcode.luowowo.article.query;

import cn.wolfcode.luowowo.common.query.QueryObject;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class QueryDestination extends QueryObject {

    private Long parentId=-1L;
    private int isHot=-1;
}
