package com.yorku.config;

import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.beans.factory.annotation.Autowired;
// import com.yorku.interceptor.TokenInterceptor;

/**
 * Web Configuration Class
 * Web 配置类
 *
 * Description: Configures global MVC settings, such as registering interceptors.
 * 描述：配置全局 MVC 设置，例如注册拦截器。
 *
 * Note: Currently commented out as we are using TokenFilter.
 * 注意：由于当前使用了 TokenFilter，此处代码已注释。
 */
//@Configuration // Marks this class as a Spring configuration class (标识此类为 Spring 配置类)
public class WebConfig implements WebMvcConfigurer {

//    @Autowired // Dependency Injection for the interceptor (依赖注入拦截器)
//    private TokenInterceptor tokenInterceptor;

//    /**
//     * Register Interceptors
//     * 注册拦截器
//     *
//     * @param registry The interceptor registry (拦截器注册表)
//     */
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//       // Add the custom token interceptor to the registry
//       // 将自定义的 Token 拦截器添加到注册表中
//       registry.addInterceptor(tokenInterceptor)
//               .addPathPatterns("/**") // Apply to all paths (拦截所有路径)
//               .excludePathPatterns("/login"); // Exclude the login path to allow public access (排除登录路径，允许公开访问)
//    }
}