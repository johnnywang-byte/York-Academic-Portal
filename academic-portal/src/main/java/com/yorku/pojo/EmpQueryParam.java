package com.yorku.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat; // 1. 记得导包
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmpQueryParam {
    // 当前页
    private Integer page = 1;
    // 每页显示条数
    private Integer pageSize = 10;
    // 姓名
    private String name;
    // 性别
    private Integer gender;

    // 开始时间
    @DateTimeFormat(pattern = "yyyy-MM-dd") // 2. 加上 @ 符号，并使用正确注解名
    private LocalDate begin;

    // 结束时间
    @DateTimeFormat(pattern = "yyyy-MM-dd") // 3. 加上 @ 符号
    private LocalDate end;
}