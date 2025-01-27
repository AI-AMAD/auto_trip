<template>
  <div class="row">
    <div class="col-12">
      <div class="card shadow mb-4">
        <div class="card-body">
          <!-- 여기에 내용 -->
          <table class="table table-bordered">
            <thead>
              <!-- 첫 번째 헤더 행: 2개의 열로 나누기 -->
              <tr>
                <th colspan="3" style="vertical-align: middle">1월1일 마지막 일정</th>
                <th colspan="3">
                  <img src="@/assets/img/swiss.png" class="img-fluid" style="max-width: 40%" />
                </th>
              </tr>
            </thead>
            <tbody>
              <!-- 두 번째 행: 통합된 셀 -->
              <tr>
                <td colspan="6" class="text-center" style="height: 70px">
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
      </div>
    </div>
  </div>
</template>

<script setup></script>

<style scoped>
table {
  table-layout: fixed; /* 테이블 셀 크기를 균등하게 유지 */
}
th,
td {
  text-align: center;
  vertical-align: middle;
}

.radio-label {
  margin-left: 8px; /* 라디오 버튼과 텍스트 사이 간격 추가 */
}
</style>
