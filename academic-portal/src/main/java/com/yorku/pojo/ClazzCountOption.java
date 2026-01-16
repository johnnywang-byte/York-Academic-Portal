package com.yorku.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Description: 班级模块柱状图饼图的实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClazzCountOption {
    //职位列表
    private List clazzList;
    //人数列表
    private List dataList;
}
