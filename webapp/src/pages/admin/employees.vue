<script lang="ts" setup>
import {ref} from 'vue'
import SaveEmployeeModal from "~/components/modals/employees/SaveEmployeeModal.vue"
import {getPaginationRowModel} from '@tanstack/vue-table'
import type {DropdownMenuItem, TableColumn, TableRow} from '@nuxt/ui'
import type {Role} from "~/types/role";
import type {Employee} from "~/types/employee";
import {useEmployee} from "~/composables/employees/useEmployee";
import {useNotify} from "~/composables/useNotify";
import {useModals} from "~/composables/modals/useModals";

definePageMeta({
  layout: 'main-layout',
  middleware: ['role'],
  roles: ['SECRETARY', 'MANAGER', 'EMPLOYEE']
})

const {showPopupContinueModal} = useModals()
const {showSuccess, showError} = useNotify()
const {isError, fetchEmployees, createEmployee, updateEmployee, deleteEmployee} = useEmployee()
const table = useTemplateRef('table')

const openCreateEmployeeModal = ref(false)
const openUpdateEmployeeModal = ref(false)
const isSlideoverOpen = ref(false)
const selectedEmployee = ref<Employee>()
const pagination = ref({
  pageIndex: 0,
  pageSize: 15
})

const data = ref<Employee[]>([] as Employee[])
const columns: TableColumn<Employee>[] = [
  {
    accessorKey: 'employeeId',
    header: '#',
    cell: ({row}) => `#${row.getValue('employeeId')}`
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
    accessorKey: 'role',
    header: 'Role',
    cell: ({row}) => formatRole(row.getValue('role'))
  },
]

const items = ref<DropdownMenuItem[]>([
  {
    label: 'Modifier',
    icon: 'i-lucide-edit',
    onSelect: () => {
      openUpdateEmployeeModal.value = true
    }
  },
  {
    label: 'Supprimer',
    icon: 'i-lucide-trash-2',
    color: 'error',
    onSelect: () => {
      onDeleteEmployee();
    }
  },
])

function onSelect(row: TableRow<Employee>) {
  selectedEmployee.value = data.value[row.index];
  console.log(selectedEmployee.value)
  isSlideoverOpen.value = true
}

async function onCreateEmployee(employee: { firstName: string, lastName: string, email: string, role: Role }) {
  await createEmployee(employee);

  if(isError.value) {
    showError(`Erreur lors de la création de l'employé (${isError.value}).`);
    return;
  }

  showSuccess('Employé créé avec succès.');
  openCreateEmployeeModal.value = false;
}

async function onUpdateEmployee(employee: { firstName: string, lastName: string, email: string, role: Role }) {
  if (!selectedEmployee.value) {
    showError("Aucun employé sélectionné pour la mise à jour.");
    return;
  }

  await updateEmployee(selectedEmployee.value.employeeId, employee);
  openUpdateEmployeeModal.value = false;
  selectedEmployee.value = {
    ...selectedEmployee.value,
    ...employee
  }

  if(isError.value) {
    showError(`Erreur lors de la mise à jour de l'employé (${isError.value}).`);
    return;
  }

  showSuccess('Employé mis à jour avec succès.');
}

function onDeleteEmployee() {
  if (!selectedEmployee.value) {
    showError("Aucun employé sélectionné.");
    return;
  }

  const instance = showPopupContinueModal('Êtes-vous sûr de vouloir supprimer cet employé ?');
  instance.result.then(async (result) => {
    if (result) {
      try {
        // Call the delete employee API here
        await deleteEmployee(selectedEmployee.value!.employeeId);
        isSlideoverOpen.value = false;
        data.value = data.value.filter(emp => emp.employeeId !== selectedEmployee.value!.employeeId);
        selectedEmployee.value = undefined;
        showSuccess('Employé supprimé avec succès.');
      } catch (e) {
        console.error(e);
        showError("Erreur lors de la suppression de l'employé.");
      }
    }
  });
}

function formatRole(role: Role): string {
  switch (role) {
    case 'EMPLOYEE':
      return 'Employé';
    case 'MANAGER':
      return 'Manager';
    case 'SECRETARY':
      return 'Secrétaire';
    default:
      return 'Inconnu';
  }
}

onMounted(async () => {
  try {
    data.value = await fetchEmployees() as Employee[];
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
          <UButton @click="openCreateEmployeeModal = true">Nouveau employé</UButton>
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
    <USlideover v-model:open="isSlideoverOpen" @close="selectedEmployee = undefined">
      <template #header>
        <div class="flex items-center justify-between w-full">
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
            <UButton color="neutral" icon="i-lucide-menu" variant="outline"/>
          </UDropdownMenu>
        </div>
      </template>
      <template #body>
        <div class="h-full">
          <div v-if="selectedEmployee" class="p-4">
            <div class="flex justify-between">
              <div>ID</div>
              <div class="text-gray-500">{{ selectedEmployee.employeeId }}</div>
            </div>
            <div class="flex justify-between mt-2">
              <div>Prénom</div>
              <div class="text-gray-500">{{ selectedEmployee.firstName }}</div>
            </div>
            <div class="flex justify-between mt-2">
              <div>Nom</div>
              <div class="text-gray-500">{{ selectedEmployee.lastName }}</div>
            </div>
            <div class="flex justify-between mt-2">
              <div>Email</div>
              <div class="text-gray-500">{{ selectedEmployee.email }}</div>
            </div>
            <div class="flex justify-between mt-2">
              <div>Rôle</div>
              <div class="text-gray-500">{{ formatRole(selectedEmployee.role) }}</div>
            </div>
          </div>
        </div>
      </template>
    </USlideover>
    <SaveEmployeeModal
        v-model:open="openCreateEmployeeModal"
        type="create"
        @on-submit="onCreateEmployee($event)"
    />
    <SaveEmployeeModal
        v-model:open="openUpdateEmployeeModal"
        v-model:employee="selectedEmployee"
        type="update"
        @on-submit="onUpdateEmployee($event)"
    />
  </div>
</template>
