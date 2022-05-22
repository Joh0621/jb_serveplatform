package com.bonc.jibei.util;

import com.deepoove.poi.data.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class PoiTLUtils {

    /** 图片默认长宽 */
    private static final int DEFAULT_WIDTH = 600;
    private static final int DEFAULT_HEIGHT = 360;

    /**
     * 生成图片数据
     * @param path 图片路径
     * @return 图片数据
     */
    public static PictureRenderData picData(String path) {
        return picData(path, PictureType.PNG);
    }

    /**
     * 生成图片数据
     * @param path 图片路径
     * @param type 图片类型
     * @return 图片数据
     */
    public static PictureRenderData picData(String path, PictureType type) {
        try {
            return Pictures.ofStream(new FileInputStream(path), type).size(DEFAULT_WIDTH, DEFAULT_HEIGHT).create();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}
