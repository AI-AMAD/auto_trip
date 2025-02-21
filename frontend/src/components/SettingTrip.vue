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
                        ></VueDatePicker>
                      </div>
                      <div>~</div>
                      <div class="col">
                        <VueDatePicker
                          v-model="endDate"
                          locale="ko"
                          :format="format"
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
                            value="tour"
                            id="tour"
                            v-model="selectedCheckboxes"
                          />
                          <label class="form-check-label" for="tour">명소 구경</label>
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
                            value="exclude"
                            id="exclude"
                            v-model="selectedCheckboxes"
                          />
                          <label class="form-check-label" for="exclude">이건 빼주세요</label>
                        </div>
                      </div>
                    </div>
                  </td>
                  <td class="text-center align-middle">추후 설정</td>
                </tr>
                <tr>
                  <td class="text-center align-middle">출발 장소</td>
                  <td>
                    <input
                      v-model="startLocation"
                      type="text"
                      class="form-control"
                      placeholder="출발지 주소를 입력 하세요"
                    />
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
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import SaveButton from '@/components/SaveButton.vue'

const startDate = ref(new Date())
const endDate = ref(new Date())
// In case of a range picker, you'll receive [Date, Date]
// date의 타입은 넘버 차후에 스트링으로 변환 하기
const format = (date) => {
  const year = date.getFullYear()
  const month = date.getMonth() + 1
  const day = date.getDate()

  return `${year}/${month}/${day}`
}

const startLocation = ref('')
const selectedCheckboxes = ref([])

const saveData = () => {
  alert(`세부 설정을 저장하였습니다.
  일정 -> ${format(startDate.value)} ~ ${format(endDate.value)}
  뭐할지 -> ${selectedCheckboxes.value.join(', ')}
  출발지 -> ${startLocation.value}`)
}
</script>

<style scoped>
.table-responsive {
  overflow: visible !important; /* 테이블 경계를 초과할 수 있도록 설정 */
  position: relative; /* 위치 기준을 명확히 설정 */
}
</style>
