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
          <span class="d-block mt-2 text-gray-600 small">{{ nickname }}</span>
        </li>

        <!-- Divider -->
        <hr class="sidebar-divider" />

        <!-- Heading -->
        <div class="sidebar-heading">Addons</div>

        <!-- Nav Item - Charts -->
        <li class="nav-item">
          <router-link to="/main/setting/where" class="nav-link">
            <i class="fas fa-fw fa-chart-area"></i>
            <span>여행 계획</span></router-link
          >
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
import { ref } from 'vue'

const nickname = ref('')
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
</style>
