package cn.wolfcode.luowowo.search.vo;

import cn.wolfcode.luowowo.common.query.QueryObject;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Setter
@Getter
public class StrategyQueryTemplate extends QueryObject {

    public static final int STRATEGY_CHINA_COMMMEND=1;
    public static final int STRATEGY_ABROAD_COMMMEND=0;
    public static final int STRATEGY_THEME_COMMMEND=2;

}
