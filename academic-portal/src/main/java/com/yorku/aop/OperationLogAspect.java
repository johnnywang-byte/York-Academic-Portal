package com.yorku.aop;

import com.yorku.mapper.OperateLogMapper;
import com.yorku.pojo.OperateLog;
import com.yorku.util.CurrentHolder;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;

/**
 * System Audit Logging Aspect
 * York University Academic Admin Portal
 *
 * Description:
 * Intercepts methods annotated with @Log to record operational audit trails.
 * Uses AOP (Aspect Oriented Programming) to capture execution details including
 * operator ID, method signature, execution time, and return values.
 *
 * @Description: 系统操作日志切面类
 * 用于拦截控制器方法并记录审计日志。
 * 通过AOP实现环绕通知，记录方法执行详情（操作人、耗时、参数等），
 * 并持久化到数据库中。
 */
@Slf4j
@Aspect
@Component
public class OperationLogAspect {

    @Autowired
    private OperateLogMapper operateLogMapper;

    /**
     * Around Advice: Wraps the target method execution to capture metrics.
     * 环绕通知：包裹目标方法的执行以捕获指标。
     *
     * @param joinPoint Execution context / 连接点上下文
     * @return Method execution result / 方法执行结果
     */
    @Around("@annotation(com.yorku.anno.Log)")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        // 1. Record Start Time
        // 记录开始时间
        long startTime = System.currentTimeMillis();

        // 2. Execute Target Method (Proceed with the actual business logic)
        // 执行目标方法（继续执行实际业务逻辑）
        Object result = joinPoint.proceed();

        // 3. Calculate Execution Duration (Latency)
        // 计算方法执行耗时
        long endTime = System.currentTimeMillis();
        long costTime = endTime - startTime;

        // 4. Build Audit Log Object
        // 构建操作日志对象
        OperateLog operateLog = new OperateLog();

        // Operator ID (Current Logged-in Staff/Admin from ThreadLocal)
        // 获取当前操作人ID（从当前线程获取）
        operateLog.setOperateEmpId(getOperateEmpId());

        // Operation Timestamp
        // 操作时间
        operateLog.setOperateTime(LocalDateTime.now());

        // Target Class Name
        // 操作类名
        operateLog.setClassName(joinPoint.getTarget().getClass().getName());

        // Target Method Name
        // 方法名
        operateLog.setMethodName(joinPoint.getSignature().getName());

        // Execution Cost (ms)
        // 执行耗时(毫秒)
        operateLog.setCostTime(costTime);

        // Request Arguments (Method Parameters)
        // 方法参数
        operateLog.setMethodParams(Arrays.toString(joinPoint.getArgs()));

        // Return Value (Serialized to String, handle nulls)
        // 返回值 (处理空值情况)
        operateLog.setReturnValue(result != null ? result.toString() : "void");

        // 5. Persist Audit Log to Database
        // 保存日志到数据库
        log.info("Audit Log Entry: {}", operateLog);
        operateLogMapper.insert(operateLog);

        return result;
    }

    /**
     * Retrieve Current User ID
     * Helper method to get the ID from the security context holder.
     *
     * 获取当前操作员ID
     * 辅助方法：从安全上下文（ThreadLocal）中获取ID。
     */
    private Integer getOperateEmpId() {
        return CurrentHolder.getCurrentId();
    }
}