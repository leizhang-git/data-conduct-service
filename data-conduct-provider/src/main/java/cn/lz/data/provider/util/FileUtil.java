package cn.lz.data.provider.util;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.lz.data.provider.domain.ByteFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author zhanglei
 * @Date 2023/12/21 14:33
 * @Desc
 */
public class FileUtil {

    /**
     * 将list按行写入到txt文件中
     * @param strings
     * @param path
     * @throws Exception
     */
    public static void writeFileContext(List<String> strings, String path) throws Exception {
        File file = new File(path);
        //如果没有文件就创建
        if (!file.isFile()) {
            file.createNewFile();
        }
        BufferedWriter writer = new BufferedWriter(new FileWriter(path));
        for (String l:strings){
            writer.write(l + "\r\n");
        }
        writer.close();
    }

    /**
     * 读取文件内容至List中
     * @param filePath
     * @param list
     * @throws IOException
     */
    public static void getFileContent(String filePath, List<String> list) throws IOException {
        BufferedReader br = null;
        if(ObjectUtil.isEmpty(filePath)) {
            return;
        }
        br = new BufferedReader(new InputStreamReader(Files.newInputStream(Paths.get(filePath)), StandardCharsets.UTF_8));
        String line;
        while ((line = br.readLine()) != null) {
            if(StrUtil.isNotEmpty(line)) {
                list.add(line);
            }
        }
        br.close();
    }

    /**
     * 文件转Byte[]
     * @param file
     * @param fileName
     * @return
     * @throws IOException
     */
    public static ByteFile uploadFile(MultipartFile file, String fileName) throws IOException {
        InputStream inputStream = file.getInputStream();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int i;
        byte[] bytes = new byte[1024];
        //转字节数组流
        while ((i = inputStream.read(bytes)) != -1) {
            byteArrayOutputStream.write(bytes, 0, i);
        }
        //放入字节数组
        byte[] fileByte = byteArrayOutputStream.toByteArray();
        inputStream.close();
        byteArrayOutputStream.close();
        ByteFile byteFile = new ByteFile(fileByte, fileName);
        return byteFile;
    }

    /**
     * 文件转List
     * @param fileName
     * @return
     * @throws IOException
     */
    public List<String> fileToList(String fileName) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
        List<String> result = new ArrayList<>();
        String str;
        while (null != (str = bufferedReader.readLine())) {
            result.add(str);
        }
        bufferedReader.close();
        return result;
    }

    public static byte[] inputToByte(InputStream inputStream) throws IOException {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int num = inputStream.read(buffer);
            while (num != -1) {
                baos.write(buffer, 0, num);
                num = inputStream.read(buffer);
            }
            baos.flush();
            return baos.toByteArray();
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }

    /**
     * 创建目录
     * @param destDirName
     * @return
     */
    public static Boolean createDir(String destDirName) {
        File dir = new File(destDirName);
        //判断有没有父路径，就是判断文件整个路径是否存在
        if (!dir.getParentFile().exists()) {
            //不存在就全部创建
            return dir.getParentFile().mkdirs();
        }
        return false;
    }
}
