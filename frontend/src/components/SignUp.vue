<template>
  <body class="bg-gradient-primary">
    <div class="container">
      <div class="card o-hidden border-0 shadow-lg my-5">
        <div class="card-body p-0">
          <!-- Nested Row within Card Body -->
          <div class="row">
            <div class="col-lg-5 d-none d-lg-block bg-register-image"></div>
            <div class="col-lg-7">
              <div class="p-5">
                <div class="text-center">
                  <h1 class="h4 text-gray-900 mb-4">Create an Account!</h1>
                </div>
                <form @submit.prevent="register" class="user">
                  <div class="form-group row">
                    <div class="col-sm-8 mb-3 mb-sm-0">
                      <!-- Nickname -->
                      <input
                        v-model="nickname"
                        type="text"
                        class="form-control form-control-user"
                        id="exampleName"
                        placeholder="Name"
                        required
                      />
                    </div>
                  </div>
                  <div class="form-group row">
                    <div class="col-sm-8 mb-3 mb-sm-0">
                      <!-- ID -->
                      <input
                        v-model="username"
                        type="text"
                        class="form-control form-control-user"
                        id="exampleInputId"
                        placeholder="ID"
                        required
                      />
                    </div>
                  </div>
                  <div class="form-group row">
                    <div class="col-sm-6 mb-3 mb-sm-0">
                      <!-- Password -->
                      <input
                        v-model="password"
                        type="password"
                        class="form-control form-control-user"
                        id="exampleInputPassword"
                        placeholder="Password"
                        required
                      />
                    </div>
                    <div class="col-sm-6">
                      <!-- confirmPassword -->
                      <input
                        v-model="confirmPassword"
                        type="password"
                        class="form-control form-control-user"
                        id="exampleRepeatPassword"
                        placeholder="Repeat Password"
                        required
                      />
                    </div>
                  </div>
                  <button type="submit" class="btn btn-primary btn-user btn-block">
                    회원 가입
                  </button>
                  <hr />
                  <button @click="kakaoLogin" class="btn btn-warning btn-user btn-block">
                    <div class="kakao-login"><i class="fas fa-sign-in-alt"></i>카카오 로그인</div>
                  </button>
                </form>
                <hr />
                <div class="text-center">
                  <router-link to="/" class="small"
                    >이미 계정이 있으시다면 로그인을 진행해주세요!
                  </router-link>
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
import { ref } from 'vue'
import axios from 'axios'
import { useRouter } from 'vue-router'

const username = ref('')
const password = ref('')
const confirmPassword = ref('')
const nickname = ref('')
const router = useRouter()

const register = async () => {
  if (password.value !== confirmPassword.value) {
    alert('비밀번호가 일치하지 않습니다.')
    return
  }

  try {
    const response = await axios.post('/api/signup', {
      nickname: nickname.value,
      username: username.value,
      password: password.value
    })

    if (response.status === 200) {
      alert('회원가입 성공!')
      // 성공 후 추가적인 처리 (예: 로그인 페이지로 이동)
      router.push('/')
    }
  } catch (error) {
    if (error.response && error.response.data && error.response.data.message) {
      alert(error.response.data.message) // "이미 가입되어 있는 유저입니다."
    } else {
      alert('회원가입 중 오류가 발생했습니다.')
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

<style scoped>
.container {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100vh;
}

.card {
  width: 80%;
}

.kakao-login i {
  margin-right: 8px;
}
</style>
