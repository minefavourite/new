package cn.wolfcode.luowowo.article.query;

import cn.wolfcode.luowowo.common.query.QueryObject;
import lombok.Getter;
import lombok.Setter;

/**
 * 游记查询条件封装对象
 */
@Setter
@Getter
public class TravelQuery extends QueryObject {

    private Long destId = -1L;

    private int state =1;  //状态

    private int isPublic=1;


    //旅游天数类型
    private int dayType = -1;
    //游记天数查询范围
    private TravelCondition days;


    //人均消费
    private int  perExpendType = -1;
    private TravelCondition perExpend;

    private int travelTimeType = -1;  //出发时间(月份)
    private TravelCondition travelTime;

    private int orderType = 1;  //排序类型
    public String getOrderBy(){

        if(orderType == 1){
            return " t.createTime desc ";
        }
        return " t.viewnum desc ";
    }

    public TravelCondition getDays(){
        return TravelCondition.TRAVEL_DAY.get(dayType);
    }


    public TravelCondition getPerExpend(){
        return TravelCondition.TRAVEL_EXPEND.get(perExpendType);
    }


    public TravelCondition getTravelTime(){
        return TravelCondition.TRAVEL_TIME.get(travelTimeType);
    }

}
