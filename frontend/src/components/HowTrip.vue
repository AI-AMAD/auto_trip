<template>
  <div class="row custom-row-spacing" v-for="(date, dateIndex) in dates" :key="date.id">
    <span class="badge text-bg-secondary">{{ date.text }}</span>
    <div v-for="(item, index) in date.items" :key="item.id" class="col-3 d-flex align-items-center">
      <div
        class="draggable-item card h-100 d-flex align-items-center p-3"
        style="max-width: 85%"
        draggable="true"
        @dragstart="onDragStart(dateIndex, index)"
        @dragover.prevent
        @drop="onDrop(dateIndex, index)"
        :class="{ faded: item.isFaded }"
      >
        <img src="@/assets/img/swiss.png" class="card-img-top" />
        <div class="card-body">
          <div class="card-text">
            {{ item.text }}
            <button
              class="fade-button btn btn-sm btn-danger mt-1"
              @click="onFadeItem(dateIndex, index)"
            >
              X
            </button>
          </div>
        </div>
      </div>
      <div v-if="index < date.items.length - 1" class="arrow ms-2">→</div>
    </div>
  </div>
  <div class="text-center mt-4">
    <button class="btn btn-success" @click="onSave">저장</button>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'
import { useAuthStore } from '@/stores/auth'

const authStore = useAuthStore()
// 컴포넌트 마운트 시 데이터 가져오기
onMounted(() => {
  fetchTripData()
  console.log('tripScheduleData--------->: ', tripScheduleData)
  console.log('이거 안찍히네 startYmd--------->: ', tripScheduleData.value[0].startYmd)
})

const tripScheduleData = ref()

// 여행 스케줄 조회
const fetchTripData = () => {
  axios
    .get(`/api/schedule/${authStore.username}`, {
      headers: {
        Authorization: `Bearer ${authStore.token}`,
        'Content-Type': 'application/json'
      }
    })
    .then((response) => {
      if (response.data) {
        tripScheduleData.value = response.data
      } else {
        tripScheduleData.value = null
      }
    })
    .catch((error) => {
      console.error('여행 정보 조회 실패:', error.response?.data || error.message)
      tripScheduleData.value = null
    })
}

// dates 배열의 각 date에 독립적인 items를 포함시킴
const dates = ref([
  {
    id: 1,
    text: '1월 1일',
    items: [
      { id: 1, text: '아이템 1', isFaded: false },
      { id: 2, text: '아이템 2', isFaded: false },
      { id: 3, text: '아이템 3', isFaded: false }
    ]
  },
  {
    id: 2,
    text: '1월 2일',
    items: [
      { id: 4, text: '아이템 4', isFaded: false },
      { id: 5, text: '아이템 5', isFaded: false },
      { id: 6, text: '아이템 6', isFaded: false }
    ]
  },
  {
    id: 3,
    text: '1월 3일',
    items: [
      { id: 7, text: '아이템 7', isFaded: false },
      { id: 8, text: '아이템 8', isFaded: false },
      { id: 9, text: '아이템 9', isFaded: false }
    ]
  }
])

const draggedItemIndex = ref(null)
const draggedDateIndex = ref(null)

const onDragStart = (dateIndex, index) => {
  draggedDateIndex.value = dateIndex
  draggedItemIndex.value = index
}

const onDrop = (targetDateIndex, targetIndex) => {
  const sourceDateIndex = draggedDateIndex.value
  const sourceIndex = draggedItemIndex.value

  if (
    sourceDateIndex !== null &&
    sourceIndex !== null &&
    (sourceDateIndex !== targetDateIndex || sourceIndex !== targetIndex)
  ) {
    const movedItem = dates.value[sourceDateIndex].items[sourceIndex]
    dates.value[sourceDateIndex].items.splice(sourceIndex, 1)
    dates.value[targetDateIndex].items.splice(targetIndex, 0, movedItem)
  }

  draggedDateIndex.value = null
  draggedItemIndex.value = null
}

const onFadeItem = (dateIndex, index) => {
  dates.value[dateIndex].items[index].isFaded = !dates.value[dateIndex].items[index].isFaded
}

const onSave = () => {
  dates.value.forEach((date) => {
    date.items = date.items.filter((item) => !item.isFaded)
  })
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
</style>
