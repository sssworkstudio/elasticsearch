package com.example.demo.controller;

import com.ebaiyihui.framework.page.PageResult;
import com.ebaiyihui.framework.response.BaseResponse;
import com.example.demo.pojo.entity.User;
import com.example.demo.pojo.vo.UserAddResVO;
import com.example.demo.pojo.vo.UserEditResVO;
import com.example.demo.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;


/**
 * @Author: shenshanshan
 * @Date: 09:02 2019-12-10
 */
@RestController
@RequestMapping("/api")
public class UserController {

    @Resource
    private UserService userService;


    @PostMapping("/index/exist")
    public BaseResponse<Object> exist(@RequestParam String index) {
        return userService.isIndexExist(index);
    }

    @PostMapping("/index/create")
    public BaseResponse<Object> createIndex(@RequestParam String index) {
        return userService.createIndex(index);
    }

    @PostMapping("/index/createcustom")
    public BaseResponse<Object> createIndexCustom(@RequestParam String index) {
        return userService.createIndexCustom(index);
    }

    @PostMapping("/index/delete")
    public BaseResponse<Object> deleteIndex(@RequestParam String index) {
        return userService.deleteIndex(index);
    }

    @PostMapping("/data/add")
    public BaseResponse<Object> addData(@RequestBody UserAddResVO vo) {
        return userService.addUser(vo);
    }

    @PostMapping("/data/update")
    public BaseResponse<Object> updateData(@RequestBody UserEditResVO vo) {
        return userService.updateUser(vo);
    }

    @PostMapping("/data/delete")
    public BaseResponse<Object> deleteData(@RequestParam String id) {
        return userService.deleteUser(id);
    }

    @PostMapping("/data/query")
    public BaseResponse<List<User>> queryData(@RequestParam String index, String ids) {
        return userService.queryData(index,ids);
    }

    @PostMapping("/data/search")
    public BaseResponse<List<User>> search(@RequestParam String keyword) {
        return userService.search(keyword);
    }

    @PostMapping("/data/pagesearch")
    public BaseResponse<PageResult<User>> searchPage(@RequestParam String keyword, Integer pageNum, Integer pageSize) {
        return userService.searchPage(keyword,pageNum,pageSize);
    }

    @PostMapping("/data/addbatch")
    public BaseResponse<Object> addBatch(@RequestBody List<UserAddResVO> list) {
        return userService.addDataBatch(list);
    }

    @PostMapping("/data/deletebatch")
    public BaseResponse<Object> deleteBatch(@RequestBody String [] ids) {
        return userService.deleteDataBatch(ids);
    }

}
