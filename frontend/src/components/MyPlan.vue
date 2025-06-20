<template>
  <div class="container my-5">
    <h1 class="text-center mb-4">나의 여행 다이어리</h1>

    <!-- 여행 개요 -->
    <section class="mb-5">
      <h2 class="card-title">여행 개요</h2>
      <div class="card shadow-sm mb-4">
        <div class="card-body">
          <p class="card-text"><strong>장소:</strong> {{ tripOverview.location }}</p>
          <p class="card-text"><strong>날짜:</strong> {{ tripOverview.startYmd }} ~ {{ tripOverview.endYmd }}</p>
        </div>
      </div>
    </section>

    <!-- 여행 장소 섹션 -->
    <section class="mb-5">
      <h2 class="mb-3">여행 장소</h2>
      <div class="row">
        <div v-for="place in places" :key="place.id" class="col-md-4 mb-4">
          <div class="card h-100 shadow-sm">
            <img :src="place.image" class="card-img-top" :alt="place.name" />
            <div class="card-body">
              <h5 class="card-title">{{ place.name }}</h5>
              <p class="card-text">{{ place.description }}</p>
              <p class="text-muted">방문일: {{ place.date }}</p>
            </div>
          </div>
        </div>
      </div>
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
              <a :href="hotel.website" class="btn btn-primary btn-sm" target="_blank">예약 사이트</a>
            </div>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<script setup>
import { ref } from 'vue';

// 여행 개요 데이터
const tripOverview = ref({
  location: '서울',
  startYmd: '20250621',
  endYmd: '20250625'
});

// 데이터 정의
const places = ref([
  {
    id: 1,
    name: '경복궁',
    description: '서울의 대표적인 고궁으로, 조선 시대의 역사를 느낄 수 있는 곳.',
    date: '2025-06-21',
    image: 'https://via.placeholder.com/300x200?text=경복궁'
  },
  {
    id: 2,
    name: '남산타워',
    description: '서울의 랜드마크로, 야경이 아름다운 명소.',
    date: '2025-06-22',
    image: 'https://via.placeholder.com/300x200?text=남산타워'
  }
]);

const routes = ref([
  { id: 1, location: '인천공항', time: '2025-06-21 08:00' },
  { id: 2, location: '경복궁', time: '2025-06-21 10:00' },
  { id: 3, location: '남산타워', time: '2025-06-21 18:00' }
]);

const hotels = ref([
  {
    id: 1,
    name: '신라호텔',
    address: '서울 중구 동호로 249',
    checkIn: '2025-06-21',
    checkOut: '2025-06-23',
    website: 'https://www.shilla.net'
  },
  {
    id: 2,
    name: '롯데호텔',
    address: '서울 중구 을지로 30',
    checkIn: '2025-06-23',
    checkOut: '2025-06-25',
    website: 'https://www.lottehotel.com'
  }
]);

// API 연동 예시 (필요 시 주석 해제)
// import { onMounted } from 'vue';
// onMounted(async () => {
//   const response = await fetch('/api/travel-diary');
//   const data = await response.json();
//   places.value = data.places;
//   routes.value = data.routes;
//   hotels.value = data.hotels;
// });
</script>

<style scoped>
.card-img-top {
  height: 200px;
  object-fit: cover;
}
h1, h2 {
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
}
.card {
  transition: transform 0.2s;
}
.card:hover {
  transform: translateYFAB(-5px);
}
</style>
