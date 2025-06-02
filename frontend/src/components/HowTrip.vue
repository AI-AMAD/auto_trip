<template>
  <div
    class="custom-row-spacing"
    v-for="(schedule, scheduleIndex) in tripSchedule"
    :key="schedule.id"
  >
    <!-- 날짜를 왼쪽 정렬로 배치 -->
    <div class="date-header text-start mb-3">
      <span class="badge text-bg-secondary">{{ schedule.text }}</span>
    </div>
    <!-- 카드들을 수평으로 배치, 필요 시 줄바꿈 -->
    <div class="d-flex flex-row flex-wrap align-items-center justify-content-start card-container">
      <div v-for="(item, index) in schedule.items" :key="item.id" class="d-flex align-items-center">
        <div
          class="draggable-item card d-flex align-items-center p-3"
          style="max-width: 90%"
          draggable="true"
          @dragstart="onDragStart(scheduleIndex, index)"
          @dragover.prevent
          @drop="onDrop(scheduleIndex, index)"
          :class="{ faded: item.isFaded }"
        >
          <!-- 동적으로 activityImageUrl 사용 -->
          <img
            :src="item.imgUrl"
            class="card-img-top"
            alt="Activity Image"
            @error="handleImageError"
          />
          <div class="card-body">
            <div class="card-text">
              <strong>{{ item.name }}</strong
              ><br />
              <small class="mb-2">{{ item.address }}</small>
              <button
                class="fade-button btn btn-sm btn-danger mt-1"
                @click="onFadeItem(scheduleIndex, index)"
              >
                X
              </button>
            </div>
          </div>
        </div>
        <div v-if="index < schedule.items.length - 1" class="arrow mx-2">→</div>
      </div>
    </div>
  </div>
  <div class="text-center mt-4">
    <button class="btn btn-success" @click="onSave">저장</button>
  </div>
  <div v-if="!tripSchedule.length" class="text-center mt-4">여행 일정이 없습니다.</div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'
import { useAuthStore } from '@/stores/auth'

onMounted(async () => {
  await fetchTripData()
})

// tripSchedule 배열 (서버 데이터를 가공하여 생성)
const tripSchedule = ref([])
const tripScheduleData = ref([])

// 서버 데이터를 tripSchedule에 반영
const updateTripSchedule = () => {
  const scheduleList = []

  // 시작 날짜와 활동
  if (
    tripScheduleData.value.length > 0 &&
    tripScheduleData.value[0].startYmd &&
    Object.keys(tripScheduleData.value[0].startYmd).length > 0
  ) {
    const startKey = Object.keys(tripScheduleData.value[0].startYmd)[0]
    scheduleList.push({
      id: `schedule-1`,
      text: formatDate(startKey),
      items: (tripScheduleData.value[0].startYmd[startKey] || []).map((activity, index) => ({
        id: `start-${index}`,
        name: activity.activityName,
        address: activity.activityAddress,
        imgUrl: activity.activityImageUrl,
        isFaded: false
      }))
    })
  }

  // 종료 날짜와 활동
  if (
    tripScheduleData.value.length > 0 &&
    tripScheduleData.value[0].endYmd &&
    Object.keys(tripScheduleData.value[0].endYmd).length > 0
  ) {
    const endKey = Object.keys(tripScheduleData.value[0].endYmd)[0]
    scheduleList.push({
      id: `schedule-2`,
      text: formatDate(endKey),
      items: (tripScheduleData.value[0].endYmd[endKey] || []).map((activity, index) => ({
        id: `end-${index}`,
        name: activity.activityName,
        address: activity.activityAddress,
        imgUrl: activity.activityImageUrl,
        isFaded: false
      }))
    })
  }

  // 미래 확장: 추가 날짜 (dailyActivities) 처리
  /*
  if (tripScheduleData.value[0].dailyActivities) {
    tripScheduleData.value[0].dailyActivities.forEach((day, index) => {
      const dayKey = Object.keys(day)[0];
      scheduleList.push({
        id: `schedule-${index + 3}`,
        text: formatDate(dayKey),
        items: (day[dayKey] || []).map((activity, idx) => ({
          id: `day${index}-${idx}`,
          name: activity.activityName,
          address: activity.activityAddress,
          imgUrl: activity.activityImageUrl,
          isFaded: false
        }))
      });
    });
  }
  */

  tripSchedule.value = scheduleList
}

const authStore = useAuthStore()

// 서버 데이터 가져오기
const fetchTripData = async () => {
  try {
    const response = await axios.get(`/api/schedule/${authStore.username}`, {
      headers: {
        Authorization: `Bearer ${authStore.token}`,
        'Content-Type': 'application/json'
      }
    })
    if (response.data) {
      tripScheduleData.value = response.data
      updateTripSchedule() // 데이터를 가져온 후 tripSchedule 업데이트
    } else {
      tripScheduleData.value = []
      tripSchedule.value = []
    }
  } catch (error) {
    console.error('여행 정보 조회 실패:', error.response?.data || error.message)
    tripScheduleData.value = []
    tripSchedule.value = []
  }
}

const draggedItemIndex = ref(null)
const draggedScheduleIndex = ref(null)

const onDragStart = (scheduleIndex, index) => {
  draggedScheduleIndex.value = scheduleIndex
  draggedItemIndex.value = index
}

const onDrop = (targetScheduleIndex, targetIndex) => {
  const sourceScheduleIndex = draggedScheduleIndex.value
  const sourceIndex = draggedItemIndex.value

  if (
    sourceScheduleIndex !== null &&
    sourceIndex !== null &&
    (sourceScheduleIndex !== targetScheduleIndex || sourceIndex !== targetIndex)
  ) {
    const movedItem = tripSchedule.value[sourceScheduleIndex].items[sourceIndex]
    tripSchedule.value[sourceScheduleIndex].items.splice(sourceIndex, 1)
    tripSchedule.value[targetScheduleIndex].items.splice(targetIndex, 0, movedItem)
  }

  draggedScheduleIndex.value = null
  draggedItemIndex.value = null
}

const onFadeItem = (scheduleIndex, index) => {
  tripSchedule.value[scheduleIndex].items[index].isFaded =
    !tripSchedule.value[scheduleIndex].items[index].isFaded
}

// 대체 이미지 (프로젝트의 assets 폴더에 있는지 확인 필요)
const defaultImage = new URL('@/assets/img/swiss.png', import.meta.url).href

const handleImageError = (event) => {
  console.log('이미지 로드 실패, 대체 이미지로 변경:', defaultImage)
  event.target.src = defaultImage
}

// 날짜 포맷팅 함수
const formatDate = (dateStr) => {
  if (!dateStr) return ''
  const year = dateStr.slice(0, 4)
  const month = dateStr.slice(4, 6)
  const day = dateStr.slice(6, 8)
  return `${year}년 ${parseInt(month)}월 ${parseInt(day)}일`
}

const onSave = () => {
  // 페이드된 항목 제거
  tripSchedule.value.forEach((schedule) => {
    schedule.items = schedule.items.filter((item) => !item.isFaded)
  })

  // 서버로 저장 (예시)
  /*
  axios.post(`/api/schedule/${authStore.username}`, {
    tripId: tripScheduleData.value[0]?.tripId,
    startYmd: {
      [tripSchedule.value[0]?.text.replace(/[^0-9]/g, '')]: tripSchedule.value[0]?.items.map(item => ({
        activityName: item.name,
        activityAddress: item.address,
        activityImageUrl: item.imgUrl
      }))
    },
    endYmd: {
      [tripSchedule.value[1]?.text.replace(/[^0-9]/g, '')]: tripSchedule.value[1]?.items.map(item => ({
        activityName: item.name,
        activityAddress: item.address,
        activityImageUrl: item.imgUrl
      }))
    }
  }, {
    headers: {
      Authorization: `Bearer ${authStore.token}`,
      'Content-Type': 'application/json'
    }
  }).then(response => {
    console.log('저장 성공:', response.data);
  }).catch(error => {
    console.error('저장 실패:', error.response?.data || error.message);
  });
  */
}
</script>

<style scoped>
.draggable-item {
  background-color: #ffffff;
  border: 1px solid #e0e0e0;
  border-radius: 12px;
  cursor: grab;
  transition: all 0.3s ease;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  width: 250px;
}

.draggable-item:hover {
  box-shadow: 0 6px 12px rgba(0, 0, 0, 0.15);
  transform: translateY(-2px);
}

.draggable-item:active {
  cursor: grabbing;
  background-color: #f8f9fa;
  transform: translateY(0);
}

.draggable-item.faded {
  opacity: 0.5;
  background-color: #f5f5f5;
}

.fade-button {
  font-size: 14px;
  border-radius: 50%;
  padding: 0.25rem 0.5rem;
}

.fade-button:hover {
  background-color: #dc3545;
  transform: scale(1.1);
}

.custom-row-spacing {
  margin-bottom: 60px;
}

.arrow {
  font-size: 24px;
  color: #6c757d;
  margin: 0 20px;
}

.card-img-top {
  width: 100%; /* 이미지 너비를 카드에 맞춤 */
  max-height: 130px; /* 높이를 조금 늘려 조정 */
  object-fit: contain; /* 이미지가 짤리지 않고 비율 유지하며 표시 */
  border-top-left-radius: 12px;
  border-top-right-radius: 12px;
}

.card-body {
  padding: 0.75rem;
}

.card-text {
  text-align: center;
}

.card-text strong {
  font-size: 1rem;
  font-weight: 600;
  color: #333;
}

.card-text small {
  font-size: 0.8rem;
  color: #6c757d;
  display: block;
  margin-top: 0.25rem;
}

.date-header {
  margin-bottom: 1.5rem;
}

.badge {
  font-size: 1.1rem;
  padding: 0.5rem 1.5rem;
  border-radius: 20px;
  background-color: #6c757d;
}

.btn-success {
  border-radius: 20px;
  padding: 0.5rem 2rem;
  font-weight: 500;
}

.card-container {
  flex-wrap: wrap;
  gap: 1rem;
  padding: 0 1rem;
}
</style>
