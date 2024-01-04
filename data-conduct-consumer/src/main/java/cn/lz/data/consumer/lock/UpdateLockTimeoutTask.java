package cn.lz.data.consumer.lock;

import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.TimeUnit;

/**
 * @Desc
 * @Author zhanglei
 * @Date 2024/1/4 17:39
 */
public class UpdateLockTimeoutTask implements Runnable {

    private String uuid;
    private String key;
    private StringRedisTemplate stringRedisTemplate;

    public UpdateLockTimeoutTask(String uuid, StringRedisTemplate stringRedisTemplate, String key) {
        this.uuid = uuid;
        this.key = key;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public void run() {
        stringRedisTemplate.opsForValue().set(uuid, String.valueOf(Thread.currentThread().getId()));
        // 定期更新锁的过期时间
        while (true) {
            stringRedisTemplate.expire(key, 10, TimeUnit.SECONDS);
            try{
                // 每隔3秒执行一次
                Thread.sleep(10000);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }
}
