package com.yorku.service;

import com.yorku.pojo.LogList;
import com.yorku.pojo.LogQueryParam;
import com.yorku.pojo.PageResult;

/**
 * @Description: 日志分页查询
 */
public interface LogService {
    /**
     * 日志管理--分页查询
     */
    PageResult<LogList> page(LogQueryParam logQueryParam);
}
