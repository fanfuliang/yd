package com.yudao.lianying.v1.basicConfig.vo;

import com.baomidou.mybatisplus.annotations.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import javax.persistence.Column;
import java.io.Serializable;

/**
 * <Description> <br>
 *
 * @author Wufei<br>
 * @version 1.0<br>
 * @taskId: <br>
 * @createDate 2023/09/14 22:56 <br>
 * @see com.yudao.lianying.v1.basicConfig.vo <br>
 */
@Data
@ToString
public class LanguageVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "创建人id")
    private String createBy;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value="中文名称")
    private String nameZh;
    @ApiModelProperty(value="英文名称")
    private String nameEn;
    @ApiModelProperty(value="编码")
    private String code;
    @ApiModelProperty("用途（1-备用，2-快译源语言，4-快译目标语言），位运算，每种用途占用一位的1，对逻辑与运算的结果进行判断，例：3 & 2 == 2，说明有2这个类型")
    private Integer purpose;
    @ApiModelProperty(value="拼音")
    private String pinyin;
    @ApiModelProperty(value="语系id")
    private String langFamilyId;
}
