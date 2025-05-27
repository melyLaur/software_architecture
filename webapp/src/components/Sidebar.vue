<script setup lang="ts">

import {useAuthSession} from "~/composables/auth/useAuthSession";
import { useRoute } from 'vue-router'

const route = useRoute()
const { getUser } = useAuthSession()

const links = computed(() => {
  const role = getUser()?.role;

  const entries = [
    { name: 'Mes Réservations', to: '/reservations' },
  ]

  if (role === 'manager') {
    entries.push({ name: 'Métriques', to: '/admin/metrics' })
  }

  if (role === 'secretary') {
    entries.push(
        { name: 'Gérer les employés', to: '/admin/employees' },
        { name: 'Réservations', to: '/admin/reservations' }
    )
  }

  return entries
})

</script>

<template>
  <aside class="w-64 h-screen bg-gray-100 border-r border-gray-200 fixed top-0 left-0 flex flex-col">
    <div class="p-4 font-bold text-lg border-b border-gray-200">
      Park Slot ({{ getUser()?.role || 'Guest' }})
    </div>
    <nav class="flex-1 p-4 space-y-2">
      <NuxtLink
          v-for="link in links"
          :key="link.to"
          :to="link.to"
          :class="[
            'block px-4 py-2 rounded hover:bg-gray-200',
            route.path === link.to ? 'bg-gray-300 font-semibold' : ''
          ]"
      >
        {{ link.name }}
      </NuxtLink>
    </nav>
  </aside>
</template>

<style scoped>

</style>