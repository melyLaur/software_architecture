<script lang="ts" setup>
import type {FormSubmitEvent} from '@nuxt/ui'
import {createReservationFormSchema, type CreateReservationFormSchema} from "~/components/inputs/reservation";

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

const form = ref<CreateReservationFormSchema>({
  date: '',
  isElectric: false
})

async function onSubmit(event: FormSubmitEvent<CreateReservationFormSchema>) {
  emits('onSubmit', {
    date: event.data.date,
    isElectric: event.data.isElectric
  })
}

</script>

<template>
  <UModal v-model:open="modelValue" title="Créer une nouvelle réservation" class="">
    <template #body>
      <UForm :schema="createReservationFormSchema" :state="form" class="w-full space-y-6" @submit="onSubmit">
        <UFormField label="Date de réservation" name="date" required>
          <UInput v-model="form.date" class="w-full" type="date"/>
        </UFormField>
        <UCheckbox v-model="form.isElectric" class="w-full" label="Voiture électrique"/>
        <UButton class="w-full" type="submit">Créer la réservation</UButton>
      </UForm>
    </template>
  </UModal>
</template>

<style scoped>

</style>