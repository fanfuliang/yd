package com.yudao.lianying.v1.whitelist.vo;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;
import lombok.Data;
import lombok.ToString;
import io.swagger.annotations.*;
import java.io.Serializable;

/**
 * <p>
 * 白名单
 * </p>
 *
 * @author liudong
 * @since 2021-11-09
 */
@Data
@ToString
public class WhitelistVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "创建人id")
    private String createBy;
    @ApiModelProperty(value = "登录名")
    private String loginName;
    @ApiModelProperty(value = "手机号")
    private String mobile;


}
