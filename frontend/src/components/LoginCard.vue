<template>
  <!-- 로그인 템플릿 -->
  <div class="mt-4 d-flex justify-content-center">
    <span class="mt-5 mb-5 ms-5 me-5 border-2 rounded-3 border border-primary">
      <form class="mt-5 mb-5 ms-5 me-5">
        <!-- Email input -->
        <div data-mdb-input-init class="form-outline mb-4">
          <input type="email" id="form2Example1" class="form-control" />
          <label class="form-label" for="form2Example1">Email address</label>
        </div>

        <!-- Password input -->
        <div data-mdb-input-init class="form-outline mb-4">
          <input type="password" id="form2Example2" class="form-control" />
          <label class="form-label" for="form2Example2">Password</label>
        </div>

        <!-- 2 column grid layout for inline styling -->
        <div class="row mb-4">
          <div class="col d-flex justify-content-center">
            <!-- Checkbox -->
            <div class="form-check">
              <input
                class="form-check-input"
                type="checkbox"
                value=""
                id="form2Example31"
                checked
              />
              <label class="form-check-label" for="form2Example31"> Remember me </label>
            </div>
          </div>

          <div class="col">
            <!-- Simple link -->
            <a href="#!">Forgot password?</a>
          </div>
        </div>

        <!-- Submit button -->
        <button
          type="button"
          data-mdb-button-init
          data-mdb-ripple-init
          class="btn btn-primary btn-block mb-4"
        >
          <router-link class="nav-link" to="/main/setting"> Sign in </router-link>
        </button>

        <!-- Register buttons -->
        <div class="text-center">
          <p>Not a member? <a href="#!">Register</a></p>
          <p>or sign up with:</p>
          <button
            type="button"
            data-mdb-button-init
            data-mdb-ripple-init
            class="btn btn-link btn-floating mx-1"
          >
            <img src="@/assets/kakao_login.png" />
          </button>
        </div>
      </form>
    </span>
  </div>
</template>

<script setup></script>

<style lang="scss" scoped></style>
