<template>
  <div class="row">
    <div class="col-8">
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
import { ref, onMounted } from 'vue'
import { useAuthStore } from '@/stores/auth'

const authStore = useAuthStore()

let map
const inputLocation = ref('')
const searchedLocation = ref('')

onMounted(() => {
  if (window.naver && window.naver.maps) {
    map = new window.naver.maps.Map('map', {
      center: new window.naver.maps.LatLng(37.5670135, 126.978374),
      zoom: 10
    })
    console.log('authStore에서 가져온 토큰 입니다.--->: ', authStore.token)
    console.log('authStore에서 가져온 닉네임 입니다.--->: ', authStore.nickname)
    console.log('authStore에서 가져온 프로필 입니다.--->: ', authStore.profileImageUrl)
    console.log('authStore에서 가져온 아이디 입니다.--->: ', authStore.username)
  } else {
    console.error('Naver Maps API is not loaded')
  }
})

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
  alert(`해당 장소를 저장합니다: ${searchedLocation.value}`)
}
</script>

<style scoped></style>
