<script setup lang="ts">

import {type AuthUser, useAuthSession} from "~/composables/auth/useAuthSession";
import { useRoute } from 'vue-router'

const route = useRoute()
const { setUser } = useAuthSession()

const employees = ref<AuthUser[]>([
  { id: '00000000-0000-0000-0000-000000000001', name: 'Jean Dumont', email: 'c.lec@fe.fr', role: 'EMPLOYEE' },
  { id: '00000000-0000-0000-0000-000000000002', name: 'Jean Dumont', email: 'c.lec@fe.fr', role: 'SECRETARY' },
  { id: '00000000-0000-0000-0000-000000000003', name: 'Jean Dumont', email: 'c.lec@fe.fr', role: 'MANAGER' },
])
const selectedEmployee = ref(employees.value[0].role)

function changeEmployee() {
  const employee = employees.value.find(emp => emp.role === selectedEmployee.value);
  if (employee) {
    setUser(employee);
  }
}

const links = computed(() => {
  const role = selectedEmployee.value;

  const entries = []

  if(role === 'EMPLOYEE') {
    entries.push({ name: 'Mes Réservations', to: '/reservations' })
  }

  if (role === 'MANAGER') {
    entries.push(
        { name: 'Métriques', to: '/admin/dashboard' },
        { name: 'Mes Réservations', to: '/reservations' }
    )
  }

  if (role === 'SECRETARY') {
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
    <div class="flex p-4 font-bold text-lg border-b border-gray-200 space-x-2">
      <div>ParkSlot</div>
      <div>
        <USelect v-model="selectedEmployee" :items="employees" label-key="role" value-key="role" @change="changeEmployee()" />
      </div>
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