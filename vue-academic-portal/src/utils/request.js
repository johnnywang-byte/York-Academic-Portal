import axios from 'axios';
import { ElMessage } from 'element-plus';
import router from '../router'

/**
 * Axios Instance Configuration
 * York U Academic Admin Portal
 *
 * @description
 * Global HTTP client configuration. Handles JWT injection and centralized error management.
 * 全局 HTTP 客户端配置。负责处理 JWT 令牌注入和统一错误管理。
 *
 * @property {string} baseURL - API prefix (defined in vite.config.js proxy) / 基础路径
 * @property {number} timeout - Request timeout in ms (10 mins for AI tasks) / 请求超时时间
 */
const request = axios.create({
  baseURL: '/api',
  timeout: 600000
});

// ----------------------------------------------------------------------
// 1. Request Interceptor (Outbound)
// 请求拦截器：在发送请求前自动注入 Token
// ----------------------------------------------------------------------
request.interceptors.request.use(
  (config) => {
    // Retrieve user session from LocalStorage
    // 获取本地存储的用户信息
    const loginUser = JSON.parse(localStorage.getItem('loginUser') || '{}');

    // Inject Token into headers if it exists
    // 如果 Token 存在，则将其添加到请求头 'token' 字段中
    if (loginUser && loginUser.token) {
      config.headers.token = loginUser.token;
    }
    return config;
  },
  (error) => {
    // Handle request configuration errors
    // 处理请求配置错误
    return Promise.reject(error);
  }
)

// ----------------------------------------------------------------------
// 2. Response Interceptor (Inbound)
// 响应拦截器：统一处理返回数据和 HTTP 错误
// ----------------------------------------------------------------------
request.interceptors.response.use(
  (response) => {
    // Success: Return the 'data' payload directly
    // 响应成功：直接返回后端的数据层 (Result Object)
    // Common structure: { code: 1, msg: 'success', data: ... }
    return response.data;
  },
  (error) => {
    // Failure: Handle HTTP errors centrally
    // 响应失败：统一错误处理逻辑

    // Check if a response was received from the server
    // 判断是否有服务端响应
    if (error.response) {
      const status = error.response.status;

      if (status === 401) {
        // 401 Unauthorized: Token expired or invalid
        // 登录超时或未授权 -> 跳转登录页
        ElMessage.error('Session Expired. Please login via Passport York.');
        router.push('/login');
      } else if (status === 500) {
        // 500 Server Error
        ElMessage.error('Internal Server Error. Please contact IT Helpdesk.');
      } else if (status === 404) {
        // 404 Not Found
        ElMessage.error('Resource Not Found (404).');
      } else {
        // Other errors
        // 其他异常
        ElMessage.error(`System Interface Error: ${status}`);
      }
    } else {
      // Network Error (Timeout, DNS, etc.)
      // 网络错误（无响应）
      ElMessage.error('Network Error. Please check your connection.');
    }

    // Propagate the error for local handling if needed
    // 返回 rejected 状态，以便调用方捕获
    return Promise.reject(error);
  }
);

// Export the instance
// 导出实例
export default request;