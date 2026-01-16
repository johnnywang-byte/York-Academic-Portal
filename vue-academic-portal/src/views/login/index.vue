<script setup>
/**
 * Component: Login Page
 * 组件：登录页面
 * * Description:
 * Entry point for the Academic Management System.
 * Customized with York University branding colors (York Red).
 * 描述：
 * 学术管理系统的入口页面。
 * 采用了约克大学的品牌色（York Red）进行定制。
 */

import { ref } from 'vue'
import { loginApi } from '@/api/login'
import { ElMessage } from 'element-plus';
import { useRouter } from 'vue-router'

// Router instance for navigation
// 路由实例，用于页面跳转
const router = useRouter();

// Reactive form data model
// 响应式表单数据模型
let loginForm = ref({
  username: '',
  password: ''
})

/**
 * Function: Handle Login
 * 功能：处理登录逻辑
 * * Logic:
 * 1. Call backend API to verify credentials.
 * 2. If success: Show notification, save token/user info, redirect to Dashboard.
 * 3. If failure: Show error message.
 * 逻辑：
 * 1. 调用后端 API 验证凭据。
 * 2. 若成功：显示提示，保存 Token/用户信息，跳转至仪表盘。
 * 3. 若失败：显示错误信息。
 */
const login = async () => {
  // Call the login API
  // 调用登录接口
  const result = await loginApi(loginForm.value);

  if (result.code) {
    // SUCCESS: Display welcome message using Element Plus
    // 成功：使用 Element Plus 显示欢迎信息
    ElMessage({
      message: 'Welcome to York U Portal', // Custom welcome text (自定义欢迎语)
      type: 'success',
      plain: true,     // Flat style for modern look (扁平化风格，更具现代感)
      showClose: true, // Allow user to close manually (允许手动关闭)
      duration: 3000,
      customClass: 'york-message-success' // Custom CSS hook (自定义样式钩子)
    });

    // Persist user session data to LocalStorage
    // 将用户会话数据持久化保存到 LocalStorage
    localStorage.setItem('loginUser', JSON.stringify(result.data));

    // Redirect to the main Dashboard
    // 跳转至主仪表盘页面
    router.push('/index');

  } else {
    // ERROR: Display failure message
    // 错误：显示失败信息
    ElMessage({
      message: result.msg || 'Authentication Failed',
      type: 'error',
      plain: true,
      showClose: true,
      duration: 4000,
      customClass: 'york-message-error'
    });
  }
}

/**
 * Function: Reset Form
 * 功能：重置表单
 */
const clear = () => {
  loginForm.value.username = ''
  loginForm.value.password = ''
}
</script>

<template>
  <div id="container">
    <div class="login-form">
      <el-form label-width="90px">

        <p class="title">
          <span class="york-red">York U</span> Academic Portal
        </p>

        <el-form-item label="Username" prop="username">
          <el-input v-model="loginForm.username" placeholder="Passport York Username"></el-input>
        </el-form-item>

        <el-form-item label="Password" prop="password">
          <el-input
            type="password"
            v-model="loginForm.password"
            placeholder="Password"
            show-password
            @keyup.enter="login"
          ></el-input>
        </el-form-item>

        <el-form-item>
          <el-button class="button york-btn-primary" type="primary" @click="login">Sign In</el-button>

          <el-button class="button" type="info" @click="clear" plain>Reset</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<style scoped>
/* * Layout Container
 * 布局容器
 * Uses Flexbox to perfectly center the login card on the screen.
 * 使用 Flexbox 将登录卡片完美居中显示。
 */
#container {
  height: 100vh;
  width: 100%;
  padding: 0;
  display: flex;
  justify-content: center; /* Horizontal Center (水平居中) */
  align-items: center;     /* Vertical Center (垂直居中) */

  /* Background Image Configuration */
  /* 背景图片配置 */
  background-image: url('../../assets/bg1.jpg');
  background-repeat: no-repeat;
  background-size: cover;
}

/* * Login Card Styling
 * 登录卡片样式
 */
.login-form {
  width: 400px;
  padding: 40px 30px;
  border-radius: 4px; /* Slightly squared corners for academic feel (微圆角，体现学术严谨感) */
  box-shadow: 0 8px 30px rgba(0, 0, 0, 0.3); /* Deep shadow for depth (深阴影增加层次感) */
  background-color: #ffffff;

  /* BRANDING: Top border in York Red */
  /* 品牌识别：顶部添加 York 红条 */
  border-top: 4px solid #E31837;
}

.title {
  font-size: 24px;
  /* System Font Stack for performance and readability */
  /* 系统字体栈，确保性能和可读性 */
  font-family: 'Inter', -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, sans-serif;
  text-align: center;
  margin-bottom: 30px;
  font-weight: 700;
  color: #333;
}

/* Utility Class: York University Official Red */
/* 工具类：约克大学官方红 (#E31837) */
.york-red {
  color: #E31837;
}

.button {
  width: 100px;
  margin-right: 10px;
  font-weight: 600;
}

/* * Override Element Plus Primary Button
 * 覆盖 Element Plus 默认主按钮样式
 * Forces the button to use York Red instead of default Blue.
 * 强制按钮使用 York 红，而非默认蓝色。
 */
.york-btn-primary {
  background-color: #E31837;
  border-color: #E31837;
  transition: all 0.3s;
}

.york-btn-primary:hover,
.york-btn-primary:focus {
  background-color: #B2122A; /* Darker red for hover state (悬停时加深红色) */
  border-color: #B2122A;
}

/* * Deep Selector: Input Focus State
 * 深度选择器：输入框聚焦状态
 * Changes the focus border color to match the brand.
 * 将输入框聚焦时的边框颜色改为品牌色。
 */
:deep(.el-input__wrapper.is-focus) {
  box-shadow: 0 0 0 1px #E31837 inset !important;
}
</style>

<style>
/* * Customize Element Plus Message Box
 * 定制 Element Plus 消息提示框
 * Note: These styles must be global because ElMessage is appended to the body.
 * 注意：由于 ElMessage 挂载在 body 上，样式必须是全局的。
 */
.el-message {
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.15) !important;
  border-radius: 4px !important;
  font-weight: 600 !important;
  border: none !important;
}

/* Add colored accent bars to notifications */
/* 给通知添加彩色装饰条 */
.el-message--success {
  border-left: 5px solid #67C23A !important;
}

.el-message--error {
  border-left: 5px solid #E31837 !important; /* York Red for errors */
}
</style>