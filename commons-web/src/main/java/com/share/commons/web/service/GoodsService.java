package com.share.commons.web.service;


import com.share.commons.data.datasource.annotation.DataSource;
import com.share.commons.web.model.Goods;

import java.util.List;

/**
 * Created by 程祥 on 15/12/21.
 * Function：
 */
public interface GoodsService {
    @DataSource("readDataSource")
    List<Goods> selectAll() throws Exception;

    Goods selectById(String id) throws Exception;

    int insertGoods(List<Goods> list) throws Exception;

    @DataSource("readDataSource")
    int updateGoods(String type) throws Exception;

    @DataSource("readDataSource")
    int updateGoods2() throws Exception;

    @DataSource("readDataSource")
    int updateGoods3() throws Exception;

    @DataSource("readDataSource")
    void insert(Goods goods) throws Exception;
}
