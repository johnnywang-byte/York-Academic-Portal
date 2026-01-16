<script setup>
</script>

<template>
  <div class="banner-container">
    <img src="../../assets/index.png" class="responsive-img">
  </div>
</template>

<style scoped>
.banner-container {
  /* 设置容器高度：85vh 代表占屏幕高度的 85% (减去顶栏的大概高度) */
  height: 85vh;
  width: 100%;
  overflow: hidden; /* 防止图片溢出 */
  border-radius: 8px; /* 可选：加一点圆角 */
}

.responsive-img {
  width: 100%;
  height: 100%;
  /* 核心代码：让图片保持比例填满容器，多余部分自动裁剪，不会变形 */
  object-fit: cover;
  object-position: center; /*以此为中心裁剪 */
  display: block;
}
</style>