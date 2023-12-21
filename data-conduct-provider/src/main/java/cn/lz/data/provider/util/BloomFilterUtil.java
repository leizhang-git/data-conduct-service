package cn.lz.data.provider.util;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import com.google.common.base.Charsets;

import java.util.stream.IntStream;

/**
 * 布隆过滤器
 */
public class BloomFilterUtil {

    public static void main(String[] args) {
        // 总数量
        int total = 1000000;
        BloomFilter<CharSequence> bf = BloomFilter.create(Funnels.stringFunnel(Charsets.UTF_8), total, 0.0002);
        // 初始化 1000000 条数据到过滤器中
        IntStream.range(0, total).forEach(i -> bf.put("" + i));
        // 判断值是否存在过滤器中
        int count = (int) IntStream.range(0, total + 10000).filter(i -> bf.mightContain("" + i)).count();
        System.out.println("已匹配数量 " + count);
    }
}
