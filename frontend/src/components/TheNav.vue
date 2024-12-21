<template>
	<nav class="navbar navbar-expand-sm navbar-dark bg-primary">
  <div class="container-fluid">
    <router-link class="nav-link" active-class="active" to="/">AMAD</router-link>
    <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
      <span class="navbar-toggler-icon"></span>
    </button>
    <div class="collapse navbar-collapse" id="navbarSupportedContent">
      <ul class="navbar-nav me-auto">
        <li class="nav-item">
          <router-link 
            class="nav-link" 
            active-class="active" 
            to="/main/home"
          > home
          </router-link>
        </li>
        <li class="nav-item">
          <router-link 
            class="nav-link" 
            active-class="active" 
            to="/main/about"
          > about
          </router-link>
        </li>
      </ul> 
    </div>
  </div>
</nav>
</template>

<script setup>

</script>

<style lang="scss" scoped>

</style>