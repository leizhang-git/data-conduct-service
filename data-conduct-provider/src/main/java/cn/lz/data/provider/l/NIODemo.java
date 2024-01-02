package cn.lz.data.provider.l;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @Desc
 * @Author zhanglei
 * @Date 2024/1/2 11:26
 */
public class NIODemo {

    static final String FROM = "D:\\test\\bak1\\ckm.mp4";
    static final String TO = "D:\\test\\bak2\\ckm_copy.mp4";
    static final int _1MB = 1024 * 1024;

    public static void main(String[] args) {
        io();
        nio();
    }

    private static void nio() {
        long start = System.nanoTime();
        try (FileChannel from = new FileInputStream(FROM).getChannel();
             FileChannel to = new FileOutputStream(TO).getChannel();
        ){
            ByteBuffer bb = ByteBuffer.allocateDirect(_1MB);
            while (true) {
                int len = from.read(bb);
                if(len == -1) {
                    break;
                }
                bb.flip();
                to.write(bb);
                bb.clear();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        long end = System.nanoTime();
        System.out.println("nio 用时：" + (end - start) / 1000_000.0);
    }

    private static void io() {
        long start = System.nanoTime();
        try (FileInputStream from = new FileInputStream(FROM);
             FileOutputStream to = new FileOutputStream(TO);
        ){
            byte[] buf = new byte[_1MB];
            while (true) {
                //读取from的数据，每次读1MB
                int len = from.read(buf);
                if(len == -1) {
                    break;
                }
                //从from复制到to
                to.write(buf, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        long end = System.nanoTime();
        System.out.println("io 用时：" + (end - start) / 1000_000.0);
    }
}
