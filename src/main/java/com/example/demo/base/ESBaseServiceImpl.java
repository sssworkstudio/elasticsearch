package com.example.demo.base;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ebaiyihui.framework.page.PageResult;
import com.example.demo.base.common.constants.CodeConstants;
import com.example.demo.base.common.constants.CommonConstants;

import com.example.demo.base.common.search.Query;
import com.example.demo.base.common.search.QueryPage;
import com.example.demo.base.util.BeanUtil;
import com.example.demo.base.common.search.ESPage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.get.GetIndexRequest;
import org.elasticsearch.action.bulk.*;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 *
 * @Author: shenshanshan
 * @Date: 9:23 2019-12-10
 */
@Service
@Slf4j
public class ESBaseServiceImpl<T> implements ESBaseApi<T> {

    @Resource
    private RestHighLevelClient client;

    private BulkProcessor bulkProcessor;

    @Override
    public boolean isIndexExist(String index) {
        boolean exists = false;
        if (StringUtils.isEmpty(index)) {
            return exists;
        }
        GetIndexRequest request = new GetIndexRequest();
        request.indices(index);
        try {
            exists = client.indices().exists(request, RequestOptions.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return exists;
    }


    @Override
    public boolean createIndex(String index) {
        if (!isIndexExist(index)) {
            CreateIndexRequest request = new CreateIndexRequest(index);
            try {
                CreateIndexResponse indexresponse = client.indices().create(request, RequestOptions.DEFAULT);
                return indexresponse.isAcknowledged();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }


    @Override
    public boolean createIndexCustom(String index, Integer shards, Integer replicas) {
        if (!isIndexExist(index)) {
            CreateIndexRequest request = new CreateIndexRequest(index);
            if (shards > 0 && replicas > 0) {
                request.settings(Settings.builder()
                        .put("index.number_of_shards", shards)
                        .put("index.number_of_replicas", replicas));

                try {
                    CreateIndexResponse response = client.indices().create(request, RequestOptions.DEFAULT);
                    return response.isAcknowledged();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }


    @Override
    public boolean deleteIndex(String index) {
        if (isIndexExist(index)) {
            DeleteIndexRequest request = new DeleteIndexRequest(index.split(","));
            try {
                AcknowledgedResponse response = client.indices().delete(request, RequestOptions.DEFAULT);
                return response.isAcknowledged();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }


    @Override
    public String addData(String index, T t, Class<T> clazz) {
        if (StringUtils.isNotEmpty(index) && t != null) {
            IndexRequest request = new IndexRequest(index);
            request.type(CommonConstants.TYPE);
            try {
                String json = JSON.toJSONString(t);
                //判断数据是否存在
                if (StringUtils.isNotEmpty(getId(t, clazz))) {
                    GetRequest getRequest = new GetRequest(index, CommonConstants.TYPE, getId(t, clazz));
                    GetResponse getResponse = client.get(getRequest, RequestOptions.DEFAULT);
                    if (getResponse.isExists()) {
                        return CodeConstants.EXIST_CODE;
                    }
                    request.id(getId(t, clazz));
                    //删除id属性
                    Map<String, Object> map = new HashMap<>();
                    map = BeanUtil.beanToMap(t, map);
                    map.remove(CommonConstants.ID);
                    json = JSON.toJSONString(map);
                }
                request.source(JSONObject.parseObject(json));
                IndexResponse response = client.index(request, RequestOptions.DEFAULT);
                return response.getId();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    @Override
    public boolean updateDataById(String index, T t, Class<T> clazz) {
        if (StringUtils.isNotEmpty(index) && t != null) {
            if (StringUtils.isEmpty(getId(t, clazz))) {
                return false;
            }
            UpdateRequest updateRequest = new UpdateRequest(index, CommonConstants.TYPE, getId(t, clazz));
            try {
                GetRequest getRequest = new GetRequest(index, CommonConstants.TYPE, getId(t, clazz));
                GetResponse getResponse = client.get(getRequest, RequestOptions.DEFAULT);
                if (!getResponse.isExists()) {
                    return false;
                }
                JSONObject obj = JSONObject.parseObject(JSON.toJSONString(t));
                Iterator<Map.Entry<String, Object>> it = obj.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<String, Object> temp = it.next();
                    if ((CommonConstants.ID).equals(temp.getKey())) {
                        it.remove();
                    }
                    if (temp.getValue().toString().length() < 1) {
                        it.remove();
                    }
                }
                updateRequest.doc(obj);
                UpdateResponse response = client.update(updateRequest, RequestOptions.DEFAULT);
                if ((DocWriteResponse.Result.NOOP).equals(response.getResult()) || (DocWriteResponse.Result.UPDATED).equals(response.getResult())) {
                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }


    @Override
    public boolean deleteDataById(String index, String id) {
        if (StringUtils.isNotEmpty(index) && StringUtils.isNotEmpty(id)) {
            try {
                DeleteResponse response = client.delete(new DeleteRequest(index, CommonConstants.TYPE, id), RequestOptions.DEFAULT);
                if (DocWriteResponse.Result.DELETED.equals(response.getResult())) {
                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }


    @Override
    public List<T> queryByIds(String index, String ids, Class<T> clazz) {
        List<T> sourceList = new ArrayList<>();
        if (StringUtils.isNotEmpty(index) && StringUtils.isNotEmpty(ids)) {
            SearchRequest request = new SearchRequest(index.split(","));
            request.types(CommonConstants.TYPE);
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.query(QueryBuilders.idsQuery().addIds(ids.split(",")));
            request.source(searchSourceBuilder);
            try {
                SearchResponse response = client.search(request, RequestOptions.DEFAULT);
                //判断响应是否成功
                if (response.status().getStatus() == CodeConstants.SUCCESS_STATUS_CODE) {
                    //获取结果集
                    for (SearchHit hit : response.getHits().getHits()) {
                        Map<String, Object> map = hit.getSourceAsMap();
                        map.put(CommonConstants.ID, hit.getId());
                        sourceList.add(BeanUtil.mapToBean(map, clazz));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sourceList;
    }


    @Override
    public List<T> queryData(Query example, Class<T> clazz) {
        if (example != null && StringUtils.isNotEmpty(example.getIndex())) {
            SearchRequest request = new SearchRequest(example.getIndex().split(","));
            request.types(CommonConstants.TYPE);
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            try {
                //返回字段
                if (StringUtils.isNotEmpty(example.getFields())) {
                    searchSourceBuilder.fetchSource(example.getFields().split(","), null);
                }

                //排序,text类型字段排序需开启【"fielddata": true】
                if (StringUtils.isNotEmpty(example.getSortField())) {
                    searchSourceBuilder.sort(SortBuilders.fieldSort(example.getSortField()).order(SortOrder.DESC));
                }

                //记录数
                if (example.getSize() != null && example.getSize() > 0) {
                    searchSourceBuilder.from(0).size(example.getSize());
                }

                //高亮显示
                if (StringUtils.isNotEmpty(example.getHighlightField())) {
                    HighlightBuilder highlightBuilder = new HighlightBuilder();
                    //高亮显示标签
                    highlightBuilder.preTags("<span style='color:red'>");
                    highlightBuilder.postTags("</span>");
                    //设置高亮字段
                    String[] fields = example.getHighlightField().split(",");
                    for (int i = 0; i < fields.length; i++) {
                        highlightBuilder.field(fields[i]);
                    }
                    searchSourceBuilder.highlighter(highlightBuilder);
                }

                //查询器
                if (null != example.getQueryBuilder()) {
                    searchSourceBuilder.query(example.getQueryBuilder());
                }

                log.info("查询语句：{}", searchSourceBuilder);
                request.source(searchSourceBuilder);
                SearchResponse response = client.search(request, RequestOptions.DEFAULT);
                log.info("查询结果：{}", response);

                if (response.status().getStatus() == CodeConstants.SUCCESS_STATUS_CODE) {
                    //高亮结果集处理
                    return setSearchResponse(response, example.getHighlightField(), clazz);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    @Override
    public PageResult<T> queryDataPage(ESPage esPage, QueryPage example, Class<T> clazz) {
        if (esPage != null && example != null) {
            int total=0;
            PageResult<T> result = new PageResult<>();
            SearchRequest request = new SearchRequest(example.getIndex().split(","));
            request.types(CommonConstants.TYPE);
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            try {
                //返回字段
                if (StringUtils.isNotEmpty(example.getFields())) {
                    searchSourceBuilder.fetchSource(example.getFields().split(","), null);
                }

                //排序,text类型字段排序需开启【"fielddata": true】
                if (StringUtils.isNotEmpty(esPage.getOrderBy())) {

                    if (esPage.isAsc()) {
                        //升序
                        searchSourceBuilder.sort(SortBuilders.fieldSort(esPage.getOrderBy()).order(SortOrder.ASC));
                    } else {
                        //降序
                        searchSourceBuilder.sort(SortBuilders.fieldSort(esPage.getOrderBy()).order(SortOrder.DESC));
                    }
                }

                //高亮显示
                if (StringUtils.isNotEmpty(example.getHighlightField())) {
                    HighlightBuilder highlightBuilder = new HighlightBuilder();
                    //高亮显示标签
                    highlightBuilder.preTags("<span style='color:red'>");
                    highlightBuilder.postTags("</span>");
                    //设置高亮字段
                    String[] fields = example.getHighlightField().split(",");
                    for (int i = 0; i < fields.length; i++) {
                        highlightBuilder.field(fields[i]);
                    }
                    searchSourceBuilder.highlighter(highlightBuilder);
                }

                //查询器
                if (null != example.getQueryBuilder()) {
                    searchSourceBuilder.query(example.getQueryBuilder());
                }

                //分页
                searchSourceBuilder.from((esPage.getPageNum()-1)*esPage.getPageSize()).size(esPage.getPageSize());

                log.info("查询语句：{}", searchSourceBuilder);
                request.source(searchSourceBuilder);
                SearchResponse response = client.search(request, RequestOptions.DEFAULT);
                log.info("查询结果：{}", response);

                if (response.status().getStatus() == CodeConstants.SUCCESS_STATUS_CODE) {
                    total=new Long(response.getHits().totalHits).intValue();
                    List<T> sourceList = setSearchResponse(response, example.getHighlightField(),clazz);
                    //总页数
                    result.setTotalPages((total  +  esPage.getPageSize()  - 1) / esPage.getPageSize());
                    //总记录数
                    result.setTotal(total);
                    result.setPageNum(esPage.getPageNum());
                    result.setPageSize(esPage.getPageSize());
                    result.setContent(sourceList);
                    return result;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    /**
     * 高亮结果集
     *
     * @param searchResponse
     * @param highlightField
     */
    private List<T> setSearchResponse(SearchResponse searchResponse, String highlightField, Class<T> clazz) {

        List<T> sourceList = new ArrayList<>();
        try {
            for (SearchHit searchHit : searchResponse.getHits().getHits()) {
                if (StringUtils.isNotEmpty(highlightField)) {

                    String[] highlightFields = highlightField.split(",");
                    for (int i = 0; i < highlightFields.length; i++) {

                        StringBuffer stringBuffer = new StringBuffer();
                        Map<String, Object> map = searchHit.getSourceAsMap();
                        map.put(CommonConstants.ID, searchHit.getId());
                        if (searchHit.getHighlightFields().get(highlightFields[i]) != null) {
                            Text[] text = searchHit.getHighlightFields().get(highlightFields[i]).getFragments();
                            if (text != null) {
                                for (Text str : text) {
                                    stringBuffer.append(str.string());
                                }
                                //遍历高亮结果集，覆盖正常结果集
                                map.put(highlightFields[i], stringBuffer.toString());
                            }
                        }
                    }
                }
                sourceList.add(BeanUtil.mapToBean(searchHit.getSourceAsMap(), clazz));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sourceList;
    }


    @Override
    public void addDataBatch(String index, List<T> list, Class<T> clazz) {
        if (StringUtils.isNotEmpty(index)&& CollectionUtils.isNotEmpty(list)) {
            list = JSON.parseArray(JSON.toJSONString(list), clazz);
            for (T t : list) {
                try {
                    IndexRequest indexRequest = new IndexRequest(index, CommonConstants.TYPE);
                    Map<String, Object> map = new HashMap<>();
                    map = BeanUtil.beanToMap(t, map);
                    if (StringUtils.isNotEmpty(getId(t, clazz))) {
                        indexRequest.id(getId(t, clazz));
                        //删除id属性
                        map.remove(CommonConstants.ID);
                    }
                    indexRequest.source(map, XContentType.JSON);
                    bulkProcessor.add(indexRequest);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void deleteByIdBatch(String index, String[] ids) {
        for (String id : ids) {
            DeleteRequest request = new DeleteRequest(index, CommonConstants.TYPE, id);
            bulkProcessor.add(request);
        }
    }


    /**
     * BulkProcessor初始化
     */
    @PostConstruct
    public void init() {
        BulkProcessor.Listener listener = new BulkProcessor.Listener() {
            @Override
            public void beforeBulk(long executionId, BulkRequest request) {
                //重写beforeBulk,在每次bulk request发出前执行,在这个方法里面可以知道在本次批量操作中有多少操作数
                int numberOfActions = request.numberOfActions();
                log.info("Executing bulk [{}] with {} requests", executionId, numberOfActions);
            }

            @Override
            public void afterBulk(long executionId, BulkRequest request, BulkResponse response) {
                //重写afterBulk方法，每次批量请求结束后执行，可以在这里知道是否有错误发生。
                if (response.hasFailures()) {
                    log.error("Bulk [{}] executed with failures,response = {}", executionId, response.buildFailureMessage());
                } else {
                    log.info("Bulk [{}] completed in {} milliseconds", executionId, response.getTook().getMillis());
                }
                BulkItemResponse[] responses = response.getItems();
            }

            @Override
            public void afterBulk(long executionId, BulkRequest request, Throwable failure) {
                //重写方法，如果发生错误就会调用。
                log.error("Failed to execute bulk", failure);
            }
        };

        //在这里调用build()方法构造bulkProcessor,在底层实际上是用了bulk的异步操作
        bulkProcessor = BulkProcessor.builder((request, bulkListener) -> client.bulkAsync(request, RequestOptions.DEFAULT, bulkListener), listener)
                // 1000条数据请求执行一次bulk
                .setBulkActions(1000)
                // 5mb的数据刷新一次bulk
                .setBulkSize(new ByteSizeValue(5L, ByteSizeUnit.MB))
                // 并发请求数量, 0不并发, 1并发允许执行
                .setConcurrentRequests(0)
                // 固定1s必须刷新一次
                .setFlushInterval(TimeValue.timeValueSeconds(1L))
                // 重试5次，间隔1s
                .setBackoffPolicy(BackoffPolicy.constantBackoff(TimeValue.timeValueSeconds(1L), 5))
                .build();
    }


    /**
     * 关闭BulkProcessor
     *
     * @return
     */
    public boolean destroy() {
        try {
            //在规定的时间内，是否所有批量操作全部完成
            return bulkProcessor.awaitClose(30L, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            log.error("Failed to close bulkProcessor", e);
        }
        log.info("bulkProcessor closed!");
        return false;
    }

    /**
     * 获取ID
     * 备注：如果有id属性返回属性值，没有返回null
     *
     * @param t
     * @return
     */
    public String getId(T t, Class<T> clazz) {
        try {
            Field[] fields = clazz.getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                if (fields[i].getName().equals(CommonConstants.ID)) {
                    fields[i].setAccessible(true);
                    if (fields[i].get(t)!=null){
                        return fields[i].get(t).toString();
                    }
                   return null;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
