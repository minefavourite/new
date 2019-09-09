package cn.wolfcode.luowowo.search.service;

import cn.wolfcode.luowowo.search.query.SearchQueryObject;
import cn.wolfcode.luowowo.search.template.DestinationTemplate;
import cn.wolfcode.luowowo.search.template.UserInfoTemplate;

import java.util.List;

/**
 * 用户搜索
 */
public interface IUserInfoTemplateService {

    void save(UserInfoTemplate t);

    /**
     * 查询相关用户
     * @param qo
     * @return
     */
    List<UserInfoTemplate> findByDestName(SearchQueryObject qo);
}
