package com.yorku.interceptor;

import com.yorku.util.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Security Interceptor (JWT Authentication Guard)
 * York University Academic Admin Portal
 *
 * Description:
 * Acting as the "Gatekeeper" for the API.
 * Intercepts incoming HTTP requests to validate the JSON Web Token (JWT).
 * Ensures only authenticated staff/faculty can access protected resources.
 *
 * @Description: 令牌校验拦截器 (安全网关)
 * 负责拦截所有请求，校验 Header 中的 JWT 是否合法。
 */
@Slf4j
@Component // Standard Spring Component
public class TokenInterceptor implements HandlerInterceptor {

    /**
     * Pre-Handle: Executed before the target Controller method.
     * 在目标方法运行之前拦截
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // 1. Path Analysis
        // 获取请求路径
        String path = request.getRequestURI();

        // 📢 Critical Fix for CORS: Allow OPTIONS pre-flight requests
        // 关键修复：放行浏览器的 OPTIONS 预检请求 (防止前端跨域报错)
        if ("OPTIONS".equals(request.getMethod())) {
            return true;
        }

        // 2. Public Endpoint Bypass
        // 排除公开路径（如登录接口），直接放行
        // Note: Ideally, exclusions are configured in WebConfig, but this serves as a double-check.
        if (path.contains("/login")) {
            log.info("🔓 [York U Security] Public Endpoint Accessed: {}", path);
            return true;
        }

        // 3. Token Extraction
        // 从请求头中获取令牌 (Standard header key: 'token')
        String token = request.getHeader("token");

        // 4. Validation: Check if token exists
        // 判断令牌是否存在，不存在则返回 401 未授权
        if (token == null || token.isEmpty()) {
            log.warn("⛔ [Access Denied] Missing Authentication Token. Path: {}", path);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 Unauthorized
            return false;
        }

        // 5. Validation: Verify Token Integrity
        // 解析令牌，校验是否合法或过期
        try {
            JwtUtils.parseToken(token);
            // Log success only on DEBUG level to avoid log spamming in production
            log.debug("✅ [Auth Success] Token Validated. Access Granted.");
            return true; // 放行

        } catch (Exception e) {
            // Token is invalid, tampered, or expired
            // 令牌非法或过期，拦截并返回 401
            log.error("❌ [Auth Failure] Invalid Token: {}", e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
    }
}