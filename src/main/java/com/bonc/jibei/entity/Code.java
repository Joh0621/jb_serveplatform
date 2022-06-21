package com.bonc.jibei.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("gb_codes")
public class Code {
    /**
     * id
     * (主键ID)
     * (无默认值)
     */
    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * 父级id,场站或者设备;
     * (可选项)
     * (无默认值)
     */
    private Integer pid;
    /**
     * 编码id与类型表关联;
     * (可选项)
     * (无默认值)
     */
    private Integer codeId;
    /**
     * 编码信息，数值或文字;
     * (可选项)
     * (无默认值)
     */
    private String codeDetail;
    /**
     * 数据来源,0代表本表，1代表从device表的台账数据
     * (可选项)
     * (无默认值)
     */
    private Integer dataSources;
}
