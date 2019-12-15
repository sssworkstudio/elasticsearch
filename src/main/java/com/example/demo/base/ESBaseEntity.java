package com.example.demo.base;


import java.io.Serializable;

/**
 * @Author: shenshanshan
 * @Date: 09:34 2019-12-10
 */
public class ESBaseEntity implements Serializable {

    private String id;

    public ESBaseEntity() {

    }

    public ESBaseEntity(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "ESBaseEntity{" +
                "id='" + id + '\'' +
                '}';
    }
}
