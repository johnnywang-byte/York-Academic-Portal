package com.yorku.pojo;

import lombok.Data;

import java.time.LocalDate;

@Data
public class EmpExpr {
    //ID
    private Integer id;
    //员工ID
    private Integer empId;
    //开始时间
    private LocalDate begin;
    //结束时间
    private LocalDate end;
    //公司名称
    private String company;
    //职位
    private String job;
}