<script lang="ts" setup>
import type {FormSubmitEvent} from '@nuxt/ui'
import {createReservationManagerFormSchema, type CreateReservationManagerFormSchema} from "~/components/inputs/reservation";

const modelValue = defineModel('open', {
  type: Boolean,
  default: false
})

defineProps({
  type: {
    type: String,
    default: 'create'
  }
})

const emits = defineEmits(['onSubmit'])

const form = ref<CreateReservationManagerFormSchema>({
  date: '',
  daysNumber: 1,
  isElectric: false
})

async function onSubmit(event: FormSubmitEvent<CreateReservationManagerFormSchema>) {
  emits('onSubmit', {
    date: event.data.date,
    daysNumber: event.data.daysNumber,
    isElectric: event.data.isElectric
  })
}

</script>

<template>
  <UModal v-model:open="modelValue" title="Créer une nouvelle réservation" class="">
    <template #body>
      <UForm :schema="createReservationManagerFormSchema" :state="form" class="w-full space-y-6" @submit="onSubmit">
        <UFormField label="Date de réservation" name="date" required>
          <UInput v-model="form.date" class="w-full" type="date"/>
        </UFormField>
        <UFormField label="Nombre de jours" name="daysNumber" required>
          <UInput v-model="form.daysNumber" class="w-full" type="number"/>
        </UFormField>
        <UCheckbox v-model="form.isElectric" class="w-full" label="Voiture électrique"/>
        <UButton class="w-full" type="submit">Créer la réservation</UButton>
      </UForm>
    </template>
  </UModal>
</template>

<style scoped>

</style>