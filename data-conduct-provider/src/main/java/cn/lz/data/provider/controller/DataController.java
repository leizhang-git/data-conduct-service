package cn.lz.data.provider.controller;

import cn.hutool.core.lang.UUID;
import cn.lz.data.provider.domain.pojo.EsData;
import cn.lz.data.provider.service.l.EsDataService;
import cn.lz.data.provider.util.RandomUtil;
import org.apache.commons.compress.utils.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @Desc
 * @Author zhanglei
 * @Date 2024/1/2 18:03
 */
@RequestMapping("/data")
@RestController
public class DataController {

    @Autowired
    private EsDataService esDataService;

    @GetMapping("/insertEsData")
    public void getSystemData() {
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
