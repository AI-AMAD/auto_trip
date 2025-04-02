<template>
  <div class="row">
    <div class="col-8">
      <div class="card">
        <h5 class="card-header">어디로 여행을 떠나시나요?</h5>
        <div class="card-body">
          <h5 class="card-title">Special title treatment</h5>
          <p class="card-text">
            With supporting text below as a natural lead-in to additional content.
          </p>
          <a href="#" class="btn btn-primary">Go somewhere</a>
        </div>
      </div>
    </div>
    <div class="col-4">
      <form class="row" @submit.prevent="search">
        <div class="col-auto">
          <input
            v-model="inputLocation"
            type="text"
            class="form-control"
            placeholder="여행 장소를 입력 하세요"
          />
        </div>
        <div class="col-auto">
          <button type="submit" class="btn btn-info mb-3">검색</button>
        </div>
      </form>
      <div class="row">
        <div class="col">
          <div class="card">
            <h5 class="card-header">이 장소가 맞나요?</h5>
            <div class="card-body">
              <p class="card-text">{{ searchedLocation }}</p>
            </div>
          </div>
        </div>
      </div>
      <div class="row mt-2">
        <div class="col">
          <SaveButton @save="saveData"></SaveButton>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import SaveButton from '@/components/SaveButton.vue'
import { ref } from 'vue'
import { NaverMap, NaverMarker } from 'vue3-naver-maps'

const inputLocation = ref('')
const searchedLocation = ref('')
const search = () => {
  searchedLocation.value = inputLocation.value
}

const saveData = () => {
  alert(`해당 장소를 저장합니다: ${searchedLocation.value}`)
}

const searchQuery = ref('')
const place = ref(null)
const mapCenter = ref({ lat: 37.5665, lng: 126.978 }) // 기본 위치: 서울

const searchPlace = async () => {
  const query = encodeURIComponent(searchQuery.value)
  const url = `https://openapi.naver.com/v1/search/local.json?query=${query}&display=1`
  const response = await fetch(url, {
    headers: {
      'X-Naver-Client-Id': 'YOUR_SEARCH_CLIENT_ID',
      'X-Naver-Client-Secret': 'YOUR_SEARCH_CLIENT_SECRET'
    }
  })
  const data = await response.json()
  if (data.items && data.items.length > 0) {
    const item = data.items[0]
    const lat = parseFloat(item.mapy)
    const lng = parseFloat(item.mapx)
    place.value = { position: { lat, lng } }
    mapCenter.value = { lat, lng }
    await savePlaceToDB(item)
  }
}

const savePlaceToDB = async (item) => {
  const placeData = {
    title: item.title,
    address: item.address,
    lat: parseFloat(item.mapy),
    lng: parseFloat(item.mapx)
  }
  await fetch('/api/places', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(placeData)
  })
}
</script>

<style scoped></style>
