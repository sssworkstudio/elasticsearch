package com.example.demo.base.util;

/**
 * @Author: shenshanshan
 * @Date: 09:05 2019-12-10
 */
public class ESPage {

    /**当前页**/
    private int pageNum;

    /**每页记录数**/
    private int pageSize;

    /**排序 升序：true 降序：false**/
    private boolean isAsc;

    /**排序字段**/
    private String orderBy;

    public ESPage() {
    }

    public ESPage(int pageNum, int pageSize) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }

    public ESPage(int pageNum, int pageSize, boolean isAsc, String orderBy) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.isAsc = isAsc;
        this.orderBy = orderBy;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public boolean isAsc() {
        return isAsc;
    }

    public void setAsc(boolean asc) {
        isAsc = asc;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    @Override
    public String toString() {
        return "ESPage{" +
                "pageNum=" + pageNum +
                ", pageSize=" + pageSize +
                ", isAsc=" + isAsc +
                ", orderBy='" + orderBy + '\'' +
                '}';
    }
}
