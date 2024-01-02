package cn.lz.data.provider.service;

import cn.lz.data.provider.dao.EsDataDao;
import cn.lz.data.provider.domain.pojo.EsData;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Desc
 * @Author zhanglei
 * @Date 2024/1/2 17:13
 */
@Service
public class EsDataService extends ServiceImpl<EsDataDao, EsData> {

    public void insert(List<EsData> esDataList) {
        this.saveBatch(esDataList);
    }

}
