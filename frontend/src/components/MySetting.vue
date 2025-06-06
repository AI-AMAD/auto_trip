<template>
  <!-- 1 번째 로우: 스탭바 -->
  <div class="row mb-4">
    <div class="col-12">
      <div class="step-container">
        <router-link to="/main/setting/where" class="nav-link">
          <div
            class="step"
            :class="activeButton === 'where' ? 'btn-active' : 'btn-inactive'"
            @click="setActive('where')"
          >
            어디로?
          </div>
        </router-link>

        <div
          class="step-arrow"
          :class="activeButton === 'where' ? 'arrow-active' : 'arrow-inactive'"
        ></div>

        <!-- 세부 설정 버튼: router-link 대신 div로 변경하여 클릭 제어 -->
        <div class="nav-link">
          <div
            class="step"
            :class="activeButton === 'details' ? 'btn-active' : 'btn-inactive'"
            @click="existWhereInfo"
          >
            세부 설정!
          </div>
        </div>

        <div
          class="step-arrow"
          :class="activeButton === 'details' ? 'arrow-active' : 'arrow-inactive'"
        ></div>

        <div class="nav-link">
          <div
            class="step"
            :class="activeButton === 'how' ? 'btn-active' : 'btn-inactive'"
            @click="existTripSchedule"
          >
            어떻게 갈까?
          </div>
        </div>

        <div
          class="step-arrow"
          :class="activeButton === 'how' ? 'arrow-active' : 'arrow-inactive'"
        ></div>

        <div class="nav-link">
          <div
            class="step"
            :class="activeButton === 'hotel' ? 'btn-active' : 'btn-inactive'"
            @click="existFinalSchedule"
          >
            숙소는?
          </div>
        </div>
      </div>
    </div>
  </div>
  <!-- end of 1번째 로우 -->

  <!-- 2번째 로우 -->
  <!-- 여행 계획 설정 -->
  <div class="row">
    <div class="col-12">
      <router-view></router-view>
    </div>
  </div>
  <!-- end of 여행 계획 설정 -->
  <!-- end of 2번째 로우 -->
</template>

<script setup>
import { ref, watch } from 'vue'
import { useAuthStore } from '@/stores/auth'
import { useRouter, useRoute } from 'vue-router'
import axios from 'axios'

const authStore = useAuthStore()
const router = useRouter()
const route = useRoute()

// 현재 활성화된 버튼을 관리하는 상태
const activeButton = ref('where')

// 버튼 활성화 상태 변경
const setActive = (button) => {
  activeButton.value = button
}

// 라우트 쿼리 파라미터를 감지해서 activeButton 업데이트
watch(
  () => route.query.active,
  (newActive) => {
    if (newActive) {
      activeButton.value = newActive
    }
  },
  { immediate: true } // 페이지 로드 시 즉시 실행
)

const existWhereInfo = () => {
  axios
    .get('/api/get/where/info', {
      params: { username: authStore.username },
      headers: {
        Authorization: `Bearer ${authStore.token}`,
        'Content-Type': 'application/json'
      }
    })
    .then((response) => {
      if (response.data) {
        // 장소가 설정된 경우 세부 설정 화면으로 이동
        setActive('details')
        router.push('/main/setting/details')
      } else {
        alert('장소를 설정하고 진행해주세요!')
      }
    })
}

// tripschedule 정보 확인
const existTripSchedule = () => {
  axios
    .get('/api/get/tripschedule/info', {
      params: { username: authStore.username },
      headers: {
        Authorization: `Bearer ${authStore.token}`,
        'Content-Type': 'application/json'
      }
    })
    .then((response) => {
      if (response.data) {
        setActive('how')
        router.push('/main/setting/how')
      } else {
        alert('여행 계획을 설정하고 진행해주세요!')
      }
    })
}

// schedule 최종 확정 여부 확인
const existFinalSchedule = () => {
  axios
    .get('/api/get/final/info', {
      params: { username: authStore.username },
      headers: {
        Authorization: `Bearer ${authStore.token}`,
        'Content-Type': 'application/json'
      }
    })
    .then((response) => {
      if (response.data) {
        setActive('hotel')
        router.push('/main/setting/hotel')
      } else {
        alert('여행 계획을 최종 저장 후 진행해주세요!')
      }
    })
}
</script>

<style scoped>
.step-container {
  display: flex;
  align-items: center;
  justify-content: flex-start;
}

.step {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 10px 20px;
  font-size: 14px;
  font-weight: bold;
  border-radius: 30px;
  color: white;
  background-color: #5bc0be;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.step-arrow {
  width: 0;
  height: 0;
  border-top: 15px solid transparent;
  border-bottom: 15px solid transparent;
  border-left: 15px solid #4aa8d8;
}

.arrow-active {
  border-left-color: #4aa8d8; /* 활성화 색상 */
}

.arrow-inactive {
  border-left-color: #e0e0e0; /* 비활성화 색상 */
}

.btn-active {
  background-color: #4aa8d8; /* 활성화 색상 */
  color: white;
}

.btn-inactive {
  background-color: #e0e0e0; /* 비활성화 색상 */
  color: #6c757d;
}
</style>
