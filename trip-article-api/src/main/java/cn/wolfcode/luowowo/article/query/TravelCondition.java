package cn.wolfcode.luowowo.article.query;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**
 * 游记的范围条件查询
 * 将两个范围数值封装到一个对象中
 * 并将该对象以key-value的形式保存到map集合中
 */
@Setter
@Getter
public class TravelCondition {
    //旅游时间的范围集合
    public static final Map<Integer,TravelCondition> TRAVEL_TIME=new HashMap<>();
    public static final Map<Integer,TravelCondition> TRAVEL_EXPEND=new HashMap<>();
    public static final Map<Integer,TravelCondition> TRAVEL_DAY=new HashMap<>();

    private int min;
    private int max;

    public TravelCondition(int min,int max){
        this.min=min;
        this.max=max;
    }
    //自定义规范
    static {
        //旅行天数
        TRAVEL_DAY.put(-1,new TravelCondition(0,Integer.MAX_VALUE));//不限天数
        TRAVEL_DAY.put(1,new TravelCondition(1,3));//1-3天
        TRAVEL_DAY.put(2,new TravelCondition(4,7));//4-7天
        TRAVEL_DAY.put(3,new TravelCondition(7,14));//7-14天
        TRAVEL_DAY.put(4,new TravelCondition(15,Integer.MAX_VALUE));//15天以上

        //旅行费用
        TRAVEL_EXPEND.put(-1,new TravelCondition(0,Integer.MAX_VALUE));//不限费用
        TRAVEL_EXPEND.put(1,new TravelCondition(1,999));//1000元以内
        TRAVEL_EXPEND.put(2,new TravelCondition(1000,6000));//1K-6K
        TRAVEL_EXPEND.put(3,new TravelCondition(6000,20000));//6K-20K
        TRAVEL_EXPEND.put(4,new TravelCondition(20000,Integer.MAX_VALUE));//20K以上

        //旅行时间
        TRAVEL_TIME.put(-1,new TravelCondition(1,12));//不限时间
        TRAVEL_TIME.put(1,new TravelCondition(1,2));//1月-2月
        TRAVEL_TIME.put(2,new TravelCondition(3,4));//3月-4月
        TRAVEL_TIME.put(3,new TravelCondition(5,6));//5月-6月
        TRAVEL_TIME.put(4,new TravelCondition(7,8));//7月-8月
        TRAVEL_TIME.put(5,new TravelCondition(9,10));//9月-10月
        TRAVEL_TIME.put(6,new TravelCondition(11,12));//11月-12月
    }
}
