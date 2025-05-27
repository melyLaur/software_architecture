<script setup lang="ts">

</script>

<template>
  <aside class="w-64 h-screen bg-gray-100 border-r border-gray-200 fixed top-0 left-0 flex flex-col">
    <div class="p-4 font-bold text-lg border-b border-gray-200">
      Park Slot
    </div>
    <nav class="flex-1 p-4 space-y-2">
      <NuxtLink to="/" class="block px-4 py-2 rounded hover:bg-gray-200">Accueil</NuxtLink>
      <NuxtLink to="/profil" class="block px-4 py-2 rounded hover:bg-gray-200">Profil</NuxtLink>
      <NuxtLink to="/parametres" class="block px-4 py-2 rounded hover:bg-gray-200">Paramètres</NuxtLink>
    </nav>
  </aside>
</template>

<style scoped>

</style>