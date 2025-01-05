<template>
  <button @click="saveClick" type="button" class="btn btn-info">저장</button>
</template>

<script setup>
const emit = defineEmits(['save'])

const saveClick = () => {
  emit('save')
}
</script>

<style lang="scss" scoped></style>
