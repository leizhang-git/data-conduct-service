package cn.lz.data.provider.domain;

import lombok.Data;

/**
 * @Author zhanglei
 * @Date 2023/12/21 14:34
 * @Desc
 */
@Data
public class ByteFile {

    private byte[] byteFile;

    private String fileName;

    public ByteFile(byte[] byteFile, String fileName) {
        this.byteFile = byteFile;
        this.fileName = fileName;
    }
}
