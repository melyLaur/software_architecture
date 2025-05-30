<script lang="ts" setup>

definePageMeta({
  layout: 'main-layout',
  middleware: ['role'],
  roles: ['MANAGER']
})


</script>

<template>
  <div class="h-full w-full">

    <h1 class="text-2xl font-bold mb-4">Tableau de bord</h1>
    <iframe
        class="w-full h-full"
        src="http://localhost:4000/public-dashboards/e8e3106368984d4e9ebddb86f53cdc78?orgId=1&kiosk&refresh=30s&theme=light"
    />
  </div>
</template>
