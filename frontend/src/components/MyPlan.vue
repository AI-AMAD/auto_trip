<template>
  <div class="my-5">
    <h1 class="text-center mb-4">나의 여행 다이어리</h1>

    <!-- 여행 개요 -->
    <section class="mb-5">
      <h2 class="card-title text-start">여행 개요</h2>
      <div class="overview-card card shadow-sm mb-4">
        <div class="card-body d-flex justify-content-between align-items-center">
          <p class="card-text mb-0"><strong>장소:</strong> {{ tripOverview.location }}</p>
          <p class="card-text mb-0">
            <strong>날짜:</strong> {{ tripOverview.startYmd }} ~ {{ tripOverview.endYmd }}
          </p>
        </div>
      </div>
    </section>

    <!-- 여행 장소 섹션 -->
    <section class="mb-5">
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
    <section class="mb-5">
      <h2 class="mb-3">호텔</h2>
      <div class="row">
        <div v-for="hotel in hotels" :key="hotel.id" class="col-md-6 mb-4">
          <div class="card h-100 shadow-sm">
            <div class="card-body">
              <h5 class="card-title">{{ hotel.name }}</h5>
              <p class="card-text">{{ hotel.address }}</p>
              <p class="text-muted">체크인: {{ hotel.checkIn }} | 체크아웃: {{ hotel.checkOut }}</p>
              <a :href="hotel.website" class="btn btn-primary btn-sm" target="_blank"
                >예약 사이트</a
              >
            </div>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'

const tripOverview = ref({
  location: '서울',
  startYmd: '20250621',
  endYmd: '20250625'
})

const places = ref([
  {
    id: 1,
    name: '경복궁',
    description: '서울의 대표적인 고궁으로, 조선 시대의 역사를 느낄 수 있는 곳.',
    date: '20250621',
    image: 'https://via.placeholder.com/300x200?text=경복궁'
  },
  {
    id: 2,
    name: '남산타워',
    description: '서울의 랜드마크로, 야경이 아름다운 명소.',
    date: '20250622',
    image: 'https://via.placeholder.com/300x200?text=남산타워'
  },
  {
    id: 3,
    name: '에버랜드',
    description: '볼거리 놀거리 최고~~',
    date: '20250622',
    image: 'https://via.placeholder.com/300x200?text=에버랜드'
  }
])

const hotels = ref([
  {
    id: 1,
    name: '신라호텔',
    address: '서울 중구 동호로 249',
    checkIn: '20250621',
    checkOut: '20250623',
    website: 'https://www.shilla.net'
  },
  {
    id: 2,
    name: '롯데호텔',
    address: '서울 중구 을지로 30',
    checkIn: '20250623',
    checkOut: '20250625',
    website: 'https://www.lottehotel.com'
  }
])

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

const defaultImage = new URL('@/assets/img/swiss.png', import.meta.url).href

const handleImageError = (event) => {
  event.target.src = defaultImage
}

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
  width: 100%; /* 너비를 화면에 맞게 100%로 설정 */
  max-width: none; /* 기존 너비 제한 제거 */
  height: auto; /* 높이는 콘텐츠에 따라 자동 조정 */
  margin: 0; /* 좌측 정렬 유지 */
}

/* 카드 내부 콘텐츠 스타일 */
.overview-card .card-body {
  padding: 1rem;
  min-height: 60px; /* 높이를 줄여 가로로 긴 형태로 */
}

/* 여행 장소 카드 스타일 */
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
  box-shadow: 0 6px 12px rgba(0, 0, 0, 0.15);
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
  margin-left: 0;
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

  .overview-card {
    max-width: 100%;
  }
}
</style>
