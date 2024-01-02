package cn.lz.data.provider.l;

import java.util.concurrent.CountDownLatch;

/**
 * @Desc
 * @Author zhanglei
 * @Date 2024/1/2 16:18
 */
public class CountDownLatchDemo {
    public static void main(String[] args) throws InterruptedException {
        //初始化一个倒计时锁，参数为3
        CountDownLatch latch = new CountDownLatch(3);

        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "-begin...");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //count--
            latch.countDown();
            System.out.println(Thread.currentThread().getName() + "-end..." + latch.getCount());
        }).start();

        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "-begin...");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //count--
            latch.countDown();
            System.out.println(Thread.currentThread().getName() + "-end..." + latch.getCount());
        }).start();

        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "-begin...");
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //count--
            latch.countDown();
            System.out.println(Thread.currentThread().getName() + "-end..." + latch.getCount());
        }).start();

        String name = Thread.currentThread().getName();
        System.out.println(name + "-waiting...");
        //等待其他线程完成
        latch.await();
        System.out.println(name + "-wait end...");
    }
}
