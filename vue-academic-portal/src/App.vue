<script setup>
import { ElConfigProvider } from 'element-plus'
// 导入 Element Plus 的英文语言包
import en from 'element-plus/dist/locale/en.mjs'
</script>

<template>
  <el-config-provider :locale="en">
    <router-view></router-view>
  </el-config-provider>
</template>

<style scoped>
</style>