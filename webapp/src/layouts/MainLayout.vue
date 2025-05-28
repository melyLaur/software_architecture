<script setup lang="ts">
import Sidebar from "~/components/Sidebar.vue";


</script>

<template>
  <div class="flex h-screen">
    <Sidebar />
    <main class="ml-64 p-4 w-full h-full">
      <slot></slot>
    </main>
  </div>
</template>

<style scoped>

</style>