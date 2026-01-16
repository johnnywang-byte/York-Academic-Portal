package com.yorku.filter;

import com.yorku.util.CurrentHolder;
import com.yorku.util.JwtUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * Token Validation Filter
 * Intercepts incoming requests to validate JWT tokens.
 * 令牌校验过滤器
 * 拦截请求并校验 JWT 令牌的有效性。
 */
@Slf4j
@WebFilter(urlPatterns = {"/*"}) // Intercept all requests / 拦截所有请求
public class TokenFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        // 1. Get the request URI
        // 1. 获取请求路径
        String path = request.getRequestURI();

        // 2. Check for public endpoints (Login or AI Search). If matched, permit the request directly.
        // 2. 检查是否为公开接口（登录 或 AI搜索）。如果是，直接放行，无需校验令牌。
        // 🔴 KEY CHANGE: Added "|| path.contains("/ai")" to allow AI testing without login
        if (path.contains("/login") || path.contains("/ai")) {
            log.info("Public endpoint accessed (Login/AI), permitting request. Path: {}", path);
            filterChain.doFilter(request, response);
            return;
        }

        // 3. Retrieve the token from the request header
        // 3. 从请求头中获取令牌
        String token = request.getHeader("token");

        // 4. Validate token existence. Return 401 Unauthorized if missing.
        // 4. 校验令牌是否存在。若不存在，返回 401 未授权。
        if (token == null || token.isEmpty()) {
            log.warn("Token is missing in header. / 请求头中缺少令牌。");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        // 5. Parse token and validate. If valid, store user info; otherwise return 401.
        // 5. 解析并校验令牌。若合法则存储用户信息，否则返回 401。
        try {
            // Parse token to get Claims (Payload)
            // 解析令牌获取 Claims 载荷
            Claims claims = JwtUtils.parseToken(token);

            // Extract User ID and store in ThreadLocal
            // 获取用户 ID 并存入 ThreadLocal
            Integer id = Integer.valueOf(claims.get("id").toString());
            CurrentHolder.setCurrentId(id);

            log.info("Token validated. Current User ID: {}", id);

        } catch (Exception e) {
            log.error("Invalid Token: {}", e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return; // Stop execution here / 停止执行
        }

        // 6. Token is valid, proceed with the request chain
        // 6. 令牌校验通过，放行请求
        filterChain.doFilter(request, response);

        // 7. Clean up ThreadLocal to prevent memory leaks
        // 7. 请求结束后清除 ThreadLocal，防止内存泄漏
        CurrentHolder.remove();
    }
}