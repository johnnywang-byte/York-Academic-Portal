package com.yorku.controller;

import com.yorku.pojo.LogList;
import com.yorku.pojo.LogQueryParam;
import com.yorku.pojo.PageResult;
import com.yorku.pojo.Result;
import com.yorku.service.LogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created with IntelliJ IDEA.
 *
 * 
 *  : 2025/04/10/22:33
 * @Description: 日志列表分页查询
 */
@RestController
@Slf4j
@RequestMapping("/log")
public class LogController {

    @Autowired
    private LogService logService;

    @GetMapping("/page")
    public Result page(LogQueryParam logQueryParam) {
        log.info("分页查询日志信息,参数：{}", logQueryParam);
        PageResult<LogList> page = logService.page(logQueryParam);
        return Result.success(page);
    }

}
