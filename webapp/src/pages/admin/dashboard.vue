<script lang="ts" setup>

definePageMeta({
  layout: 'main-layout',
  middleware: ['role'],
  roles: ['MANAGER']
})


</script>

<template>
  <h1 class="text-2xl font-bold mb-4">Tableau de bord</h1>
</template>
