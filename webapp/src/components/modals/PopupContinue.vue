<script setup lang="ts">

defineProps<{
  message: string
}>()

const emit = defineEmits(['close'])


function confirm() {
  emit('close', true)
}

function cancel() {
  emit('close')
}
</script>

<template>
  <UModal title="Confirmation" @close="cancel">
    <template #body>
      <div class="text-center space-y-4">
        <p>{{ message }}</p>
        <div class="flex justify-center gap-4">
          <UButton  @click="cancel">Non</UButton>
          <UButton color="error" @click="confirm">Oui</UButton>
        </div>
      </div>
    </template>
  </UModal>
</template>