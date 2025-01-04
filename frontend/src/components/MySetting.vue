<template>
  <!-- 1 번째 로우: 스탭바 -->
  <div class="row mb-4">
    <div class="col-12">
      <div class="step-container">
        <router-link to="/main/setting/where" class="nav-link">
          <div
            class="step"
            :class="activeButton === 'where' ? 'btn-active' : 'btn-inactive'"
            @click="setActive('where')"
          >
            어디로?
          </div>
        </router-link>

        <div
          class="step-arrow"
          :class="activeButton === 'where' ? 'arrow-active' : 'arrow-inactive'"
        ></div>

        <router-link to="/main/setting/when" class="nav-link">
          <div
            class="step"
            :class="activeButton === 'when' ? 'btn-active' : 'btn-inactive'"
            @click="setActive('when')"
          >
            언제?
          </div>
        </router-link>

        <div
          class="step-arrow"
          :class="activeButton === 'when' ? 'arrow-active' : 'arrow-inactive'"
        ></div>

        <router-link to="/main/setting/who" class="nav-link">
          <div
            class="step"
            :class="activeButton === 'who' ? 'btn-active' : 'btn-inactive'"
            @click="setActive('who')"
          >
            누구랑?
          </div>
        </router-link>

        <div
          class="step-arrow"
          :class="activeButton === 'who' ? 'arrow-active' : 'arrow-inactive'"
        ></div>

        <router-link to="/main/setting/what" class="nav-link">
          <div
            class="step"
            :class="activeButton === 'what' ? 'btn-active' : 'btn-inactive'"
            @click="setActive('what')"
          >
            뭐하지?
          </div>
        </router-link>

        <div
          class="step-arrow"
          :class="activeButton === 'what' ? 'arrow-active' : 'arrow-inactive'"
        ></div>

        <router-link to="/main/setting/how" class="nav-link">
          <div
            class="step"
            :class="activeButton === 'how' ? 'btn-active' : 'btn-inactive'"
            @click="setActive('how')"
          >
            어떻게 갈까?
          </div>
        </router-link>

        <div
          class="step-arrow"
          :class="activeButton === 'how' ? 'arrow-active' : 'arrow-inactive'"
        ></div>

        <router-link to="/main/setting/hotel" class="nav-link">
          <div
            class="step"
            :class="activeButton === 'hotel' ? 'btn-active' : 'btn-inactive'"
            @click="setActive('hotel')"
          >
            숙소는?
          </div>
        </router-link>
      </div>
    </div>
  </div>
  <!-- end of 1번째 로우 -->

  <!-- 2번째 로우 -->
  <!-- 여행 계획 설정 -->
  <div class="row">
    <div class="col-12">
      <router-view></router-view>
    </div>
  </div>
  <!-- end of 여행 계획 설정 -->
  <!-- end of 2번째 로우 -->
</template>

<script setup>
import { ref } from 'vue'

// 현재 활성화된 버튼을 관리하는 상태
const activeButton = ref('where')

// 버튼 활성화 상태 변경
const setActive = (button) => {
  activeButton.value = button
}
</script>

<style scoped>
.step-container {
  display: flex;
  align-items: center;
  justify-content: flex-start;
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
  background-color: #5bc0be;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.step-arrow {
  width: 0;
  height: 0;
  border-top: 15px solid transparent;
  border-bottom: 15px solid transparent;
  border-left: 15px solid #4aa8d8;
}

.arrow-active {
  border-left-color: #4aa8d8; /* 활성화 색상 */
}

.arrow-inactive {
  border-left-color: #e0e0e0; /* 비활성화 색상 */
}

.btn-active {
  background-color: #4aa8d8; /* 활성화 색상 */
  color: white;
}

.btn-inactive {
  background-color: #e0e0e0; /* 비활성화 색상 */
  color: #6c757d;
}
</style>
