<template>
  <div class="d-flex">
    <!-- Sidebar -->
    <nav class="bg-dark text-white p-3 vh-100" style="width: 10%; border-radius: 0px 15px 15px 0px">
      <h3 class="mb-4">Auto - Trip!!</h3>
      <ul class="nav flex-column">
        <li class="nav-item mb-3">
          <router-link class="nav-link text-white fs-5" active-class="active" to="/main/setting"
            >내 설정</router-link
          >
        </li>
        <li class="nav-item mb-3">
          <router-link class="nav-link text-white fs-5" active-class="active" to="/main/plan"
            >여행 계획</router-link
          >
        </li>
        <li class="nav-item mb-3">
          <router-link class="nav-link text-white fs-5" active-class="active" to="/main/schedule"
            >일정 보기</router-link
          >
        </li>
        <li class="nav-item mb-3">
          <router-link class="nav-link text-white fs-5" active-class="active" to="/main/diary"
            >다이어리</router-link
          >
        </li>
      </ul>
    </nav>

    <!-- Content -->
    <div class="flex-grow-1 p-4">
      <router-view></router-view>
    </div>
  </div>
</template>

<script setup></script>

<style lang="scss" scoped></style>
