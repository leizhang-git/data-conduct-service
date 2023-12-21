package cn.lz.data.provider.util;

import de.innosystec.unrar.Archive;
import de.innosystec.unrar.NativeStorage;
import de.innosystec.unrar.rarfile.FileHeader;
import lombok.extern.slf4j.Slf4j;
import net.lingala.zip4j.ZipFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @Author zhanglei
 * @Date 2023/12/21 15:08
 * @Desc
 */
@Slf4j
public class DeCompressUtil {


    /**
     *
     * @Title: unPackZip<BR>
     * @Description:  zip文件解压<BR>
     * @date: 2021年4月5日<BR>
     * @param zipFile
     * @param password
     * @param destPath
     */
    public static void unPackZip(File zipFile, String password, String destPath) {
        ZipFile zip = null;
        try {
            zip = new ZipFile(zipFile);
            zip.setCharset(StandardCharsets.UTF_8);
            log.info("begin unpack zip file....");
            zip.extractAll(destPath);
            // 如果解压需要密码
            if (zip.isEncrypted()) {
                zip.setPassword(password.toCharArray());
            }
        } catch (Exception e) {
            log.error("unPack zip file to " + destPath + " fail ....", e.getMessage(), e);
        } finally {
            try {
                assert zip != null;
                zip.close();
            } catch (IOException e) {
                log.error("unPack zip file to " + destPath + " fail ....", e.getMessage(), e);
            }
        }
    }

    /**
     *
     * @Title: unrar<BR>
     * @Description: 解压rar格式压缩包。对应的是java-unrar-0.3.jar，但是java-unrar-0.3.jar又会用到commons-logging-1.1.1.jar  <BR>
     * @date: 2021年4月5日<BR>
     * @param destDir
     * @throws Exception
     */
    public static void unrar(File file,String destDir) throws Exception{
        Archive a = null;
        FileOutputStream fos = null;
        try{
            NativeStorage ns = new NativeStorage(file);
            a = new Archive(ns);
            FileHeader fh = a.nextFileHeader();
            while(fh!=null){
                if(!fh.isDirectory()){
                    //1 根据不同的操作系统拿到相应的 destDirName 和 destFileName
                    String compressFileName = fh.getFileNameW().isEmpty()?fh.getFileNameString():fh.getFileNameW();
                    //非windows系统
                    String destFileName = destDir + compressFileName.replaceAll("\\\\", "/");
                    String destDirName = destFileName.substring(0, destFileName.lastIndexOf("/"));
                    //2创建文件夹
                    File dir = new File(destDirName);
                    if(!dir.exists()||!dir.isDirectory()){
                        dir.mkdirs();
                    }
                    //3解压缩文件
                    fos = new FileOutputStream(new File(destFileName));
                    a.extractFile(fh, fos);
                    fos.close();
                    fos = null;
                }
                fh = a.nextFileHeader();
            }
            a.close();
            a = null;
        }catch(Exception e){
            throw e;
        }finally{
            if(fos!=null){
                try{fos.close();fos=null;}catch(Exception e){e.printStackTrace();}
            }
            if(a!=null){
                try{a.close();a=null;}catch(Exception e){e.printStackTrace();}
            }
        }
    }
}
