package cn.lz.data.provider.service.l;

import cn.hutool.core.util.StrUtil;
import cn.lz.data.provider.dao.EsDataDao;
import cn.lz.data.provider.domain.pojo.EsData;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;

/**
 * @Desc
 * @Author zhanglei
 * @Date 2024/1/2 17:13
 */
@Slf4j
@Service
public class EsDataService extends ServiceImpl<EsDataDao, EsData> {

    private static final int PAGE_SIZE = 2000;

    private static final String ES_INDEX = "es_data";

    @Autowired
    @Qualifier("restHighLevelClient")
    private RestHighLevelClient client;

    @Autowired
    private ExecutorService executorService;

    public void insert(List<EsData> esDataList) {
        this.saveBatch(esDataList);
    }

    /**
     * 批量导入
     */
    @SneakyThrows
    public void importAll() {
        //总条数
        long count = this.count();
        //总页数
        long totalPageSize = (count % PAGE_SIZE) == 0 ? count / PAGE_SIZE : (count / PAGE_SIZE) + 1;
        //开始执行时间
        long startTime = System.currentTimeMillis();
        //有多少页就创建多少个CountDownLatch计数
        CountDownLatch countDownLatch = new CountDownLatch((int) totalPageSize);

        int fromIndex;
        List<EsData> esDataList = null;

        for (long i = 0; i < totalPageSize; i++) {
            //查询数据
            IPage<EsData> page = new Page<>();
            page.setCurrent(i);
            page.setSize(PAGE_SIZE);
            //查询文章
            esDataList = this.list(page);
            //创建线程
            TaskThread taskThread = new TaskThread(esDataList, countDownLatch);
            //执行线程
            executorService.execute(taskThread);
        }

        //调用await()方法，用来等待计数归零
        countDownLatch.await();

        long endTime = System.currentTimeMillis();
        log.info("es索引数据批量导入共:{}条，共消耗时间:{}秒", count, (endTime - startTime) / 1000);
    }

    class TaskThread implements Runnable {

        List<EsData> esDataList;
        CountDownLatch cdl;

        public TaskThread(List<EsData> esDataList, CountDownLatch cdl) {
            this.esDataList = esDataList;
            this.cdl = cdl;
        }

        @SneakyThrows
        @Override
        public void run() {
            //批量导入
            BulkRequest bulkRequest = new BulkRequest(ES_INDEX);

            for (EsData esData : esDataList) {
                bulkRequest.add(new IndexRequest().id(esData.getId())
                        .source(JSON.toJSONString(esData), XContentType.JSON));
            }
            //发送请求
            client.bulk(bulkRequest, RequestOptions.DEFAULT);
            //让计数-1
            cdl.countDown();
        }
    }

    public void search(String keyword) throws IOException {
        SearchRequest request = new SearchRequest(ES_INDEX);
        //设置查询条件
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        //第一个条件
        if(StrUtil.isBlank(keyword)) {
            request.source().query(QueryBuilders.matchAllQuery());
        }else {
            request.source().query(QueryBuilders.queryStringQuery(keyword).field("value").defaultOperator(Operator.OR));
        }
        //分页
        request.source().from(0);
        request.source().size(20);
        //按照时间倒叙排序
        request.source().sort("publishTime", SortOrder.DESC);
        //搜索
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        //解析结果
        SearchHits searchHits = response.getHits();
        //获取具体文档数据

    }
}
