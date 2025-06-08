<template>
  <body class="bg-gradient-primary">
    <div class="container">
      <!-- Outer Row -->
      <div class="row justify-content-center">
        <div class="col-xl-10 col-lg-12 col-md-9">
          <div class="card o-hidden border-0 shadow-lg my-5">
            <div class="card-body p-0">
              <!-- Nested Row within Card Body -->
              <div class="row">
                <div class="col-lg-6 d-none d-lg-block bg-login-image"></div>
                <div class="col-lg-6">
                  <div class="p-5">
                    <div class="text-center">
                      <h1 class="h4 text-gray-900 mb-4">Welcome Back!</h1>
                    </div>
                    <form @submit.prevent="login" class="user">
                      <div class="form-group">
                        <input
                          v-model="username"
                          type="text"
                          class="form-control form-control-user"
                          id="exampleInputId"
                          placeholder="ID"
                          required
                        />
                      </div>
                      <div class="form-group">
                        <input
                          v-model="password"
                          type="password"
                          class="form-control form-control-user"
                          id="exampleInputPassword"
                          placeholder="Password"
                          required
                        />
                      </div>
                      <div class="form-group">
                        <div class="custom-control custom-checkbox small">
                          <input type="checkbox" class="custom-control-input" id="customCheck" />
                          <label class="custom-control-label" for="customCheck">Remember Me</label>
                        </div>
                      </div>
                      <button type="submit" class="btn btn-primary btn-user btn-block">
                        로그인
                      </button>
                      <hr />
                      <button @click="kakaoLogin" class="btn btn-warning btn-user btn-block">
                        <i class="fab fa-google fa-fw"></i> 카카오 로그인
                      </button>
                    </form>
                    <hr />
                    <div class="text-center">
                      <a class="small" href="forgot-password.html"
                        >비밀번호가 기억나지 않으실때는?</a
                      >
                    </div>
                    <div class="text-center">
                      <router-link to="/signup" class="small">회원가입은 여기에서!</router-link>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </body>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'
import { useRouter, useRoute } from 'vue-router'
import { useAuthStore } from '@/stores/auth'

const username = ref('')
const password = ref('')
const router = useRouter()
const route = useRoute()
const authStore = useAuthStore() // Pinia 스토어 사용

// onMounted 역시 비동기 처리 -> 바로 fetchUserProfile 함수 호출하니 정보 가져오기도 전에 /main/manual로 router.push 됨
onMounted(async () => {
  const token = route.query.token
  if (token) {
    authStore.setToken(token)
    try {
      await authStore.fetchUserProfile()
      console.log('Nickname:', authStore.nickname) // 디버깅
      router.push('/main/manual')
    } catch (error) {
      console.error('Profile fetch failed:', error)
      alert('유저 정보를 가져오지 못했습니다.')
    }
  }
})

const login = async () => {
  try {
    const response = await axios.post('/api/login', {
      username: username.value,
      password: password.value
    })

    if (response.status === 200) {
      const token = response.data.token
      console.log('token------>: ', token)

      // Pinia에 토큰 저장
      authStore.setToken(token)
      // 유저 정보 요청
      await authStore.fetchUserProfile()

      alert('로그인 성공!')
      // 성공 후 추가적인 처리 (예: 로그인 페이지로 이동)
      router.push('/main/manual')
    }
  } catch (error) {
    if (error.response && error.response.data && error.response.data.message) {
      alert(error.response.data.message)
    } else {
      console.log('response.status------>: ', error.response.status)
      alert('로그인 중 오류 발생.')
    }
  }
}

const kakaoLogin = () => {
  const clientId = import.meta.env.VITE_KAKAO_CLIENT_ID
  const redirectUri = import.meta.env.VITE_KAKAO_REDIRECT_URI
  console.log('clientId------>: ', clientId)
  console.log('redirectUri-------->: ', redirectUri)
  const kakaoAuthUrl = `https://kauth.kakao.com/oauth/authorize?client_id=${clientId}&redirect_uri=${redirectUri}&response_type=code`

  // 브라우저를 카카오 로그인 페이지로 리다이렉트
  window.location.href = kakaoAuthUrl
}
</script>

<style lang="scss" scoped></style>
