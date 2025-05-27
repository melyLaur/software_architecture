<script setup lang="ts">

</script>

<template>
  <div class="p-8 text-center">
    <h1 class="text-2xl font-bold">403 – Accès interdit</h1>
    <p>Vous n’avez pas les permissions pour accéder à cette page.</p>
    <UButton class="mt-4" @click="$router.push('/')">Retour à l'accueil</UButton>
  </div>
</template>
<style scoped>

</style>