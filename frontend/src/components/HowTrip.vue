<template>
  <div class="row align-items-stretch">
    <div
      v-for="(item, index) in items"
      :key="item.id"
      class="col-3"
      draggable="true"
      @dragstart="onDragStart(index)"
      @dragover.prevent
      @drop="onDrop(index)"
    >
      <div class="card draggable-item text-center p-3" :class="{ faded: item.isFaded }">
        <img src="@/assets/img/swiss.png" class="card-img-top" />
        <div class="card-body">
          <div class="card-text">
            {{ item.text }}
            <button class="fade-button btn btn-sm btn-danger mt-2" @click="onFadeItem(index)">
              X
            </button>
          </div>
        </div>
      </div>
      <!-- 화살표 표시 -->
      <div v-if="index < items.length - 1" class="arrow ms-2">→</div>
    </div>
  </div>
  <div class="text-center mt-4">
    <button class="btn btn-success" @click="onSave">저장</button>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const items = ref([
  { id: 1, text: '아이템 1', isFaded: false },
  { id: 2, text: '아이템 2', isFaded: false },
  { id: 3, text: '아이템 3', isFaded: false },
  { id: 4, text: '아이템 4', isFaded: false },
  { id: 5, text: '아이템 5', isFaded: false },
  { id: 6, text: '아이템 6', isFaded: false },
  { id: 7, text: '아이템 7', isFaded: false },
  { id: 8, text: '아이템 8', isFaded: false }
])

const draggedItemIndex = ref(null)

const onDragStart = (index) => {
  draggedItemIndex.value = index
}

const onDrop = (targetIndex) => {
  const sourceIndex = draggedItemIndex.value

  if (sourceIndex !== null && sourceIndex !== targetIndex) {
    const movedItem = items.value[sourceIndex]
    items.value.splice(sourceIndex, 1)
    items.value.splice(targetIndex, 0, movedItem)
  }

  draggedItemIndex.value = null
}

const onFadeItem = (index) => {
  items.value[index].isFaded = !items.value[index].isFaded
}

const onSave = () => {
  items.value = items.value.filter((item) => !item.isFaded)
}
</script>

<style>
.draggable-item {
  background-color: #f3f3f3;
  border: 1px solid #ddd;
  border-radius: 8px;
  cursor: grab;
  transition: opacity 0.3s ease, background-color 0.3s ease;
}

.draggable-item:active {
  cursor: grabbing;
  background-color: #e0e0e0;
}

.draggable-item.faded {
  opacity: 0.5;
  background-color: #f0f0f0;
}

.fade-button {
  font-size: 14px;
}

.fade-button:hover {
  background-color: #b21f2d;
}

.arrow {
  font-size: 24px;
  color: #6c757d;
  margin: 0 10px; /* 화살표와 아이템 간격 조정 */
}
</style>
