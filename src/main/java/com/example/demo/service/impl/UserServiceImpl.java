package com.example.demo.service.impl;

import com.ebaiyihui.framework.page.PageResult;
import com.ebaiyihui.framework.response.BaseResponse;
import com.example.demo.base.ESBaseApi;
import com.example.demo.base.common.constants.CodeConstants;

import com.example.demo.base.common.search.Query;
import com.example.demo.base.common.search.QueryPage;
import com.example.demo.base.common.search.ESPage;
import com.example.demo.pojo.entity.User;
import com.example.demo.pojo.vo.UserAddResVO;
import com.example.demo.pojo.vo.UserEditResVO;
import com.example.demo.service.UserService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: shenshanshan
 * @Date: 09:51 2019-12-10
 */
@Service
public class UserServiceImpl implements UserService {


    @Resource
    private ESBaseApi esBaseService;


    @Override
    public BaseResponse<Object> isIndexExist(String index) {
        if (StringUtils.isEmpty(index)) {
            return BaseResponse.error("参数缺失");
        }
        return BaseResponse.success(esBaseService.isIndexExist(index));
    }

    @Override
    public BaseResponse<Object> createIndex(String index) {
        if (StringUtils.isEmpty(index)) {
            return BaseResponse.error("参数缺失");
        }
        return BaseResponse.success(esBaseService.createIndex(index));
    }

    @Override
    public BaseResponse<Object> createIndexCustom(String index) {
        if (StringUtils.isEmpty(index)) {
            return BaseResponse.error("参数缺失");
        }
        return BaseResponse.success(esBaseService.createIndexCustom(index, 2, 3));
    }

    @Override
    public BaseResponse<Object> deleteIndex(String index) {
        if (StringUtils.isEmpty(index)) {
            return BaseResponse.error("参数缺失");
        }
        return BaseResponse.success(esBaseService.deleteIndex(index));
    }

    @Override
    public BaseResponse<Object> addUser(UserAddResVO vo) {
        if (vo == null) {
            return BaseResponse.error("参数缺失");
        }
//        if (StringUtils.isEmpty(vo.getId())) {
//            vo.setId("BYH0000001");
//        }
        String result = esBaseService.addData("user", vo, UserAddResVO.class);
        if (CodeConstants.EXIST_CODE.equals(result)) {
            return BaseResponse.error("数据已存在");
        }
        return BaseResponse.success(result);
    }

    @Override
    public BaseResponse<Object> updateUser(UserEditResVO vo) {
        if (StringUtils.isEmpty(vo.getId())) {
            return BaseResponse.error("参数缺失");
        }
        boolean flag = esBaseService.updateDataById("user", vo, UserEditResVO.class);
        if (flag) {
            return BaseResponse.success();
        }
        return BaseResponse.error("系统异常");
    }

    @Override
    public BaseResponse<Object> deleteUser(String id) {
        if (StringUtils.isEmpty(id)) {
            return BaseResponse.error("参数缺失");
        }
        boolean flag = esBaseService.deleteDataById("user", id);
        if (flag) {
            return BaseResponse.success();
        }
        return BaseResponse.error("系统异常");
    }

    @Override
    public BaseResponse<List<User>> queryData(String index, String ids) {
        if (StringUtils.isEmpty(index) && StringUtils.isEmpty(ids)) {
            return BaseResponse.error("参数缺失");
        }
        return BaseResponse.success(esBaseService.queryByIds(index, ids, User.class));
    }

    @Override
    public BaseResponse<List<User>> search(String keyword) {
        if (StringUtils.isEmpty(keyword)) {
            return BaseResponse.error("参数缺失");
        }
        Query example = new Query();
        String fields = "address,remark,name";
        example.setIndex("user");
        example.setQueryBuilder(QueryBuilders.multiMatchQuery(keyword, fields.split(",")));
        example.setHighlightField(fields);
        example.setFields("address,remark,name");
        List<User> result = esBaseService.queryData(example,User.class);
        return BaseResponse.success(result);
    }

    @Override
    public BaseResponse<PageResult<User>> searchPage(String keyword, Integer pageNum, Integer pageSize) {
        if (StringUtils.isEmpty(keyword)) {
            return BaseResponse.error("参数缺失");
        }
        QueryPage example = new QueryPage();
        ESPage esPage=new ESPage(pageNum,pageSize,true,"age");
        String fields = "address,remark,name";
        example.setIndex("user");
        example.setQueryBuilder(QueryBuilders.multiMatchQuery(keyword, fields.split(",")));
        example.setHighlightField(fields);
        example.setFields("address,remark,name");
        PageResult<User> result = esBaseService.queryDataPage(esPage, example,User.class);
        return BaseResponse.success(result);
    }

    @Override
    public BaseResponse<Object> addDataBatch(List<UserAddResVO> list) {
        if (CollectionUtils.isEmpty(list)) {
            return BaseResponse.error("参数缺失");
        }
        esBaseService.addDataBatch("user", list, UserAddResVO.class);
        return BaseResponse.success();
    }

    @Override
    public BaseResponse<Object> deleteDataBatch(String [] ids) {
        esBaseService.deleteByIdBatch("user",ids);
        return BaseResponse.success();
    }
}
