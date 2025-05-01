<template>
  <div class="row align-items-center">
    <div class="col-8">
      <!-- DataTales Example -->
      <div class="card shadow mb-4">
        <div class="card-header py-3">
          <h6 class="m-0 font-weight-bold text-primary">세부 설정을 입력해주세요</h6>
        </div>
        <div class="card-body">
          <div class="table-responsive">
            <table class="table table-bordered" id="dataTable" width="100%" cellspacing="0">
              <thead>
                <tr class="table-primary">
                  <th></th>
                  <th>설정 값</th>
                  <th>추가</th>
                </tr>
              </thead>
              <tbody>
                <tr>
                  <td class="text-center align-middle" width="12%">언제?</td>
                  <td>
                    <div class="row align-items-center">
                      <div class="col">
                        <VueDatePicker
                          v-model="startDate"
                          locale="ko"
                          :format="format"
                          :enable-time-picker="false"
                        ></VueDatePicker>
                      </div>
                      <div>~</div>
                      <div class="col">
                        <VueDatePicker
                          v-model="endDate"
                          locale="ko"
                          :format="format"
                          :enable-time-picker="false"
                          disabled
                          :select-text="''"
                          :cancel-text="''"
                        ></VueDatePicker>
                      </div>
                    </div>
                  </td>
                  <td class="text-center align-middle">추후 설정</td>
                </tr>
                <tr>
                  <td class="text-center align-middle">뭐하지?</td>
                  <td>
                    <div class="row">
                      <div class="col">
                        <div class="form-check">
                          <input
                            class="form-check-input"
                            type="checkbox"
                            value="activity"
                            id="activity"
                            v-model="selectedCheckboxes"
                          />
                          <label class="form-check-label" for="activity">활동적인</label>
                        </div>
                      </div>
                      <div class="col">
                        <div class="form-check">
                          <input
                            class="form-check-input"
                            type="checkbox"
                            value="museum"
                            id="museum"
                            v-model="selectedCheckboxes"
                          />
                          <label class="form-check-label" for="museum">박물관</label>
                        </div>
                      </div>
                    </div>
                    <div class="row">
                      <div class="col">
                        <div class="form-check">
                          <input
                            class="form-check-input"
                            type="checkbox"
                            value="cafe"
                            id="cafe"
                            v-model="selectedCheckboxes"
                          />
                          <label class="form-check-label" for="cafe">분위기 좋은 카페</label>
                        </div>
                      </div>
                      <div class="col">
                        <!-- 나중에 콤보박스 또는 세부 체크박스로 변경 -->
                        <div class="form-check">
                          <input
                            class="form-check-input"
                            type="checkbox"
                            value="tour"
                            id="tour"
                            v-model="selectedCheckboxes"
                          />
                          <label class="form-check-label" for="tour">관광 명소</label>
                        </div>
                      </div>
                    </div>
                  </td>
                  <td class="text-center align-middle">추후 설정</td>
                </tr>
              </tbody>
            </table>
            <div class="d-flex justify-content-end">
              <SaveButton @save="saveData"></SaveButton>
            </div>
          </div>
        </div>
      </div>
      <!-- 여행 정보 요약 컴포넌트 -->
      <TripData :trip-data="tripData" />
    </div>
  </div>
</template>

<script setup>
import { ref, watch, onMounted } from 'vue'
import SaveButton from '@/components/SaveButton.vue'
import TripData from '@/components/TripData.vue'
import { useAuthStore } from '@/stores/auth'
import axios from 'axios'

const authStore = useAuthStore()
const startDate = ref(new Date())
const endDate = ref(new Date(new Date().setDate(startDate.value.getDate() + 1)))
const tripData = ref(null)

// 컴포넌트 마운트 시 데이터 조회
onMounted(() => {
  if (authStore.username && authStore.token) {
    fetchTripData()
  }
})

// 화면에 표시할 날짜 형식 (YYYY/MM/DD)
const format = (date) => {
  const year = date.getFullYear()
  const month = date.getMonth() + 1
  const day = date.getDate()

  return `${year}/${month}/${day}`
}

// API로 보낼 날짜 형식 (YYYYMMDD)
const apiFormat = (date) => {
  const year = date.getFullYear()
  const month = String(date.getMonth() + 1).padStart(2, '0')
  const day = String(date.getDate()).padStart(2, '0')
  return `${year}${month}${day}`
}

const selectedCheckboxes = ref([])

// startDate 변경 감지
watch(
  startDate,
  (newStartDate) => {
    // endDate를 startDate + 1일로 설정
    const newEndDate = new Date(newStartDate)
    newEndDate.setDate(newStartDate.getDate() + 1)
    endDate.value = newEndDate

    // 날짜 업데이트 후 alert를 비동기적으로 표시
    setTimeout(() => {
      alert('현재 버전에서는 1박 2일 설정만 가능합니다.')
    }, 200)
  },
  { immediate: false }
)

const saveData = () => {
  if (selectedCheckboxes.value.length === 0) {
    alert('활동을 하나 이상 선택해주세요.')
    return
  }

  const settingDto = {
    username: authStore.username,
    startYmd: apiFormat(startDate.value),
    endYmd: apiFormat(endDate.value),
    activity: selectedCheckboxes.value.includes('activity'),
    museum: selectedCheckboxes.value.includes('museum'),
    cafe: selectedCheckboxes.value.includes('cafe'),
    tourAtt: selectedCheckboxes.value.includes('tour')
  }
  console.log('activity--->: ', selectedCheckboxes.value.includes('activity'))

  axios
    .post('/api/save/setting', settingDto, {
      headers: {
        Authorization: `Bearer ${authStore.token}`,
        'Content-Type': 'application/json'
      }
    })
    .then(() => {
      alert('설정이 성공적으로 저장되었습니다.')
      fetchTripData()
    })
    .catch((error) => {
      alert('설정 저장에 실패했습니다: ' + (error.response?.data || error.message))
    })
}

// 여행 정보 조회
const fetchTripData = () => {
  axios
    .get('/api/get/setting', {
      params: { username: authStore.username },
      headers: {
        Authorization: `Bearer ${authStore.token}`,
        'Content-Type': 'application/json'
      }
    })
    .then((response) => {
      if (response.data) {
        tripData.value = response.data
      } else {
        tripData.value = null
      }
    })
    .catch((error) => {
      console.error('여행 정보 조회 실패:', error.response?.data || error.message)
      tripData.value = null
    })
}
</script>

<style scoped>
.table-responsive {
  overflow: visible !important; /* 테이블 경계를 초과할 수 있도록 설정 */
  position: relative; /* 위치 기준을 명확히 설정 */
}
</style>
