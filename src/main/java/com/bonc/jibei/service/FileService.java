package com.bonc.jibei.service;

import java.io.InputStream;

/**
 * @Author: dupengling
 * @DateTime: 2022/5/17 16:52
 * @Description: TODO
 */
public interface FileService {
    String upload(String fileName, InputStream is);
}
