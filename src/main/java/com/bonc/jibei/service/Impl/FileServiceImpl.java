package com.bonc.jibei.service.Impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.extra.ftp.Ftp;
import cn.hutool.extra.ftp.FtpException;
import cn.hutool.extra.ftp.FtpMode;
import com.bonc.jibei.config.WordCfgProperties;
import com.bonc.jibei.service.FileService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.*;

/**
 * @Author: dupengling
 * @DateTime: 2022/5/17 16:53
 * @Description: TODO
 */
@Service
public class FileServiceImpl implements FileService {
    @Resource
    WordCfgProperties wordCfgProperties;

    @Override
    public String upload(String fileName,InputStream is) {
        String path=wordCfgProperties.getModelPath();
        File tmp=new File(path);
        if(!tmp.isDirectory()){
            tmp.mkdirs();
        }
        fileName =  path+ "/" +  UUID.fastUUID().toString(true) + "." + FileUtil.getSuffix(fileName);
        try {
            //获取输出流
            OutputStream os=new FileOutputStream(fileName);
            //获取输入流 CommonsMultipartFile 中可以直接得到文件的流
            byte[] bts = new byte[1024];
            //一个一个字节的读取并写入
            while(is.read(bts)!=-1)
            {
                os.write(bts);
            }
            os.flush();
            os.close();
            is.close();

        } catch (FileNotFoundException e) {
            throw new FtpException("文件没发现！", e);
        } catch (IOException e) {
            throw new FtpException("文件上传失败！", e);
        }

        return fileName;
    }
    @Override
    public boolean delete(String fileName) {
        String path=wordCfgProperties.getModelPath();
        File p=new File(path);
        if(!p.isDirectory()){
            return false;
        }
        fileName =  path+ "/" +fileName;
        File f=new File(path);
        if(!f.isDirectory()){
            return false;
        }
        return f.delete();
    }
}
