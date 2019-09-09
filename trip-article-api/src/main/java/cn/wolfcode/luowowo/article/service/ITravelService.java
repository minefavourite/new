package cn.wolfcode.luowowo.article.service;

import cn.wolfcode.luowowo.article.domain.Travel;
import cn.wolfcode.luowowo.article.domain.TravelContent;
import cn.wolfcode.luowowo.article.query.TravelQuery;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface ITravelService {
    /**
     * 分页查询
     * @param qo
     * @return
     */
    PageInfo<Travel> queryForList(TravelQuery qo);

    /**
     * 查询单篇的游记明细
     * @param id
     * @return
     */
    Travel getTravel(Long id);

    /**
     * 查询前三篇的游记
     * @param id
     * @return
     */
    List<Travel> queryTravelByDestIdTop3(Long id);

    /**
     * 保存游记
     * @param travel
     * @return
     */
    Long insert(Travel travel);

    /**
     * 修改游记的状态
     * @param id
     * @param state
     */
    void updateState(Long id, Integer state);

    /**
     * 查询文章内容
     * @param id
     * @return
     */
    TravelContent queryContent(Long id);

    List<Travel> list();
}
