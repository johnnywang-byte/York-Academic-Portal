<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from "element-plus";
import { useRouter } from 'vue-router'

// Current logged-in user name
const loginName = ref('');

// Lifecycle hook: Retrieve user info from local storage when mounted
onMounted(() => {
  // Get current user object
  const loginUser = JSON.parse(localStorage.getItem('loginUser'));
  if (loginUser && loginUser.name) {
    loginName.value = loginUser.name;
  }
})

// Router instance
const router = useRouter();

// Logout function
const logout = () => {
  // Prompt confirmation dialog
  ElMessageBox.confirm(
    'Are you sure you want to log out?',
    'Sign Out', // More professional title
    {
      confirmButtonText: 'Confirm',
      cancelButtonText: 'Cancel',
      type: 'warning',
    }
  ).then(async () => { // User confirmed
    ElMessage.success('Logged out successfully');

    // Clear local storage
    localStorage.removeItem('loginUser');

    // Redirect to login page
    router.push('/login');

  }).catch(() => { // User cancelled
    // Optional: Log cancellation
  });
}
</script>

<template>
  <div class="common-layout">
    <el-container>
      <el-header class="header">
        <span class="title">York U | Academic Admin Portal</span>
        <span class="right_tool">
          <a href="javascript:;">
            <el-icon style="vertical-align: middle;">
              <EditPen />
            </el-icon> Change Password
          </a>
          <span class="divider">|</span>
          <a href="javascript:;" @click="logout">
            <el-icon style="vertical-align: middle;">
              <SwitchButton />
            </el-icon> Logout [{{ loginName }}]
          </a>
        </span>
      </el-header>

      <el-container>
        <el-aside width="220px" class="aside">
          <el-menu router :default-active="$route.path">

            <el-menu-item index="/index">
              <el-icon><Promotion /></el-icon>Dashboard
            </el-menu-item>

            <el-menu-item index="/ai">
              <el-icon><MagicStick /></el-icon>
              <span>AI Assistant</span>
            </el-menu-item>

            <el-sub-menu index="/manage">
              <template #title>
                <el-icon><Menu /></el-icon>Academic Admin
              </template>
              <el-menu-item index="/clazz"><el-icon><HomeFilled /></el-icon>Course Mgmt</el-menu-item>
              <el-menu-item index="/stu"><el-icon><UserFilled /></el-icon>Undergrad Records</el-menu-item>
            </el-sub-menu>

            <el-sub-menu index="/system">
              <template #title>
                <el-icon><Tools /></el-icon>University Admin
              </template>
              <el-menu-item index="/dept"><el-icon><HelpFilled /></el-icon>Faculties</el-menu-item>
              <el-menu-item index="/emp"><el-icon><Avatar /></el-icon>Faculty & Staff</el-menu-item>
            </el-sub-menu>

            <el-sub-menu index="/report">
              <template #title>
                <el-icon><Histogram /></el-icon>Institutional Analytics
              </template>
              <el-menu-item index="/empReport"><el-icon><InfoFilled /></el-icon>Faculty Stats</el-menu-item>
              <el-menu-item index="/stuReport"><el-icon><Share /></el-icon>Student Demographics</el-menu-item>
              <el-menu-item index="/log"><el-icon><Document /></el-icon>System Logs</el-menu-item>
            </el-sub-menu>
          </el-menu>
        </el-aside>

        <el-main class="main-content">
          <router-view></router-view>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<style scoped>
/* York University Red Theme */
.header {
  /* Official York Red is #E31837. Added a subtle gradient to #B31B1B for depth */
  background: linear-gradient(90deg, #E31837 0%, #B31B1B 100%);
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 25px;
  height: 60px;
  box-shadow: 0 2px 5px rgba(0,0,0,0.1);
}

.title {
  color: white;
  font-size: 22px; /* More professional size */
  font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Arial, sans-serif;
  font-weight: 700; /* Bold for impact */
  letter-spacing: 0.5px;
  display: flex;
  align-items: center;
}

.right_tool {
  display: flex;
  align-items: center;
}

a {
  color: rgba(255, 255, 255, 0.9);
  text-decoration: none;
  font-size: 14px;
  transition: opacity 0.3s;
  display: flex;
  align-items: center;
  gap: 5px;
}

a:hover {
  opacity: 0.8;
  text-decoration: none;
}

.divider {
  color: rgba(255,255,255,0.4);
  margin: 0 15px;
}

.aside {
  width: 230px; /* Slightly wider for longer English text */
  border-right: 1px solid #e0e0e0;
  height: calc(100vh - 60px); /* Fill remaining height */
  background-color: #ffffff;
}

.el-menu {
  border-right: none;
}

/* Optional: Make the active menu item red to match the brand */
:deep(.el-menu-item.is-active) {
  color: #E31837;
  background-color: #fff1f2;
  border-right: 3px solid #E31837;
}

.main-content {
  background-color: #f5f7fa; /* Light grey background for content area */
  height: calc(100vh - 60px);
}
</style>