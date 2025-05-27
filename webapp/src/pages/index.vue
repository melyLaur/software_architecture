<script lang="ts" setup>
import {getPaginationRowModel} from '@tanstack/vue-table'
import type {TableColumn} from '@nuxt/ui'
import MainLayout from "~/layouts/MainLayout.vue";

const table = useTemplateRef('table')

type Employee = {
  id: string
  firstName: string
  lastName: string
  email: string
}
const data = ref<Employee[]>([
  {id: '1', firstName: 'John', lastName: 'Doe', email: 'c.lechene+1@email.fr'},
  {id: '2', firstName: 'John', lastName: 'Doe', email: 'c.lechene+1@email.fr'},
  {id: '3', firstName: 'John', lastName: 'Doe', email: 'c.lechene+1@email.fr'},
  {id: '4', firstName: 'John', lastName: 'Doe', email: 'c.lechene+1@email.fr'},
  {id: '5', firstName: 'John', lastName: 'Doe', email: 'c.lechene+1@email.fr'},
  {id: '6', firstName: 'John', lastName: 'Doe', email: 'c.lechene+1@email.fr'},
  {id: '7', firstName: 'John', lastName: 'Doe', email: 'c.lechene+1@email.fr'},
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
    cell: ({row}) => row.getValue('firstName')
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
  }
]

const pagination = ref({
  pageIndex: 0,
  pageSize: 5
})
</script>

<template>
  <MainLayout>
    <div class="flex flex-col h-full">
      <div class="h-1/12">
        <div class="flex items-center justify-between mb-4">
          <h1 class="text-2xl font-bold">Employés</h1>
          <div class="flex items-center space-x-2">
            <UButton>Nouveau employé</UButton>
          </div>
        </div>
      </div>
      <div class="h-11/12 flex flex-col">
        <UTable
            ref="table"
            v-model:pagination="pagination"
            :columns="columns"
            :data="data"
            :pagination-options="{ getPaginationRowModel: getPaginationRowModel() }"
            class="flex-1 h-full"
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
    </div>
  </MainLayout>
</template>
