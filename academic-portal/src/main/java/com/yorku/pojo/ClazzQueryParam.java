package com.yorku.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * @Description: 分页查询实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClazzQueryParam {
    private String name;
    private LocalDate begin;
    private LocalDate end;
    private Integer page=1;
    private Integer pageSize=10;
}
