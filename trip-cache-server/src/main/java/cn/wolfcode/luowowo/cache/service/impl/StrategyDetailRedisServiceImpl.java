package cn.wolfcode.luowowo.cache.service.impl;

import cn.wolfcode.luowowo.article.domain.StrategyDetail;
import cn.wolfcode.luowowo.article.service.IStrategyDetailService;
import cn.wolfcode.luowowo.cache.service.IStrategyDetailRedisService;
import cn.wolfcode.luowowo.cache.vo.StrategyStatisVO;
import cn.wolfcode.luowowo.common.enums.ConstantEnum;
import cn.wolfcode.luowowo.common.utils.DateTime;
import cn.wolfcode.luowowo.common.utils.JsonResult;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.zookeeper.server.quorum.FastLeaderElection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
public class StrategyDetailRedisServiceImpl implements IStrategyDetailRedisService {

    //定义常量分别表示对不同计数增加
    private static final int STRATEGY_STATIS_VIEW=1;//表示阅读量
    private static final int STRATEGY_STATIS_REPLY=2;//表示评论量
    private static final int STRATEGY_STATIS_FAVOR=3;//表示收藏量
    private static final int STRATEGY_STATIS_THUMBUP=4;//表示点赞量

    @Autowired
    private StringRedisTemplate template;

    @Reference
    private IStrategyDetailService detailService;


    //抽出一个方法
    public void increaseNum(Long sid,int num,int type){
        //设计key值
        String key = ConstantEnum.append(ConstantEnum.STRATEGY_STATIS_VO.getName(), sid.toString());
        //查询该key是否存在
        StrategyStatisVO vo=null;
        if (template.hasKey(key)){
            //如果存在就将阅读数加1
            String voStr = template.opsForValue().get(key);
            vo= JSON.parseObject(voStr, StrategyStatisVO.class);
        }else {
            //如果不存在就创建一个vo对象
            vo=new StrategyStatisVO();
            //去MySQL数据库查找
            StrategyDetail detail = detailService.get(sid);
            try {
                BeanUtils.copyProperties(vo,detail);
                //设置一些两个对象中属性名不同的属性值
                vo.setStrategyId(detail.getId());
                vo.setDestId(detail.getDest().getId());
                vo.setDestName(detail.getDest().getName());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        switch (type){
            //将阅读数数加一
            case STRATEGY_STATIS_VIEW: vo.setViewnum(vo.getViewnum()+num);
                break;
                //评论数加1
            case STRATEGY_STATIS_REPLY:vo.setReplynum(vo.getReplynum()+num);
                break;
                //收藏数加1
            case STRATEGY_STATIS_FAVOR:vo.setFavornum(vo.getFavornum()+num);
                break;
            case STRATEGY_STATIS_THUMBUP:vo.setThumbsupnum(vo.getThumbsupnum()+num);
                break;
        }
        //将vo对象保存
        template.opsForValue().set(key,JSON.toJSONString(vo));
    }

    @Override
    public void increaseViewnum(Long id, int num) {
        this.increaseNum(id,num,StrategyDetailRedisServiceImpl.STRATEGY_STATIS_VIEW);
        //将阅读量统计数返回

    }

    @Override
    public void increaseReplyNum(Long detailId, int num) {
        this.increaseNum(detailId,num,StrategyDetailRedisServiceImpl.STRATEGY_STATIS_REPLY);

    }

    @Override
    public StrategyStatisVO getStrategyRedisVO(Long id) {
        String key = ConstantEnum.append(ConstantEnum.STRATEGY_STATIS_VO.getName(), id.toString());
        String voStr = template.opsForValue().get(key);
        StrategyStatisVO vo = JSON.parseObject(voStr, StrategyStatisVO.class);
        return vo;
    }

    @Override
    public boolean increaseFavor(Long sid, Long uid) {
        //设置一个集合,集合中保存用户id
        //为了判断该用户是否已经收藏了该攻略
        String keyVo = ConstantEnum.append(ConstantEnum.STRATEGY_STATIS_VO.getName(), sid.toString());
        StrategyStatisVO redisVO = this.getStrategyRedisVO(sid);
        String key = ConstantEnum.append(ConstantEnum.STRATEGY_STATIS_STRATEGYID.getName(), sid.toString());
        //判断是否有该集合
        List<String> list=new ArrayList<>();
        if (template.hasKey(key)){
            //如果有该集合
            String voStr = template.opsForValue().get(key);
            list = JSON.parseArray(voStr, String.class);
            if(list.contains(uid.toString())){
                //如果包含就表明是取消收藏操作,收藏量减一
                list.remove(uid.toString());
                template.opsForValue().set(key,JSON.toJSONString(list));
                redisVO.setFavornum(redisVO.getFavornum()-1);
                template.opsForValue().set(keyVo,JSON.toJSONString(redisVO));
                return false;
            }else {
                //收藏操作
                list.add(uid.toString());
            }
        }else {
            //如果没有该集合
            list.add(uid.toString());
        }
        redisVO.setFavornum(redisVO.getFavornum()+1);
        template.opsForValue().set(key,JSON.toJSONString(list));
        template.opsForValue().set(keyVo,JSON.toJSONString(redisVO));
        return true;
    }

    @Override
    public boolean thumbUp(Long sid, Long uid) {
        //设置一个key用来表示是否已经点赞过
        //设置存活时间
        String key = ConstantEnum.append(ConstantEnum.STRATEGY_STATIS_THUMBUP.getName(), sid.toString(), uid.toString());
        //查询vo对象
        String keyVo = ConstantEnum.append(ConstantEnum.STRATEGY_STATIS_VO.getName(), sid.toString());
        StrategyStatisVO redisVO = this.getStrategyRedisVO(sid);
        //判断key是否存在
        if (template.hasKey(key)){
            return false;
        }else {
            //没有点赞过
            Date date = new Date();
            Date endDate = DateTime.getEndDate(date);
            long time = DateTime.getDate(date, endDate);
            template.opsForValue().set(key,new Date().toString(),time,TimeUnit.SECONDS);
            redisVO.setThumbsupnum(redisVO.getThumbsupnum()+1);
            template.opsForValue().set(keyVo,JSON.toJSONString(redisVO));
        }
        return true;
    }

    @Override
    public boolean queryStrategyRedisVo(Long id) {
        String key = ConstantEnum.append(ConstantEnum.STRATEGY_STATIS_VO.getName(), id.toString());
        String voStr = template.opsForValue().get(key);
        if (voStr==null){
            return false;
        }
        return true;
    }


    @Override
    public void saveStrategyToRedis(StrategyStatisVO vo) {
        Long strategyId = vo.getStrategyId();
        String key = ConstantEnum.append(ConstantEnum.STRATEGY_STATIS_VO.getName(), strategyId.toString());
        template.opsForValue().set(key, JSON.toJSONString(vo));
    }

    @Override
    public List<StrategyStatisVO> queryStrategyFromRedisByPrefix(String name) {
        Set<String> keys = template.keys(name + "*");
        List<StrategyStatisVO> list =new ArrayList<>();
        //遍历key值
        for (String key : keys) {
            String voStr = template.opsForValue().get(key);
            StrategyStatisVO vo = JSON.parseObject(voStr, StrategyStatisVO.class);
            list.add(vo);
        }
        return list;
    }
}
