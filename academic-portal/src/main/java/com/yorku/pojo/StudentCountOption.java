package com.yorku.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Description: 学员统计模块-柱状图饼图
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentCountOption {
    private List clazzList;
    private List dataList;


}
