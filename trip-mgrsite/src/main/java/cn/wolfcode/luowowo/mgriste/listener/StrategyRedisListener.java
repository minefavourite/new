package cn.wolfcode.luowowo.mgriste.listener;

import cn.wolfcode.luowowo.article.domain.StrategyDetail;
import cn.wolfcode.luowowo.article.service.IStrategyDetailService;
import cn.wolfcode.luowowo.cache.service.IRecommendDetailRedisService;
import cn.wolfcode.luowowo.cache.service.IStrategyDetailRedisService;
import cn.wolfcode.luowowo.cache.vo.StrategyStatisVO;
import cn.wolfcode.luowowo.common.enums.ConstantEnum;
import com.alibaba.dubbo.config.annotation.Reference;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

@Component
public class StrategyRedisListener implements ApplicationListener<ContextRefreshedEvent> {

    @Reference
    private IStrategyDetailService detailService;

    @Reference
    private IStrategyDetailRedisService redisService;

    @Reference
    private IRecommendDetailRedisService commendRedisService;


    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        //首先去查询MySQL数据库,攻略明细
        List<StrategyDetail> list = detailService.list();
        //遍历,将数据添加到Redis中
        for (StrategyDetail detail : list) {

            //进行判断,如果已经存在就不在添加到Redis中
            if (redisService.queryStrategyRedisVo(detail.getId())){
                    continue;
            }
            StrategyStatisVO vo=new StrategyStatisVO();
            try {
                BeanUtils.copyProperties(vo,detail);
                vo.setDestId(detail.getDest().getId());
                vo.setStrategyId(detail.getId());
                vo.setDestName(detail.getDest().getName());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            redisService.saveStrategyToRedis(vo);
        }

        //从Redis中获取所有的vo对象key保存到zset中
        for (StrategyDetail detail : list) {
            String key = ConstantEnum.append(ConstantEnum.STRATEGY_STATIS_VO.getName(), detail.getId().toString());
            //去查询zset集合中是否已经有该key
            String keyZset = ConstantEnum.STRATEGY_STATIS_COMMEND_SORT.getName();
            if (commendRedisService.queryCommendByZset(keyZset,detail.getId())){
                continue;
            }
            //将vo的key存到zset中
            //将点赞数和收藏数想加
            int score = detail.getFavornum() + detail.getThumbsupnum();
            commendRedisService.addCommendNum(key,score);

        }

        for (StrategyDetail detail : list) {
            String key = ConstantEnum.append(ConstantEnum.STRATEGY_STATIS_VO.getName(), detail.getId().toString());
            //去查询zset集合中是否已经有该Hotkey
            String Hotkey = ConstantEnum.STRATEGY_STATIS_HOT_SORT.getName();
            if (commendRedisService.queryCommendByZset(Hotkey,detail.getId())){
                continue;
            }
            //将vo的key存到zset中
            //将回复数和阅读数想加
            int score = detail.getReplynum() + detail.getViewnum();
            commendRedisService.addHotNum(key,score);

        }
    }
}
