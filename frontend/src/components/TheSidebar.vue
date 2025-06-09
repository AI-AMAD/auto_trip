<template>
  <body id="page-top">
    <!-- Page Wrapper -->
    <div id="wrapper">
      <!-- Sidebar -->
      <ul
        class="navbar-nav bg-gradient-primary sidebar sidebar-dark accordion"
        id="accordionSidebar"
      >
        <!-- Sidebar - Brand -->
        <router-link to="/main/manual" class="nav-link" active-class="active">
          <a class="sidebar-brand d-flex align-items-center justify-content-center">
            <!-- <div class="sidebar-brand-icon rotate-n-15">
<i class="fas fa-laugh-wink"></i>
</div> -->
            <div class="sidebar-brand-text mx-3">Auto-trip</div>
          </a>
        </router-link>

        <!-- Divider -->
        <hr class="sidebar-divider my-0" />

        <!-- Nav Item - User Information -->
        <li class="nav-item text-center py-3">
          <img
            class="img-profile rounded-circle mx-auto d-block"
            src="@/assets/img/undraw_profile.svg"
            width="100"
            height="100"
          />
          <span class="d-block mt-2 nickname-text">{{ nickname }}</span>
          <button @click="logout" class="btn btn-sm btn-danger mt-2">로그아웃</button>
        </li>

        <!-- Divider -->
        <hr class="sidebar-divider" />

        <!-- Heading -->
        <div class="sidebar-heading">Addons</div>

        <!-- Nav Item - Charts -->
        <li class="nav-item position-relative">
          <router-link to="/main/setting/where" class="nav-link">
            <i class="fas fa-fw fa-chart-area"></i>
            <span>여행 계획</span></router-link
          >
          <div v-if="route.path === '/main/manual'" class="arrow-container">
            <img src="../assets/img/newarrow.png" class="arrow" alt="화살표" />
          </div>
        </li>
        <!-- Nav Item - Charts -->
        <li class="nav-item">
          <router-link to="/main/plan" class="nav-link">
            <i class="fas fa-fw fa-chart-area"></i>
            <span>일정 보기</span></router-link
          >
        </li>
      </ul>
      <!-- End of Sidebar -->

      <!-- Content Wrapper -->
      <div id="content-wrapper" class="d-flex flex-column">
        <!-- Main Content -->
        <div id="content">
          <!-- Begin Page Content -->
          <div class="container-fluid">
            <router-view></router-view>
          </div>
          <!-- /.container-fluid -->
        </div>
        <!-- End of Main Content -->
      </div>
      <!-- End of Content Wrapper -->
    </div>
    <!-- End of Page Wrapper -->
  </body>
</template>

<script setup>
import { onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useAuthStore } from '@/stores/auth'

const authStore = useAuthStore()
const router = useRouter()
const route = useRoute()

// Pinia에서 상태를 직접 사용
const nickname = authStore.nickname
const profileImageUrl = authStore.profileImageUrl

// 로그아웃 함수
const logout = () => {
  authStore.logout() // Pinia 상태 초기화
  alert('로그아웃 되었습니다.')
  router.push('/') // 로그인 페이지로 리다이렉트
}

// 페이지 로드 시 토큰이 있으면 유저 정보 확인
onMounted(() => {
  if (authStore.token && !authStore.nickname) {
    authStore.fetchUserProfile().catch((error) => {
      console.error('유저 정보 로드 실패:', error)
      // 필요 시 로그인 페이지로 리다이렉트
    })
  }
})
</script>

<style scoped>
#content-wrapper {
  margin: 20px;
}

#accordionSidebar {
  width: 13% !important;
}

.nav-item .nav-link span {
  font-size: 1.1rem;
}

.sidebar-brand-text {
  font-size: 1.3rem;
}

.nickname-text {
  color: #000000; /* 검정색 */
  font-weight: 500; /* 약간 두꺼운 글씨 (500~600은 중간 굵기, 700은 bold) */
  font-size: 1.15rem; /* 필요에 따라 크기 조정 (기본값 또는 small 대체) */
}

.arrow-container {
  position: absolute;
  left: 60%; /* 화살표 위치를 조정하려면 이 값을 수정 (예: -25px, -35px) */
  top: 50%;
  z-index: 100;
  transform: translateY(-50%);
}

.arrow {
  width: 135%; /* 화살표 넓이를 조정하려면 이 값을 수정 (예: 25px, 35px) */
  height: auto; /* 높이는 자동으로 비율에 맞춰 조정 */
  animation: blinkFade 3.5s forwards; /* 깜빡이는 애니메이션 적용 */
}

@keyframes blinkFade {
  0% {
    opacity: 0;
  }
  20% {
    opacity: 1;
  }
  40% {
    opacity: 0;
  }
  60% {
    opacity: 1;
  }
  80% {
    opacity: 0;
  }
  100% {
    opacity: 0;
  }
}
</style>
