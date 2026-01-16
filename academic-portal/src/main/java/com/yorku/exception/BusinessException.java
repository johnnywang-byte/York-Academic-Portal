package com.yorku.exception;

/**
 * @Description: 自定义业务异常（继承RuntimeException避免强制try-catch）
 */
public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
}