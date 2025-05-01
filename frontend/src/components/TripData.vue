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
            <button class="btn btn-primary" @click="planTrip">여행계획짜기</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed, watch } from 'vue'

const props = defineProps({
  tripData: Object
})

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

// 활동이 하나라도 선택되었는지 확인
const hasActivities = computed(() => {
  return !!props.tripData && selectedActivities.value.length > 0
})

// 여행계획짜기 버튼 클릭
const planTrip = () => {
  alert('여행 계획을 준비 중입니다!') // TODO: 실제 로직 연결
}
</script>

<style scoped lang="scss"></style>
