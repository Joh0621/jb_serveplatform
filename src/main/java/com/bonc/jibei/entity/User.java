package com.bonc.jibei.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: dupengling
 * @DateTime: 2022/4/19 17:30
 * @Description: TODO
 */
@Data
@TableName("jb_user")
public class User {
    @TableId(type = IdType.AUTO)
    @ApiModelProperty("用户ID")
    private Integer userId;

    @ApiModelProperty("父用户ID")
    private Integer parentid;

    @ApiModelProperty("组织ID")
    private Integer orgId;

    @ApiModelProperty("用户编码")
    private String userCode;

    @ApiModelProperty("用户名")
    private String userName;

    @ApiModelProperty("性别")
    private Integer userSex;

    @ApiModelProperty("联系电话")
    private String telephone;

    @ApiModelProperty("邮件")
    private String mail;

    @ApiModelProperty("登陆名")
    private String loginName;

    @ApiModelProperty("密码")
    private String loginPwd;

    @ApiModelProperty("用户状态")
    private Integer userStatus;

    @ApiModelProperty("证件类型")
    private Integer idsType;

    @ApiModelProperty("证件号码")
    private String idsNo;

    @ApiModelProperty("民族")
    private String idsNation;

    @ApiModelProperty("地址")
    private String userAddress;

    @ApiModelProperty("删除标识，1=删除")
    private Integer isDelete;

    @ApiModelProperty("账号")
    private Integer accountType;

    @ApiModelProperty("创建时间")
    private String createTime;

    @ApiModelProperty("创建人")
    private Integer createUserId;

    @ApiModelProperty("修改时间")
    private String modifyTime;

    @ApiModelProperty("修改人")
    private Integer modifyUserId;

    @ApiModelProperty("删除时间")
    private String deleteTime;

    @ApiModelProperty("删除人")
    private Integer deleteUserId;
}
