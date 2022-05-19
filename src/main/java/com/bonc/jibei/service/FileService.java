package com.bonc.jibei.service;

import java.io.InputStream;

/**
 * @Author: dupengling
 * @DateTime: 2022/5/17 16:52
 * @Description: TODO
 */
public interface FileService {
    /**
     * 上传文件
     * @param fileName：文件名
     * @param is：文件流
     * @return 返回文件路径和名
     */
    String upload(String fileName, InputStream is);

    /**
     * 删除文件
     * @param filePath：文件名
     */
    boolean delete(String filePath);
}
