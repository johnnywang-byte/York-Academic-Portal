package com.yorku.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yorku.mapper.LogMapper;
import com.yorku.pojo.LogList;
import com.yorku.pojo.LogQueryParam;
import com.yorku.pojo.PageResult;
import com.yorku.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: 日志分页查询
 */
@Service
public class LogServiceImpl implements LogService {

    @Autowired
    private LogMapper logMapper;
    @Override
    public PageResult<LogList> page(LogQueryParam logQueryParam) {
        //1.设置分页参数
        PageHelper.startPage(logQueryParam.getPage(),logQueryParam.getPageSize());
        //2.调用分页查询的方法
        List<LogList> clazzList =logMapper.list(logQueryParam);
        //3.封装结果并返回
        Page<LogList> pageResult = (Page<LogList>) clazzList;
        return new PageResult<LogList>(pageResult.getTotal(),pageResult.getResult());
    }
}
