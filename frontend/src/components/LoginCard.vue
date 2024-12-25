<template>
  <head> </head>

  <body data-cx-mode="light">
    <main class="wrapper">
      <section class="auth-section">
        <div class="cx-left">
          <div class="cx-bg"></div>
        </div>
        <div class="cx-right">
          <div class="cx-form-page">
            <!-- Login form -->
            <form class="cx-form">
              <h4>Login</h4>
              <div class="input-control">
                <input type="text" placeholder="Enter Username" name="uname" required />
                <span class="password-field-show">
                  <input
                    type="password"
                    placeholder="Enter Password"
                    name="password"
                    class="password-field"
                    value=""
                    required
                  />
                  <span
                    data-toggle=".password-field"
                    class="fa fa-fw fa-eye field-icon toggle-password"
                  ></span>
                </span>
                <div class="remember">
                  <div class="form-group">
                    <input type="checkbox" id="remember" />
                    <label for="remember">Remember me</label>
                  </div>
                  <a href="forgot.html" class="forgot-btn">Forgot password?</a>
                </div>
                <div class="form-btns">
                  <button type="submit">Login</button>
                </div>
                <div class="division-lines">
                  <p>or login with</p>
                </div>
                <div class="form-with-btns">
                  <span class="social">
                    <button type="button" class="google">
                      <i class="ri-google-fill"></i>
                    </button>
                    <button type="button" class="facebook">
                      <i class="ri-facebook-fill"></i>
                    </button>
                    <button type="button" class="twitter">
                      <i class="ri-twitter-fill"></i>
                    </button>
                    <button type="button" class="linkedin">
                      <i class="ri-linkedin-fill"></i>
                    </button>
                  </span>
                  <span class="already-acc"
                    >Not a member?
                    <a href="signup.html" class="signup-btn">Sign up</a>
                  </span>
                </div>
              </div>
            </form>
          </div>
        </div>
      </section>
    </main>
  </body>
</template>

<script setup></script>

<style lang="scss" scoped></style>
