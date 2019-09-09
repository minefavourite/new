package cn.wolfcode.luowowo.cache.service.impl;

import cn.wolfcode.luowowo.article.domain.Travel;
import cn.wolfcode.luowowo.article.service.ITravelService;
import cn.wolfcode.luowowo.cache.service.ITravelDetailRedisService;
import cn.wolfcode.luowowo.cache.vo.TravelStatisVO;
import cn.wolfcode.luowowo.common.enums.ConstantEnum;
import cn.wolfcode.luowowo.common.utils.DateTime;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class TravelDetailRedisServiceImpl implements ITravelDetailRedisService {

    private static final int TRAVEL_STATIS_VIEW=1;//表示阅读量
    private static final int TRAVEL_STATIS_REPLY=2;//表示评论量

   @Autowired
   private StringRedisTemplate template;

   @Reference
   private ITravelService travelService;

   public void increaseNum(Long tid, int num,int type){
       //首先判断Redis中是否存在该vo对象
       String key = ConstantEnum.append(ConstantEnum.TRAVEL_STATIS_VO.getName(), tid.toString());
       TravelStatisVO vo=null;
       if (template.hasKey(key)){
           //如果有
           String vostr = template.opsForValue().get(key);
           vo = JSON.parseObject(vostr, TravelStatisVO.class);
       }else {
           //如果没有,创建
           vo=new TravelStatisVO();
           //去数据库查询
           Travel travel = travelService.getTravel(tid);
           try {
               BeanUtils.copyProperties(vo,travel);
               vo.setDestId(travel.getDest().getId());
               vo.setDestName(travel.getDest().getName());
               vo.setTravelId(travel.getId());
           } catch (IllegalAccessException e) {
               e.printStackTrace();
           } catch (InvocationTargetException e) {
               e.printStackTrace();
           }
       }
       switch (type){
           case TRAVEL_STATIS_VIEW:vo.setViewnum(vo.getViewnum()+num);
           break;
           case TRAVEL_STATIS_REPLY:vo.setReplynum(vo.getReplynum()+num);
           break;
       }
       template.opsForValue().set(key,JSON.toJSONString(vo));
   }

    @Override
    public void increaseViewnum(Long tid, int num) {
        this.increaseNum(tid,num,TRAVEL_STATIS_VIEW);

    }

    @Override
    public void increaseReplyNum(Long tid, int num) {
        this.increaseNum(tid,num,TRAVEL_STATIS_REPLY);
    }

    @Override
    public boolean increaseFavor(Long tid, Long uid) {
        String keyVo = ConstantEnum.append(ConstantEnum.TRAVEL_STATIS_VO.getName(), tid.toString());
        TravelStatisVO vo = this.getTravelRedisVO(tid);
        String keyTU = ConstantEnum.append(ConstantEnum.TRAVEL_STATIS_TRAVEL_USER.getName(),uid.toString());
        List<String> list = new ArrayList<>();
        //判断是否有keyTU 表示该游记是否被人收藏过
        if(template.hasKey(keyTU)){
            //如果有就判断登录的用户是否已经收藏
            String strTU = template.opsForValue().get(keyTU);
            list = JSON.parseArray(strTU, String.class);
            if (list.contains(tid.toString())){
                //如果有就表示取消收藏
                list.remove(tid.toString());
                template.opsForValue().set(keyTU,JSON.toJSONString(list));
                //收藏量减一
                vo.setFavornum(vo.getFavornum()-1);
                template.opsForValue().set(keyVo,JSON.toJSONString(vo));
                return false;
            }else {
                list.add(tid.toString());
                template.opsForValue().set(keyTU,JSON.toJSONString(list));
                //收藏量加一
                vo.setFavornum(vo.getFavornum()+1);
                template.opsForValue().set(keyVo,JSON.toJSONString(vo));
            }
        }else {
            list.add(tid.toString());
            template.opsForValue().set(keyTU,JSON.toJSONString(list));
            //收藏量加一
            vo.setFavornum(vo.getFavornum()+1);
            template.opsForValue().set(keyVo,JSON.toJSONString(vo));
        }
       return true;
    }

    @Override
    public boolean isUserHasTravel(Long uid,Long tid) {
        String keyTU = ConstantEnum.append(ConstantEnum.TRAVEL_STATIS_TRAVEL_USER.getName(),uid.toString());
        String voStr = template.opsForValue().get(keyTU);
        if (voStr==null){
            return false;
        }else {
            List<String> list = JSON.parseArray(voStr, String.class);
            if (list.contains(tid.toString())) {
                //如果包含就返回一个true
                return true;
            } else {
                return false;
            }
        }
    }

    @Override
    public boolean thumbUp(Long tid, Long uid) {
        String keyThumb = ConstantEnum.append(ConstantEnum.STRATEGY_STATIS_THUMBUP.getName(), tid.toString(), uid.toString());
        //判断是否已经点过赞
        String key = ConstantEnum.append(ConstantEnum.TRAVEL_STATIS_VO.getName(), tid.toString());
        TravelStatisVO statisVO = this.getTravelRedisVO(tid);
        Date now = new Date();
        long date = DateTime.getDate(now, DateTime.getEndDate(now));
        if (template.hasKey(keyThumb)){
            return false;
        }else {
            //没有点过赞
            //给表示用户点赞的key设置一个存货时间
            template.opsForValue().set(keyThumb,new Date().toString(),date, TimeUnit.SECONDS);
            statisVO.setThumbsupnum(statisVO.getThumbsupnum()+1);
            template.opsForValue().set(key,JSON.toJSONString(statisVO));
        }
        return true;
    }

    @Override
    public TravelStatisVO getTravelRedisVO(Long tid) {
        String key = ConstantEnum.append(ConstantEnum.TRAVEL_STATIS_VO.getName(), tid.toString());
        String vostr = template.opsForValue().get(key);
        TravelStatisVO statisVO = JSON.parseObject(vostr, TravelStatisVO.class);
        return statisVO;
    }
}
