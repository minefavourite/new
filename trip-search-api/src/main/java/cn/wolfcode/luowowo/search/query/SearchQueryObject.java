package cn.wolfcode.luowowo.search.query;

import cn.wolfcode.luowowo.common.query.QueryObject;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Setter
@Getter
public class SearchQueryObject extends QueryObject {
    public static final int STRATEGY_CHINA_COMMMEND=1; //国外
    public static final int STRATEGY_ABROAD_COMMMEND=0; //国内
    public static final int STRATEGY_THEME_COMMMEND=2; //主题

    public  static final int SEARCH_CONTENT_ALL=-1; //全部
    public  static final int SEARCH_CONTENT_DESTS=0;  //目的地
    public  static final int SEARCH_CONTENT_STRATEGY=1;//攻略
    public  static final int SEARCH_CONTENT_TRAVEL=2; //游记
    public  static final int SEARCH_CONTENT_USER=3; //用户

    //攻略的类别 : 国内 国外 主题
    private int type=-1;

    //每个细节分类
    private Long typeValue=-1L;

    //排序的方式
    private String orderBy="viewnum";

    private Long destId = -1L;   //目的地id

    //设计一个分页,根据条件排序
    public Pageable getPageable(){
        return PageRequest.of(super.getCurrentPage()-1,super.getPageSize(), Sort.by(Sort.Direction.DESC,orderBy));
    }

    //不设计排序
    public Pageable getNoSort(){
        return PageRequest.of(super.getCurrentPage()-1,super.getPageSize());
    }
}
