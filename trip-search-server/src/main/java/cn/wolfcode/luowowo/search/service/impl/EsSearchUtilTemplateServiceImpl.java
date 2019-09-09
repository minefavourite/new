package cn.wolfcode.luowowo.search.service.impl;

import cn.wolfcode.luowowo.search.query.SearchQueryObject;
import cn.wolfcode.luowowo.search.service.EsSearchUtilTemplateService;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import org.apache.commons.beanutils.BeanUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EsSearchUtilTemplateServiceImpl implements EsSearchUtilTemplateService {

    @Autowired
    private ElasticsearchTemplate template;

    @Override
    public <T> Page<T> queryWithHighLight(String indexName, String typeName, Class<T> clz, SearchQueryObject qo,String...fields) {
        String preTags = "<span style='color:red;'>";
        String postTags = "</span>";

        //分装有高亮的关键字的字段
        HighlightBuilder.Field[] fs = new HighlightBuilder.Field[fields.length];
        for(int i = 0; i < fs.length; i++){
            fs[i] = new HighlightBuilder.Field(fields[i])
                    .preTags(preTags)
                    .postTags(postTags);
        }

        //先封装条件字段
        NativeSearchQueryBuilder searchQuery=new NativeSearchQueryBuilder();
        searchQuery.withIndices(indexName);
        searchQuery.withTypes(typeName);
        searchQuery.withPageable(qo.getNoSort());
        searchQuery.withQuery(QueryBuilders.multiMatchQuery(qo.getKeyword(),fields));
        //设置高亮字段
        searchQuery.withHighlightFields(fs);


      return   template.queryForPage(searchQuery.build(), clz, new SearchResultMapper() {
            @Override
            public <T> AggregatedPage<T> mapResults(SearchResponse response, Class<T> clazz, Pageable pageable) {
                List<T> list=new ArrayList<>();

                //获取整个hits
                SearchHits hits = response.getHits();
                //获取hits中每一个hits
                SearchHit[] searchHits = hits.getHits();
                for (SearchHit hit : searchHits) {
                    //获取hit中的sources
                    String sources = hit.getSourceAsString();
                    T t = JSON.parseObject(sources, clazz);

                    //替换高亮字段,获取这个hit中的所有高亮字段
                    highlightFieldsCopy(t,hit.getHighlightFields(),fields);
                    list.add(t);
                }
                //将结果集封装成分页对象Page : 参数1:查询数据, 参数2:分页数据, 参数3:查询到总记录数
                AggregatedPage<T> result = new AggregatedPageImpl<T>((List<T>) list, pageable,
                        response.getHits().getTotalHits());
                return result;
            }
        });
    }


    //进行高亮替换
    private <T> void highlightFieldsCopy(T t, Map<String, HighlightField> map, String ...fields){

        Map<String, String> mm = new HashMap<>();
        //遍历需要高亮的字段数组
        for (String field : fields) {
            //判断该map中是否有字段
            HighlightField hf = map.get(field);
            if (hf != null) {
                //获取该字段中的文本内容数组
                Text[] fragments = hf.fragments();
                String str = "";
                for (Text text : fragments) {
                    str += text;
                }
                mm.put(field, str);
            }
        }
        try {
            //替换文本中的每一个关键字
            BeanUtils.copyProperties(t, mm);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

}
