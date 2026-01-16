package com.yorku.service;

import com.yorku.pojo.EmpLog;
import org.springframework.stereotype.Service;

/**
 * @Description: 事务监听
 */
@Service
public interface EmpLogService {
    public void insertLog(EmpLog empLog);
}
