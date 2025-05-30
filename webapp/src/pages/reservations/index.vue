<script lang="ts" setup>

import CreateReservationModal from "~/components/modals/reservations/CreateReservationModal.vue";
import CreateReservationManagerModal from "~/components/modals/reservations/CreateReservationManagerModal.vue";
import {useModals} from "~/composables/modals/useModals";
import {useReservation} from "~/composables/reservations/useReservation";
import {useNotify} from "~/composables/useNotify";
import type {Reservation} from "~/types/reservation";
import dayjs from 'dayjs'
import 'dayjs/locale/fr'
import {useAuthSession} from "~/composables/auth/useAuthSession";
import {TODAY_RESERVATION_DEADLINE_HOUR} from "~/components/inputs/reservation";

dayjs.locale('fr');

definePageMeta({
  layout: 'main-layout',
  middleware: ['role'],
  roles: ['EMPLOYEE', 'MANAGER']
})

const {showError, showSuccess} = useNotify()
const {showPopupContinueModal} = useModals()
const {currentUser} = useAuthSession();
const {
  isLoading,
  isError,
  reservationSlots,
  cancelReservation,
  reserveParkingSpace,
  reserveParkingSpaceManager,
  fetchReservations,
} = useReservation();
const image = new URL('~/assets/images/parking.jpeg', import.meta.url).href;

const openCreateReservationModal = ref(false);
const currentReservation = ref<Reservation | undefined>(undefined);

const editReservationMenuItems = ref([
  {
    label: 'Annuler la réservation',
    icon: 'mdi-delete',
    onSelect: () => onDeleteReservation()
  }
])

async function onCreateReservation(form: { date: string, isElectric: boolean }) {
  if (!dayjs(form.date).isValid()) {
    showError('La date est invalide.');
    return;
  }

  await reserveParkingSpace({
    date: dayjs(form.date).format('YYYY-MM-DD'),
    isElectric: form.isElectric
  });

  if (isError.value) {
    showError(`Une erreur est survenue lors de la réservation. (${isError.value})`);
    return;
  }
  showSuccess('Réservation créée avec succès.');
  openCreateReservationModal.value = false;
}

async function onCreateReservationManager(form: { date: string, daysNumber: number, isElectric: boolean }) {
  if (!dayjs(form.date).isValid()) {
    showError('La date est invalide.');
    return;
  }

  await reserveParkingSpaceManager({
    date: form.date,
    daysNumber: form.daysNumber,
    isElectric: form.isElectric
  });

  if (isError.value) {
    showError(`Une erreur est survenue lors de la réservation. (${isError.value})`);
    return;
  }
  showSuccess('Réservation créée avec succès.');
  openCreateReservationModal.value = false;
}

async function onDeleteReservation() {
  const instance = showPopupContinueModal('Êtes-vous sûr de vouloir supprimer cette réservation ?');
  const result = await instance.result;
  if (result) {
    try {
      await cancelReservation(currentReservation.value!.reservationId);
      showSuccess('Réservation annulée avec succès.');
    } catch (e) {
      console.error(e);
      showError(`Une erreur est survenue lors de l'annulation de la réservation (${isError.value}).`);
    }
  }
}

const isSlotToday = (slot: Reservation) => {
  return dayjs(slot.date).isSame(dayjs(), 'day') && dayjs().hour() < TODAY_RESERVATION_DEADLINE_HOUR;
}

onMounted(() => {
  fetchReservations()
})

</script>

<template>
  <div class="flex flex-col h-full">
    <div class="h-1/12">
      <div class="flex items-center justify-between mb-4">
        <h1 class="text-2xl font-bold">Réservation</h1>
      </div>
    </div>
    <div class="h-11/12 flex flex-col pb-10 space-y-6">
      <div v-if="currentUser?.role === 'MANAGER'" class="rounded">
        <!--  Manager        -->
        <div
            v-for="(slot, index) in Array.from({length: 1}, (_, index) => (reservationSlots[index] || null)).slice(0, 1)"
            :key="index"
            class="relative rounded-lg flex flex-col px-4 py-3 text-white font-medium overflow-hidden"
        >
          <div
              :style="{ backgroundImage: `url(${image})`, backgroundSize: 'cover', backgroundPosition: 'center' }"
              class="absolute inset-0"/>
          <div class="absolute inset-0 bg-black opacity-50"/>
          <div class="relative z-10">
            <div class="flex space-y-6 justify-between items-center">
              <div class="flex flex-col space-y-2">
                <div class="text-xs">
                  <span class="mr-1.5">Place</span>
                  <span v-if="slot?.isElectric" class="bg-primary-400 text-black px-3 py-1 rounded-sm">Électrique</span>
                </div>

                <div v-if="!slot && isLoading">
                  <UIcon class="animate-spin text-3xl" name="i-lucide-loader"/>
                </div>
                <div v-else-if="!slot" class="text-3xl font-bold">Disponible</div>
                <div v-else class="text-3xl font-bold">A-0{{ slot?.parkingNumber }}</div>
              </div>
              <div v-if="!slot" class="absolute top-0 right-0">
                <UButton
                    color="neutral" icon="i-lucide-plus" variant="outline"
                    @click="openCreateReservationModal = true"/>
              </div>
              <div v-else-if="slot && isSlotToday(slot)">
                <UButton
                    color="warning" icon="i-lucide-check" variant="solid"
                    @click="$router.push(`/reservations/check-in?reservationId=${slot.reservationId}`)"
                />
              </div>
              <div v-else class="absolute top-0 right-0">
                <UDropdownMenu
                    :content="{ align: 'start' }"
                    :items="editReservationMenuItems"
                    :ui="{ content: 'w-48' }"
                >
                  <UButton color="neutral" icon="i-lucide-menu" variant="outline" @click="currentReservation = slot"/>
                </UDropdownMenu>
              </div>
            </div>
            <div v-if="!slot" class="mt-3 text-xl">
              Réserver votre place
            </div>
            <div v-else>
              <div class="text-xs">Jours réservés</div>
              <div class="text-xl font-medium">
                <span>{{ dayjs(slot.date).format('ddd DD MMM YY') }}</span>
                <span> au </span>
                <span>{{ dayjs(slot.date).add(reservationSlots.length - 1, 'day').format('ddd DD MMM YY') }}</span>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div v-else-if="currentUser?.role === 'EMPLOYEE'" class="rounded">
        <!--  Employee        -->
        <div class="grid grid-cols-2 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-5 gap-4 w-full">
          <div
              v-for="(slot, index) in Array.from({length: 5}, (_, index) => (reservationSlots[index] || null))"
              :key="index"
              class="relative rounded-lg flex flex-col px-4 py-3 text-white font-medium overflow-hidden"
          >
            <div
                :style="{ backgroundImage: `url(${image})`, backgroundSize: 'cover', backgroundPosition: 'center' }"
                class="absolute inset-0"/>
            <div class="absolute inset-0 bg-black opacity-50"/>
            <div class="relative z-10">
              <div class="flex space-y-6 justify-between items-center">
                <div class="flex flex-col space-y-2">
                  <div class="text-xs">
                    <span class="mr-1.5">Place</span>
                    <span
                        v-if="slot?.isElectric"
                        class="bg-primary-400 text-black px-3 py-1 rounded-sm">Électrique</span>
                  </div>

                  <div v-if="!slot && isLoading">
                    <UIcon class="animate-spin text-3xl" name="i-lucide-loader"/>
                  </div>
                  <div v-else-if="!slot" class="text-3xl font-bold">Disponible</div>
                  <div v-else class="text-3xl font-bold">A-0{{ slot?.parkingNumber }}</div>
                </div>
                <div v-if="!slot" class="absolute top-0 right-0">
                  <UButton
                      color="neutral" icon="i-lucide-plus" variant="outline"
                      @click="openCreateReservationModal = true"/>
                </div>
                <div v-else-if="slot && isSlotToday(slot)" class="absolute top-0 right-0">
                  <UButton
                      color="warning" icon="i-lucide-check" variant="solid"
                      @click="$router.push(`/reservations/check-in?reservationId=${slot.reservationId}`)"
                  />
                </div>
                <div v-else class="absolute top-0 right-0">
                  <UDropdownMenu
                      :content="{ align: 'start' }"
                      :items="editReservationMenuItems"
                      :ui="{ content: 'w-48' }"
                  >
                    <UButton color="neutral" icon="i-lucide-menu" variant="outline" @click="currentReservation = slot"/>
                  </UDropdownMenu>
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
        </div>
      </div>

      <!-- Area that takes the remaining space -->
      <div class="flex-1 bg-white rounded shadow p-4">
        <!--        <UTable-->
        <!--            ref="table"-->
        <!--            :columns="columns"-->
        <!--            :data="data"-->
        <!--            :pagination-options="{ getPaginationRowModel: getPaginationRowModel() }"-->
        <!--            class="flex-1 h-full"-->
        <!--            sticky-->
        <!--            @select="onSelect"-->
        <!--        />-->
      </div>

      <CreateReservationModal
          v-if="currentUser?.role === 'EMPLOYEE'"
          v-model:open="openCreateReservationModal"
          @on-submit="onCreateReservation($event)"
      />
      <CreateReservationManagerModal
          v-if="currentUser?.role === 'MANAGER'"
          v-model:open="openCreateReservationModal"
          @on-submit="onCreateReservationManager($event)"
      />
    </div>
  </div>
</template>

<style scoped>

</style>