package cn.lz.data.bootstrap;

import cn.hutool.extra.spring.SpringUtil;
import cn.lz.data.bootstrap.domain.TestUser;
import cn.lz.data.provider.service.l.EsDataService;
import co.elastic.clients.elasticsearch.core.DeleteByQueryResponse;
import com.alibaba.fastjson.JSON;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
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
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.MatchPhraseQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Author zhanglei
 * @Date 2023/12/26 16:37
 * @Desc
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("dev")
public class EsTest {

    @Autowired
    @Qualifier("restHighLevelClient")
    private RestHighLevelClient client;

    //测试索引创建
    @Test
    public void testCreateIndex() throws IOException {
        //创建索引
        CreateIndexRequest request = new CreateIndexRequest("es_data");
        //执行请求
        CreateIndexResponse createIndexResponse = client.indices().create(request, RequestOptions.DEFAULT);
        System.out.println(createIndexResponse);
    }

    //测试获取索引
    @Test
    public void testGetIndex() throws IOException {
        GetIndexRequest request = new GetIndexRequest("test_index");
        boolean exists = client.indices().exists(request, RequestOptions.DEFAULT);
        System.out.println(exists);
    }

    //测试获取条数
    @Test
    public void testGetCount() throws IOException {
        // 创建一个SearchRequest对象
        SearchRequest searchRequest = new SearchRequest("es_data");
        // 创建一个SearchSourceBuilder对象，用于构建查询请求
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        // 加上这条才会显示真实的
        searchSourceBuilder.trackTotalHits(true);
        searchSourceBuilder.size(0); // 设置返回的数据条数为0，只统计数据条数
        // 将SearchSourceBuilder添加到SearchRequest中
        searchRequest.source(searchSourceBuilder);
        // 执行查询请求
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        // 获取查询结果的总数
        long totalHits = searchResponse.getHits().getTotalHits().value;
        System.out.println(totalHits);
    }

    //测试删除索引
    @Test
    public void testDeleteIndex() throws IOException {
        DeleteIndexRequest request = new DeleteIndexRequest("test_index");
        AcknowledgedResponse acknowledgedResponse = client.indices().delete(request, RequestOptions.DEFAULT);
        System.out.println(acknowledgedResponse.isAcknowledged());
    }

    //测试添加文档
    @Test
    public void testAddDocument() throws IOException {
        //创建对象
        TestUser user1 = new TestUser("测试人员1", 23);
        //创建请求
        IndexRequest request = new IndexRequest("test_index");
        //设置规则
        request.id("1");
        request.timeout(TimeValue.timeValueSeconds(1));
        //放入数据
        request.source(JSON.toJSONString(user1), XContentType.JSON);
        //客户端发送请求，获取响应结果
        IndexResponse indexResponse = client.index(request, RequestOptions.DEFAULT);
        System.out.println(indexResponse.toString());
        System.out.println(indexResponse.status());
    }

    //测试判断文档
    @Test
    public void testIsExists() throws IOException {
        GetRequest getRequest = new GetRequest("test_index", "1");
        //不获取返回的_source的上下文
        getRequest.fetchSourceContext(new FetchSourceContext(false));
        getRequest.storedFields("_none_");
        boolean exists = client.exists(getRequest, RequestOptions.DEFAULT);
        System.out.println(exists);
    }

    //测试获取文档信息
    @Test
    public void testGetDocument() throws IOException {
        GetRequest getRequest = new GetRequest("test_index", "1");
        GetResponse getResponse = client.get(getRequest, RequestOptions.DEFAULT);
        System.out.println(getResponse.getSourceAsString());
        System.out.println(getResponse);
    }

    //测试更新文档信息
    @Test
    public void testUpdateDocument() throws IOException {
        UpdateRequest updateRequest = new UpdateRequest("test_index", "1");
        updateRequest.timeout("1s");
        TestUser user = new TestUser("测试人员1", 0);
        updateRequest.doc(JSON.toJSONString(user), XContentType.JSON);
        UpdateResponse response = client.update(updateRequest, RequestOptions.DEFAULT);
        System.out.println(response.status());
    }

    //删除文档记录
    @Test
    public void testDeleteRequest() throws IOException {
        DeleteRequest request = new DeleteRequest("es_data", "1");
        request.timeout("50s");
        DeleteResponse response = client.delete(request, RequestOptions.DEFAULT);
        System.out.println(response.status());
    }

    @Test
    public void testDeleteAllRequest() throws IOException {
        DeleteByQueryRequest request = new DeleteByQueryRequest("es_data");
        request.setQuery(QueryBuilders.matchAllQuery());
        BulkByScrollResponse bulkByScrollResponse = client.deleteByQuery(request, RequestOptions.DEFAULT);
        System.out.println("Deleted documents: " + bulkByScrollResponse.getReasonCancelled());
    }

    //批量插入数据
    @Test
    public void testBulkRequest() throws IOException {
        BulkRequest bulkRequest = new BulkRequest();
        bulkRequest.timeout("10s");

        List<TestUser> userList = new ArrayList<>();
        userList.add(new TestUser("测试人员1", 3));
        userList.add(new TestUser("测试人员2", 3));
        userList.add(new TestUser("测试人员3", 3));
        userList.add(new TestUser("测试人员4", 3));
        userList.add(new TestUser("测试人员5", 3));
        userList.add(new TestUser("测试人员6", 3));

        for (int i = 0; i < userList.size(); i++) {
            bulkRequest.add(
                    new IndexRequest("test_index")
                    .id(""+(i+1))
                    .source(JSON.toJSONString(userList.get(i)), XContentType.JSON));
        }
        BulkResponse bulkResponse = client.bulk(bulkRequest, RequestOptions.DEFAULT);
        //是否失败
        System.out.println(bulkResponse.hasFailures());
    }

    //查询
    @Test
    public void testSearch() throws IOException {
        SearchRequest searchRequest = new SearchRequest("test_index");
        //构建搜索条件
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        //查询条件，快速匹配
        MatchPhraseQueryBuilder matchPhraseQueryBuilder = QueryBuilders.matchPhraseQuery("name", "测试人员1");
        sourceBuilder.query(matchPhraseQueryBuilder);
        sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));
        searchRequest.source(sourceBuilder);
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        System.out.println(JSON.toJSONString(searchResponse.getHits()));
    }

    /**
     * 多线程导入MySQL数据-ES
     */
    @Test
    public void testImport() {
        EsDataService esDataService = SpringUtil.getBean(EsDataService.class);
        esDataService.importAll();
    }
}
