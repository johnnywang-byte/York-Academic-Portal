import { createRouter, createWebHistory } from 'vue-router'
import IndexView from '@/views/index/index.vue'
import clazzView from '@/views/clazz/index.vue'
import DeptView from '@/views/dept/index.vue'
import EmpView from '@/views/emp/index.vue'
import LogView from '@/views/log/index.vue'
import stuView from '@/views/stu/index.vue'
import EmpReportView from '@/views/report/emp/index.vue'
import stuReportView from '@/views/report/stu/index.vue'
import LayoutView from '@/views/layout/index.vue'
import LoginView from '@/views/login/index.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: '',
      component: LayoutView, // 唯一的父级 Layout
      redirect: '/index',
      children: [
        { path: 'index', name: 'index', component: IndexView },
        { path: 'clazz', name: 'clazz', component: clazzView },
        { path: 'dept', name: 'dept', component: DeptView },
        { path: 'emp', name: 'emp', component: EmpView },
        { path: 'log', name: 'log', component: LogView },
        { path: 'stu', name: 'stu', component: stuView },
        { path: 'stuReport', name: 'stuReport', component: stuReportView },
        { path: 'empReport', name: 'empReport', component: EmpReportView },

        {
          path: 'ai',       // 访问路径: /ai
          name: 'AIAssistant',
          component: () => import('../views/ai/index.vue'),
          meta: {
            title: 'AI Assistant',
            icon: 'el-icon-magic-stick'
          }
        }
      ]
    },
    { path: '/login', name: 'login', component: LoginView }
  ]
})

export default router