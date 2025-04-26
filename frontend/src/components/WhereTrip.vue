<template>
  <div class="row">
    <div class="col-8">
      <div id="map" style="width: 100%; height: 400px"></div>
    </div>
    <div class="col-4">
      <div class="row mb-3">
        <div class="col">
          <div class="card">
            <h5 class="card-header">현재 저장된 장소</h5>
            <div class="card-body">
              <p class="card-text">{{ existingPlace || '저장된 장소가 없습니다' }}</p>
            </div>
          </div>
        </div>
      </div>
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
              <p class="card-text">{{ searchedLocation || '검색된 장소가 없습니다' }}</p>
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
import { ref, onMounted } from 'vue'
import { useAuthStore } from '@/stores/auth'
import axios from 'axios'

const authStore = useAuthStore()

let map
const inputLocation = ref('')
const searchedLocation = ref('')
const existingPlace = ref('') // 기존 저장된 장소

onMounted(() => {
  if (window.naver && window.naver.maps) {
    map = new window.naver.maps.Map('map', {
      center: new window.naver.maps.LatLng(37.5670135, 126.978374),
      zoom: 10
    })
  } else {
    console.error('Naver Maps API is not loaded')
  }
  // 컴포넌트 마운트 시 기존 장소 조회
  fetchExistingPlace()
})

// 기존 장소 조회 함수
const fetchExistingPlace = () => {
  if (!authStore.username) return
  axios
    .get('/api/place', {
      params: { username: authStore.username },
      headers: {
        Authorization: `Bearer ${authStore.token}`
      }
    })
    .then((response) => {
      existingPlace.value = response.data?.place || ''
    })
    .catch((error) => {
      console.error('기존 장소 조회 실패:', error)
      if (error.response?.status !== 204) {
        alert(`기존 장소 조회에 실패했습니다: ${error.response?.data || error.message}`)
      }
    })
}

const search = () => {
  // searchedLocation.value = inputLocation.value
  if (!inputLocation.value) return
  if (!inputLocation.value.trim()) {
    alert('검색어를 입력하세요')
    return
  }
  naver.maps.Service.geocode(
    {
      query: inputLocation.value.trim()
    },
    function (status, response) {
      console.log('Geocode response:', response) // 응답 구조 확인
      if (status !== naver.maps.Service.Status.OK) {
        alert('검색에 실패했습니다: ' + status)
        return
      }
      const addresses = response.v2?.addresses
      console.log('addresses---->: ', addresses)
      if (!addresses || addresses.length === 0) {
        alert('검색 결과가 없습니다')
        return
      }
      const result = addresses[0]
      const point = new naver.maps.LatLng(result.y, result.x)
      map.setCenter(point)
      const marker = new naver.maps.Marker({
        position: point,
        map: map
      })
      searchedLocation.value = result.roadAddress || result.jibunAddress || inputLocation.value
    }
  )
}

const saveData = () => {
  if (!searchedLocation.value) {
    alert('저장할 장소가 없습니다. 먼저 검색하세요.')
    return
  }
  if (!authStore.username) {
    alert('로그인이 필요합니다.')
    return
  }

  const placeDto = {
    username: authStore.username,
    place: searchedLocation.value
  }

  axios
    .post('/api/save/place', placeDto, {
      headers: {
        Authorization: `Bearer ${authStore.token}`,
        'Content-Type': 'application/json'
      }
    })
    .then((response) => {
      const oldPlace = existingPlace.value
      // 서버에서 최신 데이터 재조회
      fetchExistingPlace()
      let message
      if (response.data.includes('updated') && oldPlace) {
        message = `기존 장소 "${oldPlace}" -> "${searchedLocation.value}"으로 변경되었습니다.`
      } else {
        message = `장소 "${searchedLocation.value}"가 저장되었습니다.`
      }
      alert(message)
      // 입력 필드와 검색 결과 초기화
      inputLocation.value = ''
      searchedLocation.value = ''
      map.setCenter(new window.naver.maps.LatLng(37.5670135, 126.978374))
    })
    .catch((error) => {
      console.error('저장 실패:', error)
      alert(`장소 저장에 실패했습니다: ${error.response?.data || error.message}`)
    })
}
</script>

<style scoped></style>
