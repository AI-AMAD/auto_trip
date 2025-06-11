<template>
  <div class="row">
    <div class="col-12">
      <div class="card shadow mb-4">
        <div class="card-body">
          <!-- 여기에 내용 -->
          <!-- 여기에 내용 -->
          <table class="table table-bordered">
            <thead>
              <!-- 첫 번째 헤더 행: 2개의 열로 나누기 -->
              <tr class="header-row">
                <th colspan="3" class="date-text header-cell">{{ scheduleDate }}</th>
                <th colspan="3" class="header-cell">
                  <div class="d-flex flex-column align-items-center">
                    <img :src="scheduleImage" class="img-fluid schedule-image" />
                    <div class="place-text">{{ place }}</div>
                    <div class="address-text">{{ address }}</div>
                  </div>
                </th>
              </tr>
            </thead>
            <tbody>
              <!-- 두 번째 행: 통합된 셀 -->
              <tr>
                <td colspan="6" class="text-center select-hotel-cell">
                  원하는 숙소를 선택 해주세요
                </td>
              </tr>
              <!-- 세 번째 행: 3개의 열로 나누기 (각각 1/3 너비) -->
              <tr>
                <td colspan="2">
                  <img src="@/assets/img/hotel1.png" class="img-fluid" style="max-width: 90%" />
                </td>
                <td colspan="2">
                  <img src="@/assets/img/hotel2.png" class="img-fluid" style="max-width: 90%" />
                </td>
                <td colspan="2">
                  <img src="@/assets/img/hotel3.png" class="img-fluid" style="max-width: 90%" />
                </td>
              </tr>
              <tr>
                <td colspan="2">
                  <input type="radio" id="radio1" name="hotel" value="1" />
                  <label for="radio1" class="radio-label">숙소 1</label>
                </td>
                <td colspan="2">
                  <input type="radio" id="radio2" name="hotel" value="2" />
                  <label for="radio2" class="radio-label">숙소 2</label>
                </td>
                <td colspan="2">
                  <input type="radio" id="radio3" name="hotel" value="3" />
                  <label for="radio3" class="radio-label">숙소 3</label>
                </td>
              </tr>
              <tr>
                <td colspan="2">마지막 장소 -> 이동시간:</td>
                <td colspan="2">마지막 장소 -> 이동시간:</td>
                <td colspan="2">마지막 장소 -> 이동시간:</td>
              </tr>
            </tbody>
          </table>
        </div>
        <!-- 내용 끝 -->
        <!-- col div 닫는 곳 -->
        <div class="col d-flex justify-content-center mt-4 pb-3">
          <SaveButton @save="saveData"></SaveButton>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import SaveButton from '@/components/SaveButton.vue'
import { useAuthStore } from '@/stores/auth'
import axios from 'axios'

const scheduleDate = ref('')
const scheduleImage = ref('')
const place = ref('')
const address = ref('')

const authStore = useAuthStore()

onMounted(() => {
  if (authStore.username && authStore.token) {
    fetchTripData()
  }
})

const fetchTripData = async () => {
  try {
    await axios
      .get('/api/get/lastplace', {
        params: { username: authStore.username },
        headers: {
          Authorization: `Bearer ${authStore.token}`,
          'Content-Type': 'application/json'
        }
      })
      .then((response) => {
        // start_ymd를 포맷팅하여 scheduleDate 업데이트
        scheduleDate.value = format(response.data.startYmd)
        place.value = response.data.activityName
        address.value = response.data.activityAddress

        // activity_image_url을 사용하여 이미지 업데이트
        if (response.data.activityImageUrl) {
          scheduleImage.value = response.data.activityImageUrl
        }
      })
  } catch (error) {
    console.error('일정 데이터를 가져오는 중 오류 발생:', error)
  }
}

const format = (startYmd) => {
  const year = startYmd.slice(0, 4)
  const month = parseInt(startYmd.slice(4, 6))
  const day = parseInt(startYmd.slice(6, 8))
  return `${year}년 ${month}월 ${day}일 마지막 일정`
}
</script>

<style scoped>
table {
  table-layout: fixed; /* 테이블 셀 크기를 균등하게 유지 */
  width: 100%;
}

th,
td {
  text-align: center;
  vertical-align: middle;
}

.card-body {
  padding: 1.5rem; /* card-body 패딩 증가 */
}

.header-row {
  min-height: 150px; /* 첫 번째 행 최소 높이 */
}

.header-cell {
  vertical-align: middle;
  padding: 20px; /* 헤더 셀 패딩 */
}

.schedule-image {
  max-width: 60%;
  margin-bottom: 10px;
  object-fit: contain; /* 이미지 비율 유지 */
}

.select-hotel-cell {
  height: 70px; /* 두 번째 행 높이 */
}

.date-text {
  font-size: 1.2rem; /* scheduleDate 글자 크기 */
  font-weight: bold; /* 가독성 위해 굵게 */
}

.place-text {
  font-size: 1.5rem;
  font-weight: bold;
  margin-bottom: 5px;
}

.address-text {
  font-size: 0.9rem;
  color: #6c757d;
}

.radio-label {
  margin-left: 8px; /* 라디오 버튼과 텍스트 사이 간격 추가 */
}
</style>
