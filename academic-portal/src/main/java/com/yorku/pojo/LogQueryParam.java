package com.yorku.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: 日志分页查询实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogQueryParam {
    private Integer page=1;
    private Integer pageSize=10;

}
