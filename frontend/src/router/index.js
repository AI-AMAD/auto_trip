import { createRouter, createWebHistory } from 'vue-router'
import LoginView from '@/views/LoginView.vue'
import MainPage from '@/views/MainPage.vue'
import SignUp from '@/components/SignUp.vue'
import MySetting from '@/components/MySetting.vue'
import WhereTrip from '@/components/WhereTrip.vue'
import WhenTrip from '@/components/WhenTrip.vue'
import WhatTrip from '@/components/WhatTrip.vue'
import WhoTrip from '@/components/WhoTrip.vue'
import HowTrip from '@/components/HowTrip.vue'
import HotelTrip from '@/components/HotelTrip.vue'
import MainManual from '@/components/MainManual.vue'
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
      path: '/signup',
      name: 'signup',
      component: SignUp
    },
    {
      path: '/main',
      name: 'mainpage',
      component: MainPage,
      children: [
        {
          path: 'manual',
          component: MainManual
        },
        {
          path: 'setting',
          component: MySetting,
          children: [
            {
              path: 'where',
              component: WhereTrip
            },
            {
              path: 'when',
              component: WhenTrip
            },
            {
              path: 'who',
              component: WhoTrip
            },
            {
              path: 'what',
              component: WhatTrip
            },
            {
              path: 'how',
              component: HowTrip
            },
            {
              path: 'hotel',
              component: HotelTrip
            }
          ]
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
