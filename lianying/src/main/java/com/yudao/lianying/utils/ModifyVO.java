package com.yudao.lianying.utils;

import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author liudong
 * @since 2020-12-15
 */
@Data
@ToString
public class ModifyVO {
    String id;
    List<String> nameList;
    List<String> valueList;
}
