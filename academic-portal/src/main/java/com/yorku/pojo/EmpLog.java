package com.yorku.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @Description: 事务日志
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmpLog {
    //ID
    private Integer id;
    //操作时间
    private LocalDateTime operateTime;
    //详细信息
    private String info;
}