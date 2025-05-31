<script lang="ts" setup>
import type {FormSubmitEvent} from '@nuxt/ui'
import {createEmployeeFormSchema, type CreateEmployeeFormSchema} from "~/components/inputs/employee";
import type { PropType } from 'vue'
import type { Employee } from "~/types/employee";

const modelValue = defineModel('open', {
  type: Boolean,
  default: false
})

const employee = defineModel('employee', {
  type: Object as PropType<Employee>,
  default: () => ({
    id: '',
    firstName: '',
    lastName: '',
    email: '',
    role: 'EMPLOYEE'
  })
});

defineProps({
  type: {
    type: String,
    default: 'create'
  },
})

watch(employee, (newValue) => {
  console.log('Employee changed:', newValue);
  if (newValue) {
    form.value = {
      firstName: employee.value.firstName ?? '',
      lastName: employee.value.lastName ?? '',
      email: employee.value.email ?? '',
      role: employee.value.role ?? 'EMPLOYEE'
    }
  }
})

const emits = defineEmits(['onSubmit'])

const form = ref<CreateEmployeeFormSchema>({
  firstName: '',
  lastName: '',
  email: '',
  role: 'EMPLOYEE'
})

async function onSubmit(event: FormSubmitEvent<CreateEmployeeFormSchema>) {
  emits('onSubmit', {
    firstName: event.data.firstName,
    lastName: event.data.lastName,
    email: event.data.email,
    role: event.data.role
  })
}

</script>

<template>
  <UModal v-model:open="modelValue" :title="type === 'create' ? 'Créer un nouvel employé' : 'Mettre à jour l\'employé'" class="">
    <template #body>
      <UForm :schema="createEmployeeFormSchema" :state="form" class="w-full space-y-6" @submit="onSubmit">
        <UFormField label="Nom" name="lastName" required>
          <UInput v-model="form.lastName" class="w-full" placeholder="Entrez le nom complet" />
        </UFormField>
        <UFormField label="Prénom" name="firstName" required>
          <UInput v-model="form.firstName" class="w-full" placeholder="Entrez le nom complet" />
        </UFormField>
        <UFormField label="Email" name="email" required>
          <UInput v-model="form.email" class="w-full" type="email" placeholder="Entrez l'email" />
        </UFormField>
        <UFormField label="Rôle" name="role" required>
          <USelect v-model="form.role" :items="[
            { label: 'Employé', value: 'EMPLOYEE' },
            { label: 'Manager', value: 'MANAGER' },
            { label: 'Secrétaire', value: 'SECRETARY' }
          ]" class="w-full" label-key="label" value-key="value"/>
        </UFormField>
        <UButton class="w-full" type="submit">
          {{ type === 'create' ? "Créer l'employé" : "Mettre à jour l'employé" }}
        </UButton>
      </UForm>
    </template>
  </UModal>
</template>

<style scoped>

</style>