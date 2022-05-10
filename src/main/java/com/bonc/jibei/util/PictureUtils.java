package com.bonc.jibei.util;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.io.FileUtil;
import org.apache.poi.util.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;

/**
 * @Author: dupengling
 * @DateTime: 2022/4/23 22:46
 * @Description: TODO
 */
public final class PictureUtils {
    private static Logger logger = LoggerFactory.getLogger(PictureUtils.class);

    public PictureUtils() {
    }

    public static byte[] getUrlByteArray(String urlPath) {
        try {
            return toByteArray(getUrlPictureStream(urlPath));
        } catch (IOException var2) {
            logger.error("getUrlPictureStream error,{},{}", urlPath, var2);
            return null;
        }
    }

    public static byte[] getLocalByteArray(File res) {
        try {
            return toByteArray(new FileInputStream(res));
        } catch (FileNotFoundException var2) {
            logger.error("FileNotFound", var2);
            return null;
        }
    }

    public static byte[] getBufferByteArray(BufferedImage image) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();

        try {
            ImageIO.write(image, "png", os);
        } catch (IOException var3) {
            logger.error("getBufferByteArray error", var3);
        }

        return os.toByteArray();
    }

    public static byte[] toByteArray(InputStream is) {
        if (null == is) {
            return null;
        } else {
            try {
                byte[] var1 = IOUtils.toByteArray(is);
                return var1;
            } catch (IOException var11) {
                logger.error("toByteArray error", var11);
            } finally {
                try {
                    is.close();
                } catch (IOException var10) {
                    logger.error("close stream error", var10);
                }

            }
            return null;
        }
    }
    public static BufferedImage getUrlBufferedImage(String urlPath) {
        URL url = null;
        BufferedImage bufferImage = null;

        try {
            url = new URL(urlPath);
            bufferImage = ImageIO.read(url);
            return bufferImage;
        } catch (Exception var4) {
            logger.error("getUrlBufferedImage error, {}, {}", urlPath, var4);
            return null;
        }
    }

    public static BufferedImage getLocalBufferedImage(File res) {
        try {
            BufferedImage read = ImageIO.read(res);
            return read;
        } catch (FileNotFoundException var2) {
            logger.error("FileNotFound", var2);
        } catch (IOException var3) {
            logger.error("getLocalBufferedImage IO error", var3);
        }

        return null;
    }

    public static InputStream getUrlPictureStream(String urlPath) throws IOException {
        URL url = new URL(urlPath);
        return url.openConnection().getInputStream();
    }

    public static BufferedImage newBufferImage(int width, int height) {
        return new BufferedImage(width, height, 5);
    }

    public static String getImageBase(String filePath) {
        byte[] bytes = FileUtil.readBytes(filePath);
        return Base64.encode(bytes);
    }
}
