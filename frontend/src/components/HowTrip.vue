<template>
  <div
    class="row custom-row-spacing"
    v-for="(schedule, scheduleIndex) in tripSchedule"
    :key="schedule.id"
  >
    <span class="badge text-bg-secondary">{{ schedule.text }}</span>
    <div
      v-for="(item, index) in schedule.items"
      :key="item.id"
      class="col-3 d-flex align-items-center"
    >
      <div
        class="draggable-item card h-100 d-flex align-items-center p-3"
        style="max-width: 85%"
        draggable="true"
        @dragstart="onDragStart(scheduleIndex, index)"
        @dragover.prevent
        @drop="onDrop(scheduleIndex, index)"
        :class="{ faded: item.isFaded }"
      >
        <!-- 동적으로 activityImageUrl 사용 -->
        <img
          :src="item.imgUrl || '@/assets/img/swiss.png'"
          class="card-img-top"
          alt="Activity Image"
        />
        <div class="card-body">
          <div class="card-text">
            <strong>{{ item.name }}</strong
            ><br />
            <small>{{ item.address }}</small>
            <button
              class="fade-button btn btn-sm btn-danger mt-1"
              @click="onFadeItem(scheduleIndex, index)"
            >
              X
            </button>
          </div>
        </div>
      </div>
      <div v-if="index < schedule.items.length - 1" class="arrow ms-2">→</div>
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

const authStore = useAuthStore()
const tripScheduleData = ref([])

// 날짜 포맷팅 함수
const formatDate = (dateStr) => {
  if (!dateStr) return ''
  const year = dateStr.slice(0, 4)
  const month = dateStr.slice(4, 6)
  const day = dateStr.slice(6, 8)
  return `${year}년 ${parseInt(month)}월 ${parseInt(day)}일`
}

// tripSchedule 배열 (서버 데이터를 가공하여 생성)
const tripSchedule = ref([])

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

onMounted(async () => {
  await fetchTripData()
})

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
  background-color: #f3f3f3;
  border: 1px solid #ddd;
  border-radius: 8px;
  cursor: grab;
  transition: opacity 0.3s ease, background-color 0.3s ease;
}

.draggable-item:active {
  cursor: grabbing;
  background-color: #e0e0e0;
}

.draggable-item.faded {
  opacity: 0.5;
  background-color: #f0f0f0;
}

.fade-button {
  font-size: 14px;
}

.fade-button:hover {
  background-color: #b21f2d;
}

.arrow {
  font-size: 24px;
  color: #6c757d;
  margin: 0 10px;
}

.custom-row-spacing {
  margin-bottom: 50px; /* 원하는 크기로 설정 */
}

.card-img-top {
  max-height: 150px;
  object-fit: cover;
}

.card-text strong {
  font-size: 1.1rem;
}

.card-text small {
  font-size: 0.85rem;
  color: #6c757d;
}
</style>
