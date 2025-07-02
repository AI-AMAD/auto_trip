<template>
  <div class="my-4">
    <!-- 여행 개요 -->
    <section class="mb-5 mx-0">
      <h2 class="card-title text-start">여행 개요</h2>
      <div
        class="card-container d-flex flex-row flex-wrap align-items-center justify-content-start"
      >
        <div class="overview-card card shadow-sm mb-4">
          <div class="card-body">
            <div class="card-text">
              <span><strong>장소:</strong> {{ tripOverview.place }}</span>
            </div>
            <div class="card-text">
              <span
                ><strong>날짜:</strong> {{ formatDate(tripOverview.startYmd) }} ~
                {{ formatDate(tripOverview.endYmd) }}</span
              >
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- 여행 장소 섹션 -->
    <section class="mb-5 mx-0">
      <h2 class="mb-3">여행 장소</h2>
      <div
        class="custom-row-spacing"
        v-for="(schedule, scheduleIndex) in groupedPlaces"
        :key="schedule.date"
      >
        <div class="date-header text-start mb-3">
          <span class="badge text-bg-secondary">{{ formatDate(schedule.date) }}</span>
        </div>
        <div
          class="card-container d-flex flex-row flex-wrap align-items-center justify-content-start"
        >
          <div
            v-for="(place, index) in schedule.items"
            :key="place.id"
            class="d-flex align-items-center"
          >
            <div class="card d-flex align-items-center p-3 position-relative">
              <img
                :src="place.image"
                class="card-img-top"
                :alt="place.name"
                @error="handleImageError"
              />
              <div class="card-body">
                <div class="card-text">
                  <strong>{{ place.name }}</strong
                  ><br />
                  <small class="address-text">{{ place.description }}</small>
                </div>
              </div>
            </div>
            <div v-if="index < schedule.items.length - 1" class="arrow mx-2">→</div>
          </div>
        </div>
      </div>
      <div v-if="!groupedPlaces.length" class="text-center mt-4 mb-5">여행 장소가 없습니다.</div>
    </section>

    <!-- 호텔 섹션 -->
    <section class="mb-5 mx-0">
      <h2 class="mb-3">호텔</h2>
      <div
        class="card-container d-flex flex-row flex-wrap align-items-center justify-content-start"
      >
        <div v-if="hotels.length" class="d-flex align-items-center">
          <div class="card d-flex align-items-center p-3 position-relative">
            <img
              :src="hotels[0].image"
              class="card-img-top"
              :alt="hotels[0].name"
              @error="handleImageError"
            />
            <div class="card-body">
              <div class="card-text">
                <strong>{{ hotels[0].name }}</strong
                ><br />
                <small class="address-text">{{ hotels[0].address }}</small
                ><br />
                <small class="text-muted"
                  >체크인: {{ formatDate(tripOverview.startYmd) }} | 체크아웃:
                  {{ formatDate(tripOverview.endYmd) }}</small
                >
              </div>
            </div>
          </div>
        </div>
      </div>
      <div v-if="!hotels.length" class="text-center mt-4 mb-5">호텔 정보가 없습니다.</div>
    </section>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import axios from 'axios'
import { useAuthStore } from '@/stores/auth'

const authStore = useAuthStore()

// 컴포넌트 마운트 시 데이터 가져오기
onMounted(() => {
  fetchTripData()
})

// 데이터 상태
const tripOverview = ref({
  place: '',
  startYmd: '',
  endYmd: ''
})
const places = ref([])
const hotels = ref([])

// 백엔드에서 데이터 가져오기
const fetchTripData = async () => {
  try {
    const response = await axios.get(`/api/diary/${authStore.username}`, {
      headers: {
        Authorization: `Bearer ${authStore.token}`,
        'Content-Type': 'application/json'
      }
    })
    if (response.data && response.data.length > 0) {
      const data = response.data[0] // 첫 번째 여행 계획 사용
      // 여행 개요 설정
      tripOverview.value = {
        place: data.place || '',
        startYmd: Object.keys(data.startYmd || {})[0] || '',
        endYmd: Object.keys(data.endYmd || {})[0] || ''
      }

      // 여행 장소 설정
      places.value = []
      if (data.startYmd && Object.keys(data.startYmd).length > 0) {
        const startKey = Object.keys(data.startYmd)[0]
        places.value.push(
          ...data.startYmd[startKey].map((activity, index) => ({
            id: `start-${index}`,
            name: activity.activityName,
            description: activity.activityAddress,
            date: startKey,
            image: activity.activityImageUrl
          }))
        )
      }
      if (data.endYmd && Object.keys(data.endYmd).length > 0) {
        const endKey = Object.keys(data.endYmd)[0]
        places.value.push(
          ...data.endYmd[endKey].map((activity, index) => ({
            id: `end-${index}`,
            name: activity.activityName,
            description: activity.activityAddress,
            date: endKey,
            image: activity.activityImageUrl
          }))
        )
      }

      // 호텔 설정
      hotels.value = data.hotel
        ? [
            {
              id: 'hotel-1',
              name: data.hotel.name || 'Unknown Hotel',
              address: data.hotel.address || 'No address provided',
              image: data.hotel.imageUrl || 'https://via.placeholder.com/300x200?text=호텔',
              website: 'https://example.com', // 백엔드에서 website 제공 안 하므로 기본값
              checkIn: tripOverview.value.startYmd,
              checkOut: tripOverview.value.endYmd
            }
          ]
        : []
    } else {
      tripOverview.value = { place: '', startYmd: '', endYmd: '' }
      places.value = []
      hotels.value = []
    }
  } catch (error) {
    console.error('여행 정보 조회 실패:', error.response?.data || error.message)
    tripOverview.value = { place: '', startYmd: '', endYmd: '' }
    places.value = []
    hotels.value = []
  }
}

// 날짜별로 장소 그룹화
const groupedPlaces = computed(() => {
  const grouped = places.value.reduce((acc, place) => {
    const date = place.date
    if (!acc[date]) {
      acc[date] = { date, items: [] }
    }
    acc[date].items.push(place)
    return acc
  }, {})
  return Object.values(grouped).sort((a, b) => a.date.localeCompare(b.date))
})

// 대체 이미지
const defaultImage = new URL('@/assets/img/swiss.png', import.meta.url).href

const handleImageError = (event) => {
  event.target.src = defaultImage
}

// 날짜 포맷팅 함수: '20250621' -> '2025년 06월 21일'
const formatDate = (dateStr) => {
  if (!dateStr) return ''
  const year = dateStr.slice(0, 4)
  const month = dateStr.slice(4, 6)
  const day = dateStr.slice(6, 8)
  return `${year}년 ${parseInt(month)}월 ${parseInt(day)}일`
}
</script>

<style scoped>
/* 여행 개요 카드 스타일 */
.overview-card {
  width: 30% !important;
  min-height: 120px;
  max-height: 150px;
  background-color: #ffffff;
  border: 1px solid #e0e0e0;
  border-radius: 12px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
}

.overview-card:hover {
  box-shadow: 0 6px 12px rgba(0, 0, 0, 0.15);
  transform: translateY(-2px);
}

.overview-card .card-body {
  padding: 1.5rem;
  height: 100%;
  display: flex;
  flex-direction: column; /* 세로 방향으로 정렬 */
  align-items: flex-start; /* 좌측 정렬 */
}

.overview-card .card-text {
  text-align: left;
  font-size: 1.1rem;
  color: #333;
  margin-bottom: 0.5rem;
}

/* 여행 장소 및 호텔 카드 스타일 */
.card {
  background-color: #ffffff;
  border: 1px solid #e0e0e0;
  border-radius: 12px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  width: 250px;
  height: 320px;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  position: relative;
  transition: all 0.3s ease;
}

.card:hover {
  box-shadow: 0 6px 12px rgba(0, 0, 0, 0.1);
  transform: translateY(-2px);
}

.card-img-top {
  width: 100%;
  height: 150px;
  object-fit: cover;
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
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
}

.custom-row-spacing {
  margin-bottom: 60px;
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
  display: flex;
  flex-wrap: wrap;
  gap: 2rem;
  justify-content: flex-start;
  width: 100%;
  padding: 0 1rem;
}

.arrow {
  font-size: 33px;
  color: #6c757d;
  margin: 0 1rem;
}

h1,
h2 {
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
}

@media (max-width: 768px) {
  .overview-card {
    width: 100% !important;
    min-height: 100px;
    max-height: 130px;
  }

  .overview-card .card-text {
    font-size: 1rem;
  }

  .card {
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
}
</style>
