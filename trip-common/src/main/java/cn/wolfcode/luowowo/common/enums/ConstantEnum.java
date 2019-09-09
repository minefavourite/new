package cn.wolfcode.luowowo.common.enums;

import com.alibaba.fastjson.JSON;
import lombok.Getter;

import java.util.Date;

/**
 * 统一管理key集合
 * 构造器私有
 */
@Getter
public enum ConstantEnum  {

    VERIFY_CODE("verify_code",300L),
    USER_INFO_REDIS("user_info_redis",30*60L),

    //设计一个StrategyStatisVO统计数量前缀
    STRATEGY_STATIS_VO("strategy_statis_vo",-1l),

    //设置一个关于攻略收藏集合的前缀
    STRATEGY_STATIS_STRATEGYID("strategy_statis_strategyid",-1L),

    //设置一个产量,表示攻略点赞
    STRATEGY_STATIS_THUMBUP("strategy_statis_thumbup",null),

    //设计一个travelStatisVO统计数量前缀
    TRAVEL_STATIS_VO("travel_statis_vo",-1l),

    //设置一个关于游记点赞的前缀
    TRAVEL_STATIS_THUMBUP("travel_statis_thumbup",null),

    //设置一个关于游记收藏集合的前缀
    TRAVEL_STATIS_TRAVEL_USER("travel_statis_travel_user",-1L),

    //设置一个国内外的攻略排行榜的key
    STRATEGY_STATIS_COMMEND_SORT("strategy_statis_commend_sort",-1L),

    //设置一个国内外的攻略排行榜的key
    STRATEGY_STATIS_HOT_SORT("strategy_statis_hot_sort",-1L);



    private String name;
    private Long time;

    private ConstantEnum(String name,Long time){
        this.name=name;
        this.time=time;
    }


    //定义一个方法,进行key值的拼接
    public static String append( String prefix,String... values){
        StringBuilder builder=new StringBuilder();
        //拼接
          builder.append(prefix);
        for (String value : values) {
            builder.append(":").append(value);
        }
        return builder.toString();
    }

    public static void main(String[] args) {
        StringBuilder builder=new StringBuilder();
        builder.append("今天不错");
        String jsonString = JSON.toJSONString(builder);
        System.out.println(jsonString);
        String x = builder.toString();
        System.out.println(x);
    }
}
