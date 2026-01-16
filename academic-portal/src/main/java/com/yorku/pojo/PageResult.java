package com.yorku.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Description: 分页查询返回的数据实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageResult <T>{
    //页码
    private Long total;
    //当前页数据
    private List<T> rows;


}
