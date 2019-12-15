package com.example.demo.service;


import com.ebaiyihui.framework.page.PageResult;
import com.ebaiyihui.framework.response.BaseResponse;
import com.example.demo.pojo.entity.User;
import com.example.demo.pojo.vo.UserAddResVO;
import com.example.demo.pojo.vo.UserEditResVO;

import java.util.List;

/**
 * @Author: shenshanshan
 * @Date: 09:12 2019-12-10
 */
public interface UserService{

    BaseResponse<Object> isIndexExist(String index);

    BaseResponse<Object> createIndex(String index);

    BaseResponse<Object> createIndexCustom(String index);

    BaseResponse<Object> deleteIndex(String index);

    BaseResponse<Object> addUser(UserAddResVO vo);

    BaseResponse<Object> updateUser(UserEditResVO vo);

    BaseResponse<Object> deleteUser(String id);

    BaseResponse<List<User>> queryData(String index,String ids);

    BaseResponse<List<User>> search(String keyword);

    BaseResponse<PageResult<User>> searchPage(String keyword,Integer pageNum,Integer pageSize);

    BaseResponse<Object> addDataBatch(List<UserAddResVO> list);

    BaseResponse<Object> deleteDataBatch(String [] ids);

}