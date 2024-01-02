package cn.lz.data.bootstrap;

import cn.hutool.core.lang.UUID;
import cn.lz.data.provider.domain.pojo.EsData;
import cn.lz.data.provider.service.EsDataService;
import cn.lz.data.provider.util.RandomUtil;
import org.apache.commons.compress.utils.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @Desc
 * @Author zhanglei
 * @Date 2024/1/2 17:05
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("dev")
public class DataTest {

    @Autowired
    private EsDataService esDataService;

    @Test
    public void testInsertData() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String time = formatter.format(LocalDateTime.now());
        List<EsData> dataList = Lists.newArrayList();
        for (int i = 0; i < 100000; i++) {
            String id = UUID.randomUUID().toString();
            String value = RandomUtil.getFamilyName() + i;
            EsData esData = new EsData();
            esData.setId(id);
            esData.setValue(value);
            esData.setTime(time);
            dataList.add(esData);
        }
        esDataService.insert(dataList);
    }

}
