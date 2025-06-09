<template>
  <div>
    <h2>auto-trip으로 여행 계획에서 자유로워 지세요!</h2>
    <div class="card-container">
      <div class="row">
        <div class="card">
          <img src="@/assets/img/where.png" class="card-img-top" alt="Where to go" />
          <div class="card-body">
            <h5 class="card-title">어디로 갈까?</h5>
            <p class="card-text">가고 싶은 여행지를 선택하세요.</p>
          </div>
        </div>
        <div class="card">
          <img src="@/assets/img/details.png" class="card-img-top" alt="Where to go" />
          <div class="card-body">
            <h5 class="card-title">언제? 가서 뭐할까?</h5>
            <p class="card-text">여행의 세부설정을 선택하세요.</p>
          </div>
        </div>
      </div>
      <div class="row">
        <div class="card">
          <img src="@/assets/img/how.png" class="card-img-top" alt="Where to go" />
          <div class="card-body">
            <h5 class="card-title">이건 빼줘! 아냐 여기부터 갈래!</h5>
            <p class="card-text">여행지의 순서를 바꿔보세요.</p>
          </div>
        </div>
        <div class="card">
          <img src="@/assets/img/hotel.png" class="card-img-top" alt="Where to go" />
          <div class="card-body">
            <h5 class="card-title">근데 우리 어디서 자?</h5>
            <p class="card-text">검색하지 말고 선택만 하세요.</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup></script>

<style scoped>
h2 {
  text-align: left; /* 좌측 정렬로 변경 */
  margin-bottom: 2rem;
  padding-left: 1rem; /* 좌측 여백 추가 */
}

.card-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 1rem;
}

.row {
  display: flex;
  justify-content: space-between;
  margin-bottom: 2rem;
}

.card {
  flex: 0 0 47%; /* 비율은 유지하되, 최소 너비를 키우기 위해 필요 시 min-width를 조정할 수 있습니다 */
  height: auto; /* 높이를 자동으로 조정 */
  opacity: 0;
  animation: fadeIn 1s forwards, shake 0.5s ease-in-out 2;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  border-radius: 10px;
  overflow: hidden;
  min-width: 0; /* min-width를 0으로 설정해 유연성을 높임 */
}

.card-img-top {
  width: 100%;
  height: 27vh;
  object-fit: contain;
  image-rendering: -webkit-optimize-contrast;
}

.card-body {
  padding: 1.8rem; /* 패딩을 늘려 높이감을 더함 */
  text-align: center;
}

@keyframes fadeIn {
  from {
    opacity: 0;
  }
  to {
    opacity: 1;
  }
}

@keyframes shake {
  0% {
    transform: translateY(-2px) rotate(0deg);
  }
  25% {
    transform: translateY(-2px) rotate(1deg);
  }
  50% {
    transform: translateY(-2px) rotate(0deg);
  }
  75% {
    transform: translateY(-2px) rotate(-1deg);
  }
  100% {
    transform: translateY(-2px) rotate(0deg);
  }
}

@media (max-width: 768px) {
  .row {
    flex-direction: column;
    align-items: center;
  }

  .card {
    flex: 0 0 100%;
    margin-bottom: 2rem;
  }

  .card-img-top {
    height: 20vh;
  }
}
</style>
