package testComment;

import cn.wolfcode.luowowo.comment.query.TravelCommentQuery;
import cn.wolfcode.luowowo.comment.service.impl.TravelCommentServiceImpl;
import org.springframework.data.domain.Page;

public class testMongodb {




    public static void main(String[] args) {
        TravelCommentServiceImpl service=new TravelCommentServiceImpl();
        TravelCommentQuery qo=new TravelCommentQuery();
        qo.setTravelId(5L);
        qo.setCurrentPage(1);
        qo.setPageSize(10);
        Page page = service.queryForList(qo);
        System.out.println(page);


    }
}
