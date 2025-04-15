import 'bootstrap/dist/css/bootstrap.min.css'

import VueDatePicker from '@vuepic/vue-datepicker'
import '@vuepic/vue-datepicker/dist/main.css'
import 'bootstrap-icons/font/bootstrap-icons.css'

import { createApp } from 'vue'
import { createPinia } from 'pinia'
import { createNaverMap } from 'vue3-naver-maps'

import App from './App.vue'
import router from './router'

const app = createApp(App)

app.component('VueDatePicker', VueDatePicker)
app.use(createPinia())
app.use(router)
app.use(createNaverMap, {
  clientId: import.meta.env.VITE_NAVER_SECRET_KEY, // Required
  category: 'ncp', // Optional
  subModules: [] // Optional
})

app.mount('#app')
