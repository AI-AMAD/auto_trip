<template>
  <div>
    <div v-if="!tripData" class="text-center">
      <p>여행 데이터를 로딩 중입니다...</p>
    </div>
    <div v-else class="card shadow mb-4">
      <div class="card-header py-3">
        <h6 class="m-0 font-weight-bold text-primary">여행 설정 요약</h6>
      </div>
      <div class="card-body">
        <div class="table-responsive">
          <table class="table table-bordered" width="100%" cellspacing="0">
            <tbody>
              <tr>
                <td class="text-center align-middle" width="12%">언제?</td>
                <td>{{ formatDate(tripData.startYmd) }} ~ {{ formatDate(tripData.endYmd) }}</td>
              </tr>
              <tr>
                <td class="text-center align-middle">어디로?</td>
                <td>{{ tripData.place || '미설정' }}</td>
              </tr>
              <tr>
                <td class="text-center align-middle">무엇을?</td>
                <td>
                  <span v-for="(activity, index) in selectedActivities" :key="index">
                    {{ activity }}{{ index < selectedActivities.length - 1 ? ', ' : '' }}
                  </span>
                  <span v-if="!selectedActivities.length">미설정</span>
                </td>
              </tr>
            </tbody>
          </table>
          <div class="d-flex justify-content-end">
            <button
              class="btn"
              :class="{ 'btn-primary': !isPlanning, 'btn-warning': isPlanning }"
              @click="planTrip"
              :disabled="isPlanning"
            >
              <span
                v-if="isPlanning"
                class="spinner-border spinner-border-sm mr-2"
                role="status"
                aria-hidden="true"
              ></span>
              {{ isPlanning ? '여행 계획 생성 중...' : '여행계획짜기' }}
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, watch } from 'vue'
import { useAuthStore } from '@/stores/auth'
import axios from 'axios'
import { useRouter } from 'vue-router'

const authStore = useAuthStore()
const router = useRouter()

const props = defineProps({
  tripData: Object
})

// 로딩 상태 관리
const isPlanning = ref(false)

// tripData 변경 감지 및 디버깅
watch(
  () => props.tripData,
  (newTripData) => {
    console.log('TripData.vue - tripData updated:', newTripData)
  },
  { immediate: true }
)

// YYYYMMDD를 YYYY/MM/DD로 변환
const formatDate = (ymd) => {
  if (!ymd || ymd.length !== 8) return '미설정'
  const year = ymd.slice(0, 4)
  const month = ymd.slice(4, 6)
  const day = ymd.slice(6, 8)
  return `${year}/${month}/${day}`
}

// 선택된 활동 목록 생성
const selectedActivities = computed(() => {
  const activities = []
  if (props.tripData?.activity) activities.push('활동적인')
  if (props.tripData?.museum) activities.push('박물관')
  if (props.tripData?.cafe) activities.push('분위기 좋은 카페')
  if (props.tripData?.tourAtt) activities.push('관광 명소')
  return activities
})

// 여행계획짜기 버튼 클릭
const planTrip = async () => {
  if (!authStore.username || !authStore.token) {
    alert('로그인이 필요합니다.')
    return
  }

  const tripSummaryDto = {
    username: authStore.username,
    place: props.tripData.place,
    startYmd: props.tripData.startYmd,
    endYmd: props.tripData.endYmd,
    activity: props.tripData.activity,
    museum: props.tripData.museum,
    cafe: props.tripData.cafe,
    tourAtt: props.tripData.tourAtt
  }

  isPlanning.value = true
  try {
    await axios.post('/api/auto/plan', tripSummaryDto, {
      headers: {
        Authorization: `Bearer ${authStore.token}`,
        'Content-Type': 'application/json'
      }
    })
    alert('여행 계획이 성공적으로 생성되었습니다.')
    router.push({ path: '/main/setting/how', query: { active: 'how' } })
  } catch (error) {
    alert('여행 계획 생성에 실패했습니다: ' + (error.response?.data || error.message))
  } finally {
    isPlanning.value = false
  }
}
</script>

<style scoped>
/* 버튼 전환 애니메이션 */
.btn {
  transition: background-color 0.3s ease, border-color 0.3s ease, color 0.3s ease;
}

/* 스피너와 텍스트 정렬 */
.btn .spinner-border {
  vertical-align: middle;
  margin-right: 8px; /* Bootstrap의 mr-2와 동일 */
}
</style>
