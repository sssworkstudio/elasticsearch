package com.example.demo.base;

import com.ebaiyihui.framework.page.PageResult;
import com.example.demo.base.example.SearchExample;
import com.example.demo.base.example.SearchPageExample;
import com.example.demo.base.util.ESPage;

import java.util.List;

/**
 * @Author: shenshanshan
 * @Date: 09:36 2019-12-10
 */
public interface ESBaseApi<T> {

    /**
     * 判断索引是否存在
     *
     * @param index 索引名称
     * @return
     */
    boolean isIndexExist(String index);

    /**
     * 创建索引
     * 备注：默认分片和副本
     *
     * @param index 索引名称
     * @return
     */
    boolean createIndex(String index);

    /**
     * 创建索引
     * 备注：自定义分区和副本
     *
     * @param index    索引名称
     * @param shards   分区数
     * @param replicas 副本数
     * @return
     */
    boolean createIndexCustom(String index, Integer shards, Integer replicas);

    /**
     * 删除索引
     *
     * @param index 索引名称，多个用逗号分隔
     * @return
     */
    boolean deleteIndex(String index);

    /**
     * 数据添加
     *
     * @param index 索引名称
     * @param t     实体类
     * @param clazz
     * @return
     */
    String addData(String index, T t, Class<T> clazz);

    /**
     * 通过ID修改数据
     *
     * @param index 索引名称
     * @param t     实体类
     * @param clazz
     * @return
     */
    boolean updateDataById(String index, T t, Class<T> clazz);

    /**
     * 通过ID删除数据
     *
     * @param index 索引名称
     * @param id    数据ID
     * @return
     */
    boolean deleteDataById(String index, String id);

    /**
     * 根据ID查询数据
     *
     * @param index 索引名称，多个索引用逗号分隔
     * @param ids   数据ID，多个ID用逗号分隔
     * @param clazz
     * @return
     */
    List<T> queryByIds(String index, String ids, Class<T> clazz);

    /**
     * 查询数据
     *
     * @param example 查询条件实体类
     * @param clazz
     * @return
     */
    List<T> queryData(SearchExample example, Class<T> clazz);

    /**
     * 分页查询数据
     *
     * @param esPage  es分页实体类
     * @param example 查询条件实体类
     * @param clazz
     * @return
     */
    PageResult<T> queryDataPage(ESPage esPage, SearchPageExample example, Class<T> clazz);

    /**
     * 批量添加
     *
     * @param index 索引名称
     * @param list  数据集合
     * @param clazz
     */
    void addDataBatch(String index, List<T> list, Class<T> clazz);

    /**
     * 通过ID批量删除
     *
     * @param index
     * @param ids
     */
    void deleteByIdBatch(String index, String[] ids);

}
