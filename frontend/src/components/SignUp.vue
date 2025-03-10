<template>
  <body class="bg-gradient-primary">
    <div class="container">
      <div class="card o-hidden border-0 shadow-lg my-5">
        <div class="card-body p-0">
          <!-- Nested Row within Card Body -->
          <div class="row">
            <div class="col-lg-5 d-none d-lg-block bg-register-image"></div>
            <div class="col-lg-7">
              <div class="p-5">
                <div class="text-center">
                  <h1 class="h4 text-gray-900 mb-4">Create an Account!</h1>
                </div>
                <form class="user">
                  <div class="form-group row">
                    <div class="col-sm-8 mb-3 mb-sm-0">
                      <input
                        type="text"
                        class="form-control form-control-user"
                        id="exampleName"
                        placeholder="Name"
                      />
                    </div>
                  </div>
                  <div class="form-group row">
                    <div class="col-sm-8 mb-3 mb-sm-0">
                      <input
                        type="text"
                        class="form-control form-control-user"
                        id="exampleInputId"
                        placeholder="ID"
                      />
                    </div>
                  </div>
                  <div class="form-group row">
                    <div class="col-sm-6 mb-3 mb-sm-0">
                      <input
                        type="password"
                        class="form-control form-control-user"
                        id="exampleInputPassword"
                        placeholder="Password"
                      />
                    </div>
                    <div class="col-sm-6">
                      <input
                        type="password"
                        class="form-control form-control-user"
                        id="exampleRepeatPassword"
                        placeholder="Repeat Password"
                      />
                    </div>
                  </div>
                  <a href="login.html" class="btn btn-primary btn-user btn-block"> 회원 가입 </a>
                  <hr />
                  <a href="index.html" class="btn btn-warning btn-user btn-block">
                    <i class="fab fa-google fa-fw"></i> Kakao로 가입하기
                  </a>
                </form>
                <hr />
                <div class="text-center">
                  <a class="small" href="forgot-password.html"
                    >비밀번호가 기억이 나지 않으실때는?</a
                  >
                </div>
                <div class="text-center">
                  <router-link to="/" class="small"
                    >이미 계정이 있으시다면 로그인을 진행해주세요!
                  </router-link>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </body>
</template>

<script setup></script>

<style scoped>
.container {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100vh;
}

.card {
  width: 80%;
}
</style>
