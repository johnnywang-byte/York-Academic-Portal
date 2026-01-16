package com.yorku.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Description: Entity classes for bar charts and pie charts 柱状图饼状图的实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobOption {

    //职位列表
    private List jobList;
    //数据列表
    private List dataList;
}
