package cn.wolfcode.luowowo.search.service.impl;

import cn.wolfcode.luowowo.search.vo.StrategyQueryTemplate;
import cn.wolfcode.luowowo.search.query.SearchQueryObject;
import cn.wolfcode.luowowo.search.repository.StrategyRepository;
import cn.wolfcode.luowowo.search.service.IStrategyTemplateService;
import cn.wolfcode.luowowo.search.template.StrategyTemplate;
import cn.wolfcode.luowowo.search.vo.StatisVO;
import com.alibaba.dubbo.config.annotation.Service;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.composite.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;

import java.util.*;

@Service
public class StrategyTemplateServiceImpl implements IStrategyTemplateService {

    @Autowired
    private StrategyRepository strategyRepository;

    @Autowired
    private ElasticsearchTemplate template;

    //转换端口中心
    @Autowired
    private TransportClient client;

    @Override
    public void save(StrategyTemplate t) {
        strategyRepository.save(t);
    }

    @Override
    public List<Map<String, Object>> queryThemeAndDest() {
        List<Map<String,Object>> datas=new ArrayList<>();
        List<StatisVO> themes = this.queryAllStrategy("themeId", "themeName");
        List<StatisVO> subList;
        //截取前十个主题
        if(themes.size() > 10){
            subList = themes.subList(0, 10);
        }else {
            subList=themes.subList(0,themes.size());
        }
        //查询主题下相关的目的地
        //遍历theme集合
        for (StatisVO theme : subList) {
            Map<String,Object> map=new HashMap<>();
            List<StatisVO> destVo = this.queryDestByThemeId(theme.getId(),"themeId");
            //将主题和对应的目的地存到map集合中
            map.put("theme",theme);
            map.put("dests",destVo);
            datas.add(map);
        }
        return datas;
    }

    @Override
    public List<StatisVO> queryStrategyGroup(int num) {
        String strategyName="";
        String strategyId="";
        if (num== StrategyQueryTemplate.STRATEGY_ABROAD_COMMMEND){
            strategyName="countryName";
            strategyId="countryId";
        }else if(num==StrategyQueryTemplate.STRATEGY_CHINA_COMMMEND) {
            strategyName="provinceName";
            strategyId="provinceId";
        }else {
            strategyName="themeName";
            strategyId="themeId";
        }
        List<StatisVO> themes = this.queryAllStrategy(strategyId, strategyName);

        return themes;
    }

    @Override
    public Page<StrategyTemplate> queryStrategyByQo(SearchQueryObject qo) {


        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        //查询国内攻略下内容
        if (qo.getType()==StrategyQueryTemplate.STRATEGY_CHINA_COMMMEND){
            boolQuery.must(QueryBuilders.termQuery("provinceId",qo.getTypeValue()));
            //查询国外攻略下内容
        }else if (qo.getType()==StrategyQueryTemplate.STRATEGY_ABROAD_COMMMEND){
            boolQuery.must(QueryBuilders.termQuery("countryId",qo.getTypeValue()));
            //查询主题攻略下内容
        }else if (qo.getType()==StrategyQueryTemplate.STRATEGY_THEME_COMMMEND){
            boolQuery.must(QueryBuilders.termQuery("themeId",qo.getTypeValue()));
        }
        Page<StrategyTemplate> search = strategyRepository.search(boolQuery, qo.getPageable());


        return search;
    }

    @Override
    public List<StrategyTemplate> findByDestName(SearchQueryObject qo) {
        List<StrategyTemplate> strategyTemplates = strategyRepository.findByDestName(qo.getKeyword());
        return strategyTemplates;
    }

    //根据主题id查询目的地
    private List<StatisVO> queryDestByThemeId(Long themeId,String strategyName ){
        List<StatisVO> dest=new ArrayList<>();
        Iterable<StrategyTemplate> dests = strategyRepository.search(QueryBuilders.termQuery(strategyName, themeId));
        //实现去重,将相同的目的地去除
        List<String> destList= new ArrayList<>();

        //遍历dests
        dests.forEach(dts ->{
            if (!destList.contains(dts.getDestName())) {
                StatisVO vo = new StatisVO();
                vo.setId(dts.getDestId());
                vo.setName(dts.getDestName());
                dest.add(vo);
                destList.add(dts.getDestName());
            }
        });
        return dest;
    }

    //根据国内,国外,主题的需求查询一个StatisVO的list集合
    private List<StatisVO> queryAllStrategy(String strategyId,String strategyName){

        List<CompositeValuesSourceBuilder<?>> sources =new ArrayList<>();

        //创建一个sources中的一个列
        TermsValuesSourceBuilder idSource=new TermsValuesSourceBuilder("id");
        idSource.field(strategyId);
        sources.add(idSource);
        //创建sources中的第二个列
        TermsValuesSourceBuilder nameSource=new TermsValuesSourceBuilder("name");
        nameSource.field(strategyName);
        sources.add(nameSource);

        //多列分组条件
        //一个参数是自定义分组名:themeGroup 一个包含CompositeValuesSourceBuilder类的集合
        CompositeAggregationBuilder themeGroup = new CompositeAggregationBuilder("themeGroup", sources);

        //设置查询方式
        NativeSearchQueryBuilder builder=new NativeSearchQueryBuilder();
        //查询条件
        builder.addAggregation(themeGroup);
        //查询的索引
        builder.withIndices(StrategyTemplate.INDEX_NAME);
        builder.withTypes(StrategyTemplate.TYPE_NAME);
        builder.withPageable(PageRequest.of(0,1));

        //查询
        AggregatedPage<StrategyTemplate> page = (AggregatedPage<StrategyTemplate>) strategyRepository.search(builder.build());
        Aggregations aggregations = page.getAggregations();
        InternalComposite group = aggregations.get("themeGroup");
        List<? extends CompositeAggregation.Bucket> buckets = group.getBuckets();
        //设计一个list存放所有的主题
        List<StatisVO> themes=new ArrayList<>();
        for (CompositeAggregation.Bucket bucket : buckets) {
            StatisVO vo=new StatisVO();
            //获取一个桶里的map
            Map<String, Object> key = bucket.getKey();
            //获取id
            vo.setId(Long.valueOf(key.get("id").toString()));
            vo.setName(key.get("name").toString());
            vo.setCount(bucket.getDocCount());
            themes.add(vo);


        }
        //lomda表达式
        /*buckets.forEach(bucket ->{
            StatisVO vo=new StatisVO();
            //获取一个桶里的map
            Map<String, Object> key = bucket.getKey();
            //获取id
            vo.setId(Long.valueOf(key.get("id").toString()));
            vo.setName(key.get("name").toString());
            vo.setCount(bucket.getDocCount());
            themes.add(vo);
        });*/
        Collections.sort(themes, new Comparator<StatisVO>() {
            @Override
            public int compare(StatisVO o1, StatisVO o2) {

                return Integer.valueOf(o2.getCount()-o1.getCount()+"");
            }
        });

       /* Collections.sort(themes,(StatisVO s1, StatisVO s2)->
       Integer.valueOf(s2.getCount()-s1.getCount()+"")
        );*/
        return themes;
    }
}
