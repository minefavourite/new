package cn.wolfcode.luowowo.article.service;

import cn.wolfcode.luowowo.article.domain.Destination;
import cn.wolfcode.luowowo.article.domain.StrategyDetail;
import cn.wolfcode.luowowo.article.query.QueryDestination;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface IDestinationService {

    PageInfo<Destination> queryForList(QueryDestination qd);

    /**
     * 查詢區域所有的目的地
     * @param deep
     * @return
     */
    List<Destination> selectDestByDeep(int deep);

    /**
     * 进行吐司操作
     * @param parentId
     * @return
     */
    List<Destination> getToasts(Long parentId);

    /**
     * 根据region出入过来的id查询
     * @param regionId
     * @return
     */
    List<Destination> selectDestByRegionId(Long regionId);

    /**
     * 根据查询出来的目的地的最终父区域id判断是否为国内
     * @param id
     * @return
     */
    boolean isAbroad(Long id);

    /**
     * 查询国外的攻略
     * @param id
     * @return
     */
    Destination getCountry(Long id);

    /**
     * 查询国内的攻略
     * @param id
     * @return
     */
    Destination getProvince(Long id);

    /**
     * 查询所有目的地
     * @return
     */
    List<Destination> list();
}
