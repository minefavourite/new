package cn.wolfcode.luowowo.search.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * 统计数据包装对象
 */
@Setter
@Getter
public class StatisVO {

    private Long id;  //数据id
    private String name; //数据name
    private Long count;  //分组查询的个数
}
