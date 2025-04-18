<template>
  <div class="row">
    <div class="col-8">
      <!--      <div class="card">-->
      <!--        <h5 class="card-header">어디로 여행을 떠나시나요?</h5>-->
      <!--        <div class="card-body">-->
      <!--          <h5 class="card-title">Special title treatment</h5>-->
      <!--          <p class="card-text">-->
      <!--            With supporting text below as a natural lead-in to additional content.-->
      <!--          </p>-->
      <!--          <a href="#" class="btn btn-primary">Go somewhere</a>-->
      <!--        </div>-->
      <!--      </div>-->
      <div id="map" style="width: 100%; height: 400px"></div>
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

const script = document.createElement('script')
script.src = 'https://oapi.map.naver.com/openapi/v3/maps.js'
script.async = true
script.defer = true
document.head.appendChild(script)
script.onload = () => {
  new naver.maps.Map('map', {
    center: new naver.maps.LatLng(37.5670135, 126.978374),
    zoom: 10
  })
}

const inputLocation = ref('')
const searchedLocation = ref('')
const search = () => {
  searchedLocation.value = inputLocation.value
}

const saveData = () => {
  alert(`해당 장소를 저장합니다: ${searchedLocation.value}`)
}
</script>

<style scoped></style>
