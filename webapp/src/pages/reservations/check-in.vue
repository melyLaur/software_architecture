<script lang="ts" setup>
import {ref, onMounted} from 'vue';
import {useRoute} from 'vue-router';
import {useReservation} from "~/composables/reservations/useReservation";
import type {Reservation} from "~/types/reservation";
import dayjs from 'dayjs'
import 'dayjs/locale/fr';

dayjs.locale('fr');

const route = useRoute();
const {isLoading, isError, getReservationSlotBy, checkInReservation} = useReservation()

const image = new URL('~/assets/images/parking.jpeg', import.meta.url).href;
const logo = new URL('~/assets/images/parkslot.png', import.meta.url).href;
const slot = ref<Reservation>();


onMounted(async () => {
  const token = String(route.query.token);
  slot.value = await getReservationSlotBy();
  if (slot.value) {
    await checkInReservation(token);
  }
});
</script>

<template>
  <div class="h-screen w-screen bg-black text-white flex p-4 pb-12" style="min-height: 710px; min-width: 310px; max-height: 1000px; max-width: 450px; overflow: hidden;">

    <div v-if="!slot" class="flex flex-col space-y-10 w-full max-w-2xl px-4 text-center">
      <img :src="logo" alt="Logo" class="w-64 h-auto mx-auto mb-4"/>

      <h1 class="text-3xl font-bold text-center">Réservation non confirmée</h1>

      <div v-if="isLoading">
        <UIcon name="i-heroicons-x-circle-solid"/>
      </div>
      <div v-if="isError">
        <p class="text-center">Aucune réservation trouvée pour ce lien de confirmation. Veuillez vérifier le lien ou
          contacter le support.</p>
      </div>
    </div>

    <div v-else-if="isError === true" class="flex flex-col space-y-8 w-full max-w-2xl px-4">
      <img :src="logo" alt="Logo" class="w-64 h-auto mx-auto mb-4"/>

      <h1 class="text-2xl font-bold text-center">Réservation non confirmée</h1>

      <div
          class="relative rounded-lg flex flex-col px-4 py-3 text-white font-medium overflow-hidden border-1 border-gray-700 h-38">
        <div
            :style="{ backgroundImage: `url(${image})`, backgroundSize: 'cover', backgroundPosition: 'center' }"
            class="absolute inset-0"/>
        <div class="absolute inset-0 bg-black opacity-50"/>
        <div v-if="isLoading" class="absolute bottom-7 right-7 text-green-400 text-6xl">
          <UIcon class="animate-spin text-3xl" name="i-lucide-loader"/>
        </div>
        <div v-else class="absolute bottom-3 right-3 text-red-500 text-6xl">
          <UIcon name="i-heroicons-x-circle-solid"/>
        </div>

        <div class="flex flex-col space-y-5 relative z-10">
          <div class="flex space-y-5 justify-between items-center">
            <div class="flex flex-col space-y-2">
              <div class="text-xs">
                <span class="mr-1.5">Place</span>
                <span
                    v-if="slot?.isElectric"
                    class="bg-primary-400 text-black px-3 py-1 rounded-sm">Électrique</span>
              </div>

              <div v-if="!slot" class="text-3xl font-bold">Disponible</div>
              <div v-else class="text-3xl font-bold">A-0{{ slot?.parkingNumber }}</div>
            </div>
          </div>
          <div v-if="!slot" class="mt-3 text-xl">
            Réserver votre place
          </div>
          <div v-else>
            <div class="text-xs">Jour réservé</div>
            <div class="text-xl font-medium">{{ dayjs(slot.date).format('ddd DD MMM YY') }}</div>
          </div>
        </div>
      </div>
      <p class="text-center">Votre réservation n'a pas pu être confirmée. Veuillez vérifier le lien de confirmation ou
        contacter le support.</p>

    </div>

    <div v-else class="flex flex-col space-y-8 w-full max-w-2xl px-4">
      <img :src="logo" alt="Logo" class="w-64 h-auto mx-auto mb-4"/>

      <h1 class="text-3xl font-bold text-center">Réservation confirmé</h1>

      <div
          class="relative rounded-lg flex flex-col px-4 py-3 text-white font-medium overflow-hidden border-1 border-gray-700 h-38">
        <div
            :style="{ backgroundImage: `url(${image})`, backgroundSize: 'cover', backgroundPosition: 'center' }"
            class="absolute inset-0"/>
        <div class="absolute inset-0 bg-black opacity-50"/>

        <div v-if="isLoading" class="absolute bottom-7 right-7 text-green-400 text-6xl">
          <UIcon class="animate-spin text-3xl" name="i-lucide-loader"/>
        </div>
        <div v-else class="absolute bottom-3 right-3 text-green-400 text-6xl">
          <UIcon name="i-heroicons-check-circle-solid"/>
        </div>

        <div class="flex flex-col space-y-5 relative z-10">
          <div class="flex space-y-5 justify-between items-center">
            <div class="flex flex-col space-y-2">
              <div class="text-xs">
                <span class="mr-1.5">Place</span>
                <span
                    v-if="slot?.isElectric"
                    class="bg-primary-400 text-black px-3 py-1 rounded-sm">Électrique</span>
              </div>

              <div v-if="!slot" class="text-3xl font-bold">Disponible</div>
              <div v-else class="text-3xl font-bold">A-0{{ slot?.parkingNumber }}</div>
            </div>
          </div>
          <div v-if="!slot" class="mt-3 text-xl">
            Réserver votre place
          </div>
          <div v-else>
            <div class="text-xs">Jour réservé</div>
            <div class="text-2xl font-medium">{{ dayjs(slot.date).format('ddd DD MMM YY') }}</div>
          </div>
        </div>
      </div>
      <p class="text-center px-8">
        Votre réservation a été confirmée avec succès.
      </p>
    </div>

  </div>
</template>

<style scoped>

</style>