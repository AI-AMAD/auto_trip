<template>
  <div class="container mt-3">
    <div class="step-container">
      <router-link to="/main/setting/where">
        <div class="step active" onclick="goToStep(1)" id="step-1">어디로?</div>
      </router-link>
      <div class="step-arrow active" id="arrow-1"></div>
      <router-link to="/main/setting/when">
        <div class="step inactive" onclick="goToStep(2)" id="step-2">언제?</div>
      </router-link>
    </div>
  </div>
  <!-- 여행 계획 설정 -->
  <router-view></router-view>
  <!-- end of 여행 계획 설정 -->
</template>

<script setup>
const goToStep = (step) => {
  const steps = document.querySelectorAll('.step')
  const arrows = document.querySelectorAll('.step-arrow')

  steps.forEach((stepElement, index) => {
    if (index < step) {
      stepElement.classList.add('active')
      stepElement.classList.remove('inactive')
    } else {
      stepElement.classList.remove('active')
      stepElement.classList.add('inactive')
    }
  })

  arrows.forEach((arrow, index) => {
    if (index < step - 1) {
      arrow.classList.add('active')
      arrow.classList.remove('inactive')
    } else {
      arrow.classList.remove('active')
      arrow.classList.add('inactive')
    }
  })
}
</script>

<style scoped>
.step-container {
  display: flex;
  align-items: center;
  justify-content: flex-start; /* 버튼 정렬 (여기서 간격 조절 가능) */
  gap: 10px; /* 버튼과 화살표 간격 조정 */
}
.step {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 10px 20px;
  font-size: 14px;
  font-weight: bold;
  border-radius: 30px;
  color: white;
  background-color: #5bc0be; /* 활성화 단계 색상 */
  cursor: pointer;
  transition: background-color 0.3s ease;
}
.step.inactive {
  background-color: #e0e0e0; /* 비활성화 단계 색상 */
  color: #6c757d;
  cursor: not-allowed;
}
.step-arrow {
  width: 0;
  height: 0;
  border-top: 15px solid transparent;
  border-bottom: 15px solid transparent;
  border-left: 15px solid #5bc0be; /* 기본 화살표 색상 */
}
.step-arrow.inactive {
  border-left-color: #e0e0e0; /* 비활성화 단계 화살표 색상 */
}
</style>
