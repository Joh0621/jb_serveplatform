package com.bonc.jibei.api;

import com.bonc.jibei.vo.KeyValueVO;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: dupengling
 * @DateTime: 2022/5/18 15:04
 * @Description: TODO
 */
public class EnumValue {
    public static List<Object> getUserTypeName(){
        List<Object> keyValueVOS=new ArrayList<>();
        for (InterEnum e: InterEnum.values()) {
            keyValueVOS.add(new KeyValueVO(e.getCode(),e.getName()));
        }
        return keyValueVOS;
    }
    public static List<Object> getReportStatus(){
        List<Object> keyValueVOS=new ArrayList<>();
        for (StatusEnum e: StatusEnum.values()) {
            keyValueVOS.add(new KeyValueVO(e.getCode(),e.getName()));
        }
        return keyValueVOS;
    }
}
