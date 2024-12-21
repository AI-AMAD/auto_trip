import { createRouter, createWebHistory } from 'vue-router'
import LoginView from '@/views/LoginView.vue'
import MainPage from '@/views/MainPage.vue'
import MySetting from '@/components/MySetting.vue'
import MyPlan from '@/components/MyPlan.vue'
import MySchedule from '@/components/MySchedule.vue'
import MyDiary from '@/components/MyDiary.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'login',
      component: LoginView
    },
    {
      path: '/main',
      name: 'mainpage',
      component: MainPage,
      children: [
        {
          path: 'setting',
          component: MySetting
        },
        {
          path: 'plan',
          component: MyPlan
        },
        {
          path: 'schedule',
          component: MySchedule
        },
        {
          path: 'diary',
          component: MyDiary
        }
      ]
    }
  ]
})

export default router
