package cn.wolfcode.luowowo.mgriste.job;

import cn.wolfcode.luowowo.article.service.IStrategyDetailService;
import cn.wolfcode.luowowo.article.vo.StrategyPersistenceStatisVO;
import cn.wolfcode.luowowo.cache.service.IStrategyDetailRedisService;
import cn.wolfcode.luowowo.cache.vo.StrategyStatisVO;
import cn.wolfcode.luowowo.common.enums.ConstantEnum;
import com.alibaba.dubbo.config.annotation.Reference;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/*@Component
public class PresistenceStrategyRedis {

    @Reference
    private IStrategyDetailRedisService redisService;

    @Reference
    private IStrategyDetailService detailService;

    @Scheduled(cron = "0/15 * * * * ?")
    public void presistence(){
        //去Redis中查询
        String name = ConstantEnum.STRATEGY_STATIS_VO.getName();
        List<StrategyStatisVO> list = redisService.queryStrategyFromRedisByPrefix(name);
        //遍历
        for (StrategyStatisVO vo : list) {

            StrategyPersistenceStatisVO pvo=new StrategyPersistenceStatisVO();
            try {
                BeanUtils.copyProperties(pvo,vo);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            detailService.insertStrategy(pvo);
        }
    }
}*/
