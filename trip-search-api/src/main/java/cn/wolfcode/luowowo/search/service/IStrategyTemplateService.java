package cn.wolfcode.luowowo.search.service;

import cn.wolfcode.luowowo.search.query.SearchQueryObject;
import cn.wolfcode.luowowo.search.template.DestinationTemplate;
import cn.wolfcode.luowowo.search.template.StrategyTemplate;
import cn.wolfcode.luowowo.search.vo.StatisVO;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

/**
 * 攻略搜索
 */
public interface IStrategyTemplateService {

    /**
     * 保存数据到ES
     * @param t
     */
    void save(StrategyTemplate t);

    /**
     * 查询主题和对应的目的地
     * @return
     */
    List<Map<String,Object>> queryThemeAndDest();

    /**
     * 查询国内,国外,主题的攻略
     * @param num
     * @return
     */
    List<StatisVO> queryStrategyGroup(int num);

    /**
     * 查询每个分类下的攻略
     * @param qo
     * @return
     */
    Page<StrategyTemplate> queryStrategyByQo(SearchQueryObject qo);

    /**
     * 查询目的地下的相关攻略
     * @param qo
     * @return
     */
    List<StrategyTemplate> findByDestName(SearchQueryObject qo);
}
