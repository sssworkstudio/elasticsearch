package com.example.demo.base.common.search;

import org.elasticsearch.index.query.QueryBuilder;

/**
 * @Author: shenshanshan
 * @Date: 10:07 2019-12-16
 */
public class Query {
    /**
     * 索引名称，多个索引逗号分隔
     */
    private String index;

    /**
     * 查询器
     */
    private QueryBuilder queryBuilder;

    /**
     * 返回字段，多个字段逗号隔开
     */
    private String fields;

    /**
     * 记录数
     */
    private Integer size;

    /**
     * 排序字段
     */
    private String sortField;

    /**
     * 高亮字段，多个字段逗号分隔
     */
    private String highlightField;

    public Query() {

    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public QueryBuilder getQueryBuilder() {
        return queryBuilder;
    }

    public void setQueryBuilder(QueryBuilder queryBuilder) {
        this.queryBuilder = queryBuilder;
    }

    public String getFields() {
        return fields;
    }

    public void setFields(String fields) {
        this.fields = fields;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getSortField() {
        return sortField;
    }

    public void setSortField(String sortField) {
        this.sortField = sortField;
    }

    public String getHighlightField() {
        return highlightField;
    }

    public void setHighlightField(String highlightField) {
        this.highlightField = highlightField;
    }

    @Override
    public String toString() {
        return "Query{" +
                "index='" + index + '\'' +
                ", queryBuilder=" + queryBuilder +
                ", fields='" + fields + '\'' +
                ", size=" + size +
                ", sortField='" + sortField + '\'' +
                ", highlightField='" + highlightField + '\'' +
                '}';
    }
}
