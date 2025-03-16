import { defineStore } from 'pinia'
import axios from 'axios'

export const useAuthStore = defineStore('auth', {
  state: () => ({
    token: localStorage.getItem('token') || null, // 초기값은 localStorage에서 가져옴
    nickname: localStorage.getItem('nickname') || '',
    profileImageUrl: localStorage.getItem('profileImageUrl') || ''
  }),
  actions: {
    // 로그인 시 토큰 저장
    setToken(token) {
      this.token = token
      localStorage.setItem('token', token) // 브라우저 저장소에 저장
    },
    // 유저 정보 가져오기
    async fetchUserProfile() {
      try {
        const response = await axios.get('/api/user/profile', {
          headers: {
            Authorization: `Bearer ${this.token}`
          }
        })
        this.nickname = response.data.nickname
        this.profileImageUrl = response.data.profileImageUrl

        // localStorage에 저장
        localStorage.setItem('nickname', this.nickname)
        localStorage.setItem('profileImageUrl', this.profileImageUrl)
      } catch (error) {
        console.error('유저 정보 로드 실패:', error)
      }
    },
    // 로그아웃
    logout() {
      this.token = null
      this.nickname = ''
      this.profileImageUrl = ''
      localStorage.removeItem('token')
      localStorage.removeItem('nickname')
      localStorage.removeItem('profileImageUrl')
    }
  }
})
