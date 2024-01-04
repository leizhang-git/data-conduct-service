package cn.lz.data.consumer.lock;

import cn.hutool.core.util.StrUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Objects;
import java.util.UUID;

/**
 * @Desc
 * @Author zhanglei
 * @Date 2024/1/4 17:40
 */
@Component
public class RedisLock {

    private static final Logger log = LoggerFactory.getLogger(RedisLock.class);

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 锁的过期时间（s）
     */
    private static final int LOCK_OVERDUE_TIME = 60;

    /**
     * 最大尝试次数
     */
    private static final int MAX_RETRY = 50;

    /**
     * key前缀
     */
    private static final String KEY_PRE = "REDIS_LOCK";

    /**
     * 保证加锁&解锁的归一化
     * 加锁操作和解锁只能由同一线程来进行
     */
    private final ThreadLocal<String> threadLocal = new ThreadLocal<>();

    /**
     * 保证可重入锁
     */
    private final ThreadLocal<Integer> threadLocalInteger = new ThreadLocal<>();

    /**
     * 加锁
     * @param key
     * @return
     */
    public boolean tryLock(String key) {
        return tryLock(key, MAX_RETRY);
    }

    /**
     * 加锁
     * @param key
     * @param maxAttempts
     * @return
     */
    public boolean tryLock(String key, Integer maxAttempts) {
        boolean isLocked = false;
        key = KEY_PRE + "_" + key;
        if(StrUtil.isEmpty(threadLocal.get())) {
            isLocked = true;
        }
        if (threadLocal.get() == null) {
            threadLocal.set(lockValue());
            try {
                isLocked = addLock(key, lockValue(), LOCK_OVERDUE_TIME);
            }catch (Exception e) {
                log.error("获取所失败...", e);
                return false;
            }
            // 尝试获取锁失败，则自旋获取锁直至成功（需要设置最大重试次数）
            if (Boolean.FALSE.equals(isLocked)) {
                int count = -1;
                do {
                    ++count;
                    isLocked = addLock(key, lockValue(), LOCK_OVERDUE_TIME);
                } while (Boolean.FALSE.equals(isLocked) && count < MAX_RETRY);
            }
            // 启动新的线程来定期检查当前线程是否执行完成，并更新过期时间(避免超时超卖问题！)
            new Thread(new UpdateLockTimeoutTask(lockValue(), stringRedisTemplate, key)).start();
        }
        // 重入次数加1
        if (Boolean.TRUE.equals(isLocked)) {
            int count = threadLocalInteger.get() == null ? 0 :threadLocalInteger.get();
            threadLocalInteger.set(++count);
        }
        return Boolean.TRUE.equals(isLocked);
    }

    public void releaseLock(String key) {
        // 判断当前线程所对应的uuid是否与Redis对应的uuid相同，再执行删除锁操作
        if (threadLocal.get().equals(stringRedisTemplate.opsForValue().get(key))) {
            Integer count = threadLocalInteger.get();
            // 计数器减为0时才能释放锁
            if (count == null || --count <= 0) {
                stringRedisTemplate.delete(key);
                // 获取更新锁超时时间的线程并中断
                String threadId = stringRedisTemplate.opsForValue().get(lockValue());
                Thread updateLockTimeoutThread = ThreadUtil.getThreadByThreadId(threadId);
                if (updateLockTimeoutThread != null) {
                    // 中断更新锁超时时间的线程
                    updateLockTimeoutThread.interrupt();
                    stringRedisTemplate.delete(lockValue());
                }
            }
        }
    }

    public boolean addLock(String key, String value, long seconds) {
        return Boolean.TRUE.equals(stringRedisTemplate.execute((RedisCallback<Boolean>) redisConnection -> {
            ValueOperations<String, String> forValue = stringRedisTemplate.opsForValue();
            Boolean result = forValue.setIfAbsent(key, value, Duration.ofSeconds(seconds));
            return Objects.nonNull(result) ? result : Boolean.FALSE;
        }));
    }

    public String lockValue() {
        return UUID.randomUUID() + "-" + System.currentTimeMillis();
    }
}
