<script setup lang="ts">

definePageMeta({
  layout: 'main-layout',
  middleware: ['role'],
  roles: ['SECRETARY']
})

</script>

<template>
  <div>
    <h1 class="text-2xl font-bold mb-4">Réservations</h1>
  </div>
</template>

<style scoped>

</style>