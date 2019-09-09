package cn.wolfcode.luowowo.cache.service.impl;

import cn.wolfcode.luowowo.cache.service.IRecommendDetailRedisService;
import cn.wolfcode.luowowo.cache.vo.StrategyStatisVO;
import cn.wolfcode.luowowo.common.enums.ConstantEnum;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class RecommendDetailRedisServiceImpl implements IRecommendDetailRedisService {

    @Autowired
    private StringRedisTemplate template;

    @Override
    public void addCommendNum(String name, int num) {
        String keyZ = ConstantEnum.STRATEGY_STATIS_COMMEND_SORT.getName();
        this.addScoreNum(name,num,keyZ);
    }

    @Override
    public void addHotNum(String name, int num) {
        String hotKey = ConstantEnum.STRATEGY_STATIS_HOT_SORT.getName();
        this.addScoreNum(name,num,hotKey);
    }

    @Override
    public List<StrategyStatisVO> queryAllStrategyCommend() {
        String keyZ = ConstantEnum.STRATEGY_STATIS_COMMEND_SORT.getName();
        //先从Redis中查询
        List<StrategyStatisVO> list= this.queryRecommendDetail(keyZ);
        return list;
    }

    @Override
    public List<StrategyStatisVO> queryAllHotDetail() {
        String hotKey = ConstantEnum.STRATEGY_STATIS_HOT_SORT.getName();
        List<StrategyStatisVO> list = this.queryRecommendDetail(hotKey);
        return list;
    }

    @Override
    public boolean queryCommendByZset(String keyZset,Long sid) {
        String key = ConstantEnum.append(ConstantEnum.STRATEGY_STATIS_VO.getName(), sid.toString());
        Long rank = template.opsForZSet().rank(keyZset, key);
        return rank!=null;
    }

    //查询推荐统一方法
    public List<StrategyStatisVO> queryRecommendDetail(String keyZset){
        List<StrategyStatisVO> list=new ArrayList<>();
        Set<String> sets;
        if (keyZset==ConstantEnum.STRATEGY_STATIS_HOT_SORT.getName()){
            sets = template.opsForZSet().reverseRange(keyZset, 0, 9);
        }else {
            sets = template.opsForZSet().reverseRange(keyZset, 0, -1);
        }
        if(sets!=null) {
            for (String set : sets) {
                String vostr = template.opsForValue().get(set);
                StrategyStatisVO vo = JSON.parseObject(vostr, StrategyStatisVO.class);
                list.add(vo);
            }
        }
        return list;
    }

    //重构增加排行榜分数的方法
    public void addScoreNum(String name,int num,String key){

        template.opsForZSet().incrementScore(key,name,num);
    }
}
