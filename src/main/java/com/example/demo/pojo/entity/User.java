package com.example.demo.pojo.entity;

import com.example.demo.base.ESBaseEntity;

/**
 * @Author: shenshanshan
 * @Date: 09:45 2019-12-10
 */
public class User extends ESBaseEntity {

    private String name;

    private Integer age;

    private String address;

    private String remark;

    private String phone;

    public User() {

    }

    public User(String name, Integer age, String address, String remark, String phone) {
        this.name = name;
        this.age = age;
        this.address = address;
        this.remark = remark;
        this.phone = phone;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


}

