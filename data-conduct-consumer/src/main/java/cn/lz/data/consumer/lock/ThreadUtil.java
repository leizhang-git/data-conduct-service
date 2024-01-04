package cn.lz.data.consumer.lock;

/**
 * @Desc
 * @Author zhanglei
 * @Date 2024/1/4 17:39
 */
public class ThreadUtil {

    // 根据线程 id 获取线程句柄
    public static Thread getThreadByThreadId(String threadId) {
        ThreadGroup group = Thread.currentThread().getThreadGroup();
        while(group != null){
            Thread[] threads = new Thread[(int)(group.activeCount() * 1.2)];
            int count = group.enumerate(threads, true);
            for (int i = 0; i < count; i++){
                if (threadId.equals(threads[i].getId())) {
                    return threads[i];
                }
            }
        }
        return null;
    }
}
