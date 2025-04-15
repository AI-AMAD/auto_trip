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
      <naver-map style="width: 100%; height: 400px" :map-options="mapOptions">
        <naver-marker latitude="37.51347" longitude="127.041722" />
      </naver-map>
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

const mapOptions = {
  latitude: 37.51347, // 지도 중앙 위도
  longitude: 127.041722, // 지도 중앙 경도
  zoom: 13
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
