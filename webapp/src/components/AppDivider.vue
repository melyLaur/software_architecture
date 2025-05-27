<script setup lang="ts">

defineProps<{
  title?: string
}>();

</script>

<template>
  <div class="flex items-center justify-center py-4">
    <div class="w-1/2 border-t border-gray-300"></div>
    <span v-if="title" class="px-2 text-gray-500">{{ title }}</span>
    <div class="w-1/2 border-t border-gray-300"></div>
  </div>
</template>

<style scoped>

</style>