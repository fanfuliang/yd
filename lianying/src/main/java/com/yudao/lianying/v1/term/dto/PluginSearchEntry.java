package com.yudao.lianying.v1.term.dto;

import com.baomidou.mybatisplus.annotations.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import java.util.List;

/**
 * <Description> <br>
 *
 * @author Wufei<br>
 * @version 1.0<br>
 * @taskId: <br>
 * @createDate 2023/10/02 23:41 <br>
 * @see com.yudao.lianying.v1.account.dao <br>
 */
@Data
public class PluginSearchEntry {
    private String Id;

    private String field;
    private String fieldName;
    private String applicableTo;
    private String applicableToName;
    private String partOfSpeech;
    private String partOfSpeechName;
    private String note;

    private List<PluginSearchLanguage> Languages;
}
