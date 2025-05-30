<script lang="ts" setup>
import {ref} from 'vue'
import {getPaginationRowModel} from '@tanstack/vue-table'
import type {DropdownMenuItem, TableColumn, TableRow} from '@nuxt/ui'
import {useEmployeeApi} from "~/services/employees/employee.api";

definePageMeta({
  layout: 'main-layout',
  middleware: ['role'],
  roles: ['SECRETARY', 'MANAGER', 'EMPLOYEE']
})

const table = useTemplateRef('table')
const {getEmployees} = useEmployeeApi()

type Employee = {
  id: string
  firstName: string
  lastName: string
  email: string
  remainingPark: number
}
const data = ref<Employee[]>([
  {id: '1', firstName: 'John', lastName: 'Doe', email: 'c.lechene+1@email.fr', remainingPark: 3},
  {id: '2', firstName: 'John', lastName: 'Doe', email: 'c.lechene+1@email.fr', remainingPark: 3},
  {id: '3', firstName: 'John', lastName: 'Doe', email: 'c.lechene+1@email.fr', remainingPark: 3},
  {id: '4', firstName: 'John', lastName: 'Doe', email: 'c.lechene+1@email.fr', remainingPark: 3},
  {id: '5', firstName: 'John', lastName: 'Doe', email: 'c.lechene+1@email.fr', remainingPark: 3},
  {id: '6', firstName: 'John', lastName: 'Doe', email: 'c.lechene+1@email.fr', remainingPark: 3},
  {id: '7', firstName: 'John', lastName: 'Doe', email: 'c.lechene+1@email.fr', remainingPark: 3},
  {id: '8', firstName: 'John', lastName: 'Doe', email: 'c.lechene+1@email.fr', remainingPark: 3},
  {id: '9', firstName: 'John', lastName: 'Doe', email: 'c.lechene+1@email.fr', remainingPark: 3},
  {id: '10', firstName: 'John', lastName: 'Doe', email: 'c.lechene+1@email.fr', remainingPark: 3},
  {id: '11', firstName: 'John', lastName: 'Doe', email: 'c.lechene+1@email.fr', remainingPark: 3},
  {id: '12', firstName: 'John', lastName: 'Doe', email: 'c.lechene+1@email.fr', remainingPark: 3},
  {id: '13', firstName: 'John', lastName: 'Doe', email: 'c.lechene+1@email.fr', remainingPark: 3},
  {id: '14', firstName: 'John', lastName: 'Doe', email: 'c.lechene+1@email.fr', remainingPark: 3},
  {id: '15', firstName: 'John', lastName: 'Doe', email: 'c.lechene+1@email.fr', remainingPark: 3},
  {id: '16', firstName: 'John', lastName: 'Doe', email: 'c.lechene+1@email.fr', remainingPark: 3},
  {id: '17', firstName: 'John', lastName: 'Doe', email: 'c.lechene+1@email.fr', remainingPark: 3},
  {id: '18', firstName: 'John', lastName: 'Doe', email: 'c.lechene+1@email.fr', remainingPark: 3},
  {id: '19', firstName: 'John', lastName: 'Doe', email: 'c.lechene+1@email.fr', remainingPark: 3},
  {id: '20', firstName: 'John', lastName: 'Doe', email: 'c.lechene+1@email.fr', remainingPark: 3},
])
const columns: TableColumn<Employee>[] = [
  {
    accessorKey: 'id',
    header: '#',
    cell: ({row}) => `#${row.getValue('id')}`
  },
  {
    accessorKey: 'firstName',
    header: 'Prénom',
    cell: ({row}) => row.getValue('lastName')
  },
  {
    accessorKey: 'lastName',
    header: 'Nom',
    cell: ({row}) => row.getValue('lastName')
  },
  {
    accessorKey: 'email',
    header: 'Email',
    cell: ({row}) => row.getValue('email')
  },
  {
    accessorKey: 'remainingPark',
    header: 'Places restantes',
    cell: ({row}) => row.getValue('remainingPark')
  },
]

const items = ref<DropdownMenuItem[]>([
  {
    label: 'Modifier',
    icon: 'i-lucide-edit',
  },
  {
    label: 'Supprimer',
    icon: 'i-lucide-trash-2',
    color: 'error',
    action: () => {
      console.log('Delete action triggered')
      // Implement delete logic here
    }
  },
])

const isSlideoverOpen = ref(false)
const selectedEmployee = ref<Employee | null>(null)

function onSelect(row: TableRow<Employee>, e?: Event) {
  console.log('Row selected:', row, e)
  const employeeId = row.id;
  openEmployeeDetails(employeeId)
}

function openEmployeeDetails(id: string) {
  selectedEmployee.value = data.value.find(emp => emp.id === id) || null
  console.log("selectedEmployee: ", selectedEmployee);
  isSlideoverOpen.value = true
}

const pagination = ref({
  pageIndex: 0,
  pageSize: 15
})

onMounted(async () => {
  try {
    data.value = await getEmployees();
  } catch (e) {
    console.error(e);
  }
})
</script>

<template>
  <div class="flex flex-col h-full">
    <div class="h-1/12">
      <div class="flex items-center justify-between mb-4">
        <h1 class="text-2xl font-bold">Employés</h1>
        <div class="flex items-center space-x-2">
          <UButton>Nouveau employé</UButton>
        </div>
      </div>
    </div>
    <div class="h-11/12 flex flex-col pb-10">
      <UTable
          ref="table"
          v-model:pagination="pagination"
          :columns="columns"
          :data="data"
          :pagination-options="{ getPaginationRowModel: getPaginationRowModel() }"
          class="flex-1 h-full"
          sticky
          @select="onSelect"
      />

      <div class="flex justify-center border-t border-default pt-4">
        <UPagination
            :default-page="(table?.tableApi?.getState().pagination.pageIndex || 0) + 1"
            :items-per-page="table?.tableApi?.getState().pagination.pageSize"
            :total="table?.tableApi?.getFilteredRowModel().rows.length"
            @update:page="(p) => table?.tableApi?.setPageIndex(p - 1)"
        />
      </div>
    </div>
    <USlideover v-model:open="isSlideoverOpen">
      <template #header>
        <div class="flex items-center justify-between">
          <h2 class="text-lg font-semibold">Détails de l'employé</h2>
          <UDropdownMenu
            :content="{
              align: 'start',
              side: 'bottom',
              sideOffset: 8
            }"
            :items="items"
            :ui="{
                content: 'w-48'
              }"
          >
            <UButton color="neutral" icon="i-lucide-menu" label="Open" variant="outline"/>
          </UDropdownMenu>
        </div>
      </template>
      <template #body>
        <div class="h-full">
          <div v-if="selectedEmployee" class="p-4">
            <h2 class="text-xl font-semibold mb-4">Détails de l'employé</h2>
            <p><strong>ID:</strong> {{ selectedEmployee.id }}</p>
            <p><strong>Prénom:</strong> {{ selectedEmployee.firstName }}</p>
            <p><strong>Nom:</strong> {{ selectedEmployee.lastName }}</p>
            <p><strong>Email:</strong> {{ selectedEmployee.email }}</p>
            <p><strong>Places restantes:</strong> {{ selectedEmployee.remainingPark }}</p>
          </div>
        </div>
      </template>
    </USlideover>
  </div>
</template>
