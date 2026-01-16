package com.yorku.mapper;

import com.yorku.pojo.EmpLog;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

/**
 * Employee Operation Log Mapper Interface
 * 员工操作日志 Mapper 接口
 * * Description: Handles the persistence of system operation logs.
 * 描述：处理系统操作日志的持久化。
 * * Context: Often used with Transaction Propagation (REQUIRES_NEW) to record logs even if the main business transaction rolls back.
 * 场景：通常配合事务传播行为 (REQUIRES_NEW) 使用，以确保即使主业务事务回滚，日志也能被成功记录（例如记录解散部门失败的原因）。
 */
@Mapper
public interface EmpLogMapper {

    /**
     * Insert Log Record
     * 插入日志记录
     * * @param empLog - The log entity containing the timestamp and operation info. (包含时间戳和操作信息的日志实体)
     */
    @Insert("insert into emp_log (operate_time, info) values (#{operateTime}, #{info})")
    public void insert(EmpLog empLog);

}