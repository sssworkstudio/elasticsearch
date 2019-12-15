package com.example.demo.base.example;

import org.elasticsearch.index.query.QueryBuilder;

/**
 * @Author: shenshanshan
 * @Date: 08:56 2019-12-10
 */
public class SearchPageExample {

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
     * 高亮字段，多个字段逗号分隔
     */
    private String highlightField;

    public SearchPageExample() {
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

    public String getHighlightField() {
        return highlightField;
    }

    public void setHighlightField(String highlightField) {
        this.highlightField = highlightField;
    }

    @Override
    public String toString() {
        return "SearchPageExample{" +
                "index='" + index + '\'' +
                ", queryBuilder=" + queryBuilder +
                ", fields='" + fields + '\'' +
                ", highlightField='" + highlightField + '\'' +
                '}';
    }
}
