<template>
  <div
    class="custom-row-spacing"
    v-for="(schedule, scheduleIndex) in tripSchedule"
    :key="schedule.id"
  >
    <!-- 날짜를 왼쪽 정렬로 배치 -->
    <div class="date-header text-start mb-4">
      <span class="badge text-bg-secondary"> {{ schedule.date }} </span>
    </div>
    <!-- 카드들을 수평으로 배치, 필요 시 줄바꿈 -->
    <div class="d-flex flex-row flex-wrap align-items-center justify-content-start card-container">
      <div v-for="(item, index) in schedule.items" :key="item.id" class="d-flex align-items-center">
        <div
          class="draggable-item card d-flex align-items-center p-3 position-relative"
          draggable="true"
          @dragstart="onDragStart(scheduleIndex, index)"
          @dragover.prevent
          @drop="onDrop(scheduleIndex, index)"
          :class="{ faded: item.isFaded }"
        >
          <!-- 새로고침 버튼 (좌측 상단 외부) -->
          <button
            class="refresh-button position-absolute"
            @click="onRefreshItem(scheduleIndex, index, item.activityType)"
            :disabled="item.isRefreshing"
          >
            <i class="fas fa-sync-alt" :class="{ 'fa-spin': item.isRefreshing }"></i>
          </button>
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
              <small class="address-text">{{ item.address }}</small>
            </div>
            <!-- 변경하기 버튼 -->
          </div>
          <button
            class="fade-button btn btn-sm btn-danger position-absolute"
            @click="onFadeItem(scheduleIndex, index)"
          >
            X
          </button>
          <!-- 드래그앤드롭 문구 툴팁 -->
          <div class="drag-tooltip position-absolute">drag & drop !!!</div>
        </div>
        <div v-if="index < schedule.items.length - 1" class="arrow mx-2">→</div>
      </div>
    </div>
  </div>
  <div class="col d-flex justify-content-center mt-4 pb-3">
    <SaveButton @save="saveData"></SaveButton>
  </div>
  <div v-if="!tripSchedule.length" class="text-center mt-4 mb-5">여행 일정이 없습니다.</div>
  <!-- 토스트 메시지 -->
  <div v-if="showToast" class="toast-message">드래그앤 드롭으로 여행 일정을 수정하세요</div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import SaveButton from '@/components/SaveButton.vue'
import axios from 'axios'
import { useAuthStore } from '@/stores/auth'
import { useRouter } from 'vue-router'

const showToast = ref(false)
const router = useRouter()

onMounted(async () => {
  await fetchTripData()
  console.log('onMounted때 tripSchedule.value-----> : ', tripSchedule.value)
  showToast.value = true
  setTimeout(() => {
    showToast.value = false
  }, 3000)
})

// tripSchedule 배열 (서버 데이터를 가공하여 생성)
const tripSchedule = ref([])
const tripScheduleData = ref([])

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

// 서버 데이터를 tripSchedule에 반영
const updateTripSchedule = () => {
  const scheduleList = []
  const place = tripScheduleData.value[0].place
  // 시작 날짜와 활동
  if (
    tripScheduleData.value.length > 0 &&
    tripScheduleData.value[0].startYmd &&
    Object.keys(tripScheduleData.value[0].startYmd).length > 0
  ) {
    const startKey = Object.keys(tripScheduleData.value[0].startYmd)[0]
    scheduleList.push({
      id: `schedule-1`,
      date: formatDate(startKey),
      place: place,
      items: (tripScheduleData.value[0].startYmd[startKey] || []).map((activity, index) => ({
        id: `start-${index}`,
        scheduleId: activity.scheduleId, // scheduleId 추가
        activityType: activity.activityType, // activityType 추가
        name: activity.activityName,
        address: activity.activityAddress,
        imgUrl: activity.activityImageUrl,
        place: place,
        isFaded: false,
        isRefreshing: false
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
      date: formatDate(endKey),
      place: place,
      items: (tripScheduleData.value[0].endYmd[endKey] || []).map((activity, index) => ({
        id: `end-${index}`,
        scheduleId: activity.scheduleId, // scheduleId 추가
        activityType: activity.activityType, // activityType 추가
        name: activity.activityName,
        address: activity.activityAddress,
        imgUrl: activity.activityImageUrl,
        place: place,
        isFaded: false,
        isRefreshing: false
      }))
    })
  }

  tripSchedule.value = scheduleList
}

const draggedItemIndex = ref(null)
const draggedScheduleIndex = ref(null)

const onDragStart = (scheduleIndex, index) => {
  console.log('onDragStart 실행')
  draggedScheduleIndex.value = scheduleIndex
  draggedItemIndex.value = index
}

const onDrop = (targetScheduleIndex, targetIndex) => {
  console.log('onDrop 실행')
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
  console.log('onFadeItem 실행')
  console.log('tripSchedule.value----->: ', tripSchedule.value)
  tripSchedule.value[scheduleIndex].items[index].isFaded =
    !tripSchedule.value[scheduleIndex].items[index].isFaded
}

// 캐싱된 장소 데이터를 저장하는 ref
const cachedPlaces = ref({}) // { "scheduleIndex:index:activityType": [PlaceWithImage, ...] }

const onRefreshItem = async (scheduleIndex, index, activityType) => {
  const item = tripSchedule.value[scheduleIndex].items[index]
  if (item.isRefreshing) return // 이미 새로고침 중이면 중단
  item.isRefreshing = true // 새로고침 시작 표시

  try {
    // 캐시 키 생성 (scheduleIndex:index:activityType)
    const cacheKey = `${scheduleIndex}:${index}:${activityType}`

    // 캐싱된 데이터가 있는지 확인
    if (cachedPlaces.value[cacheKey] && cachedPlaces.value[cacheKey].length > 0) {
      // 캐싱된 데이터에서 첫 번째 장소 가져오기
      const newPlace = cachedPlaces.value[cacheKey].shift()
      console.log('Using cached place:', JSON.stringify(newPlace, null, 2))

      // UI 업데이트
      tripSchedule.value[scheduleIndex].items[index] = {
        ...item,
        name: newPlace.place.title,
        address: newPlace.place.address,
        imgUrl: newPlace.imageUrl,
        isRefreshing: false
      }

      // 캐시가 비었는지 확인
      if (cachedPlaces.value[cacheKey].length === 0) {
        delete cachedPlaces.value[cacheKey]
        console.log('Cache cleared for key:', cacheKey)
      }
    } else {
      // 캐싱된 데이터가 없으면 API 호출
      const excludedPlaces = []
      tripSchedule.value.forEach((schedule) => {
        schedule.items.forEach((place) => {
          excludedPlaces.push({
            name: place.name,
            address: place.address
          })
        })
      })

      // 백엔드로 보낼 데이터 준비
      const requestData = {
        place: item.place,
        activityType: activityType,
        excludedPlaces: excludedPlaces
      }

      // 콘솔에 요청 데이터 출력
      console.log('Request Data:', JSON.stringify(requestData, null, 2))

      // 실제 API 호출
      const response = await axios.post('/api/refresh/trip-place', requestData, {
        headers: {
          Authorization: `Bearer ${authStore.token}`,
          'Content-Type': 'application/json'
        }
      })

      // 응답 데이터 확인
      console.log('Response Data:', JSON.stringify(response.data, null, 2))

      // 응답 데이터 캐싱
      if (response.data && response.data.length > 0) {
        cachedPlaces.value[cacheKey] = response.data
        const newPlace = cachedPlaces.value[cacheKey].shift()

        // UI 업데이트
        tripSchedule.value[scheduleIndex].items[index] = {
          ...item,
          name: newPlace.place.title,
          address: newPlace.place.address,
          imgUrl: newPlace.imageUrl,
          isRefreshing: false
        }

        // 캐시가 비었는지 확인
        if (cachedPlaces.value[cacheKey].length === 0) {
          delete cachedPlaces.value[cacheKey]
          console.log('Cache cleared for key:', cacheKey)
        }
      } else {
        console.warn('새로운 장소 데이터가 없습니다.')
      }
    }
  } catch (error) {
    console.error('새로고침 실패:', error.response?.data || error.message)
  } finally {
    item.isRefreshing = false // 새로고침 완료
  }
}

// 대체 이미지 (프로젝트의 assets 폴더에 있는지 확인 필요)
const defaultImage = new URL('@/assets/img/noimage.png', import.meta.url).href

const handleImageError = (event) => {
  event.target.src = defaultImage
}

// 날짜 포맷팅 함수 '20250706' -> 2025년 07월 06일
const formatDate = (dateStr) => {
  if (!dateStr) return ''
  const year = dateStr.slice(0, 4)
  const month = dateStr.slice(4, 6)
  const day = dateStr.slice(6, 8)
  return `${year}년 ${parseInt(month)}월 ${parseInt(day)}일`
}

// 서버 저장 형식으로 날짜 변환 2025년07월06일 -> '20250706'
const parseDate = (dateStr) => {
  if (!dateStr || typeof dateStr !== 'string') {
    console.error('유효하지 않은 dateStr:', dateStr)
    return null
  }
  const parts = dateStr.split(' ')
  if (parts.length === 3) {
    const year = parts[0].replace('년', '')
    const month = parts[1].replace('월', '').padStart(2, '0')
    const day = parts[2].replace('일', '').padStart(2, '0')
    return `${year}${month}${day}` // "20250624" 형식 반환
  }
  console.error('잘못된 날짜 형식:', dateStr)
  return null
}

// 변경 사항 저장
const saveData = async () => {
  // 확인 팝업
  const isConfirmed = window.confirm('여행계획을 최종적으로 저장하시겠습니까?')
  if (!isConfirmed) return

  // 페이드된 항목 제거
  tripSchedule.value.forEach((schedule) => {
    schedule.items = schedule.items.filter((item) => !item.isFaded)
  })

  // 클라이언트 데이터를 서버 형식으로 변환
  const updatedSchedules = []
  tripSchedule.value.forEach((schedule, scheduleIndex) => {
    const date = parseDate(schedule.date)
    schedule.items.forEach((item, index) => {
      updatedSchedules.push({
        tripId: tripScheduleData.value[0].tripId,
        startYmd: scheduleIndex === 0 ? date : '',
        endYmd: scheduleIndex === 1 ? date : '',
        activityOrder: index + 1,
        activityType: item.activityType || '',
        activityName: item.name || '',
        activityAddress: item.address || '',
        activityImageUrl: item.imgUrl || ''
      })
    })
  })

  console.log('updatedSchedules:', JSON.stringify(updatedSchedules, null, 2))

  // 서버 요청
  try {
    // 기존 일정 삭제
    await axios.delete(`/api/schedule/${authStore.username}/${tripScheduleData.value[0].tripId}`, {
      headers: {
        Authorization: `Bearer ${authStore.token}`,
        'Content-Type': 'application/json'
      }
    })

    // 새로운 일정 삽입
    await axios
      .post(
        `/api/schedule/${authStore.username}/${tripScheduleData.value[0].tripId}`,
        updatedSchedules,
        {
          headers: {
            Authorization: `Bearer ${authStore.token}`,
            'Content-Type': 'application/json'
          }
        }
      )
      .then(() => {
        alert('설정이 성공적으로 저장되었습니다.')
        fetchTripData()
        router.push({ path: '/main/setting/hotel', query: { active: 'hotel' } })
      })
      .catch((error) => {
        alert('설정 저장에 실패했습니다: ' + (error.response?.data || error.message))
      })

    await fetchTripData() // 최신 데이터로 갱신
  } catch (error) {
    console.error('저장 실패:', error.response?.data || error.message)
  }
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
  width: 250px; /* 고정 너비 */
  height: 320px; /* 고정 높이 */
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  position: relative;
}

.draggable-item:hover {
  box-shadow: 0 6px 12px rgba(0, 0, 0, 0.15);
  transform: translateY(-2px);
  animation: shake 0.5s ease-in-out infinite; /* 흔들림 애니메이션 추가 */
}

/* 드래그앤드롭 툴팁 스타일 */
.drag-tooltip {
  position: absolute;
  top: 10px;
  left: 50%;
  transform: translateX(-50%);
  background-color: rgba(0, 0, 0, 0.75);
  color: #ffffff;
  font-size: 0.75rem;
  padding: 0.25rem 0.5rem;
  border-radius: 4px;
  opacity: 0;
  z-index: 10;
  pointer-events: none; /* 툴팁 클릭 방지 */
  white-space: nowrap;
}

/* 카드 호버 시 툴팁 애니메이션 */
.draggable-item:hover .drag-tooltip {
  animation: fadeInOut 2s ease-in-out infinite;
}

/* 새로고침 버튼 스타일 */
.refresh-button {
  background-color: #ffd700; /* 노란색 */
  border: none;
  color: #333;
  font-size: 16px;
  border-radius: 50%;
  padding: 0.25rem 0.5rem;
  width: 30px;
  height: 30px;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.2);
  transition: transform 0.3s ease;
  position: absolute;
  top: -13px;
  left: -13px;
  z-index: 10;
}

.refresh-button:hover {
  transform: scale(1.1);
}

.refresh-button:disabled {
  background-color: #ccc;
  cursor: not-allowed;
}

/* 툴팁 페이드인/아웃 애니메이션 정의 */
@keyframes fadeInOut {
  0% {
    opacity: 0;
    transform: translateX(-50%) translateY(0);
  }
  20% {
    opacity: 1;
    transform: translateX(-50%) translateY(-5px);
  }
  80% {
    opacity: 1;
    transform: translateX(-50%) translateY(-5px);
  }
  100% {
    opacity: 0;
    transform: translateX(-50%) translateY(0);
  }
}

/* 토스트 메시지 스타일 */
.toast-message {
  position: fixed;
  top: 10vh;
  left: 40%;
  transform: translateX(-50%);
  background-color: rgba(0, 0, 0, 0.8);
  color: #ffffff;
  padding: 0.75rem 1.5rem;
  border-radius: 4px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.2);
  z-index: 1000;
  animation: toastFade 3s ease-out forwards, shake 0.5s ease-in-out infinite; /* 흔들림 효과 추가 */
}

/* 토스트 애니메이션 */
@keyframes toastFade {
  0% {
    opacity: 0;
    transform: translateX(-50%) translateY(-10px);
  }
  10% {
    opacity: 1;
    transform: translateX(-50%) translateY(0);
  }
  90% {
    opacity: 1;
    transform: translateX(-50%) translateY(0);
  }
  100% {
    opacity: 0;
    transform: translateX(-50%) translateY(-10px);
  }
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
  font-size: 16px;
  border-radius: 50%;
  padding: 0.25rem 0.5rem;
  width: 30px; /* 버튼 너비 고정 */
  height: 30px; /* 버튼 높이 고정 */
  position: absolute;
  top: -13px;
  right: -13px;
}

.fade-button:hover {
  background-color: #dc3545;
  transform: scale(1.1);
}

/* 흔들림 애니메이션 정의 */
@keyframes shake {
  0% {
    transform: translateY(-2px) rotate(0deg);
  }
  25% {
    transform: translateY(-2px) rotate(1deg);
  }
  50% {
    transform: translateY(-2px) rotate(0deg);
  }
  75% {
    transform: translateY(-2px) rotate(-1deg);
  }
  100% {
    transform: translateY(-2px) rotate(0deg);
  }
}

.custom-row-spacing {
  margin-bottom: 60px;
}

.arrow {
  font-size: 33px;
  color: #6c757d;
  margin: 0 1rem;
}

.card-img-top {
  width: 100%;
  height: 150px; /* 고정 높이 */
  object-fit: cover; /* 이미지가 영역을 채우도록 */
  border-radius: 12px;
}

.card-body {
  padding: 0.75rem;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  flex-grow: 1;
  min-height: 140px;
}

.card-text {
  text-align: center;
  flex-grow: 1;
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.card-text strong {
  font-size: 1.3rem;
  font-weight: 600;
  color: #333;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.address-text {
  font-size: 0.9rem;
  color: #6c757d;
  display: block;
  margin-top: 0.25rem;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 3; /* 최대 3줄 허용 */
  -webkit-box-orient: vertical;
}

.date-header {
  margin-bottom: 1.5rem;
}

.badge {
  font-size: 1rem;
  padding: 0.4rem 1.2rem;
  border-radius: 20px;
  background-color: #99ccff;
}

.card-container {
  flex-wrap: wrap;
  gap: 2rem;
  padding: 0 1rem;
}

@media (max-width: 768px) {
  .draggable-item {
    width: 200px;
    height: 270px;
  }

  .card-img-top {
    height: 120px;
  }

  .card-body {
    min-height: 120px;
  }

  .badge {
    font-size: 0.75rem;
    padding: 0.3rem 1rem;
  }

  .fade-button {
    width: 32px;
    height: 32px;
    font-size: 14px;
    top: -8px;
    right: -8px;
  }
}
</style>
