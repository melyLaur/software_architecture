import type {Reservation} from "~/types/reservation";
import {useReservationApi} from "~/services/reservations/reservation.api";

export const useReservation = () => {
    const isLoading = ref(false)
    const isError = ref(false)
    const reservationSlots = ref<Reservation[]>([])

    //------- utils functions
    const setReservationSlots = (slots: Reservation[]) => {
        reservationSlots.value = slots;
    }
    const addReservationSlot = (slot: Reservation) => {
        const index = reservationSlots.value.findIndex(s => s === null);
        if (index !== -1) {
            reservationSlots.value[index] = slot;
        } else {
            reservationSlots.value[0] = slot;
        }
    }
    const removeReservationSlot = (reservationId: string) => {
        const slots = reservationSlots.value
            .filter(slot => slot !== null)
            .filter(slot => slot.reservationId !== reservationId);
        setReservationSlots(slots);
    }


    //------- domain functions
    const getReservationSlotById = async (reservationId: string) => {
        const { getReservationSlotById } = useReservationApi();
        try {
            isLoading.value = true;
            isError.value = false;
            return await getReservationSlotById(reservationId);
        } catch(e) {
            console.error(e);
            isError.value = e?.message || 'Erreur inconnue'
        } finally {
            isLoading.value = false;
        }
    }

    const fetchReservations = async () => {
        const { fetchReservations } = useReservationApi();
        try {
            isLoading.value = true;
            isError.value = false;
            const reservations = await fetchReservations();
            setReservationSlots(reservations);
        } catch(e) {
            console.error(e);
            isError.value = e?.message || 'Erreur inconnue'
        } finally {
            isLoading.value = false;
        }
    }

    const reserveParkingSpace = async (form: { date: string, isElectric: boolean }) => {
        const { reserveParkingSpace } = useReservationApi();
        try {
            isLoading.value = true;
            isError.value = false;
            const reservation = await reserveParkingSpace({
                date: form.date,
                isElectric: form.isElectric
            })
            setTimeout(() => {
                addReservationSlot(reservation)
                isLoading.value = false
            }, 750);
        } catch(e) {
            console.error(e);
            isError.value = e?.message || 'Erreur inconnue'
            isLoading.value = false
        } finally {

        }
    }

    const reserveParkingSpaceManager = async (form: { date: string, daysNumber: number, isElectric: boolean }) => {
        const { reserveParkingSpaceManager } = useReservationApi();
        try {
            isLoading.value = true;
            isError.value = false;
            const reservations = await reserveParkingSpaceManager({
                bookedFor: form.date,
                daysNumber: form.daysNumber,
                isElectric: form.isElectric
            })
            setTimeout(() => {
                setReservationSlots(reservations)
                isLoading.value = false
            }, 750);
        } catch(e) {
            console.error(e);
            isError.value = e?.message || 'Erreur inconnue'
            isLoading.value = false
        } finally {

        }
    }

    const cancelReservation = async (reservationId: string) => {
        const { cancelReservation } = useReservationApi();
        try {
            isLoading.value = true;
            isError.value = false;
            await cancelReservation({ reservationId: reservationId })
            removeReservationSlot(reservationId);
        } catch(e) {
            console.error(e);
            isError.value = e?.message || 'Erreur inconnue'
        } finally {
            isLoading.value = false;
        }
    }

    const checkInReservation = async (reservationId: string) => {
        const { checkInReservation } = useReservationApi()
        try {
            isLoading.value = true;
            isError.value = false;
            await checkInReservation({ reservationId });
            setTimeout(() => {
                isLoading.value = false;
            }, 750);
        } catch (e) {
            console.error(e);
            isError.value = e?.message || 'Erreur inconnue'
            isLoading.value = false
        }
    }

    return {
        isLoading,
        isError,
        reservationSlots,
        cancelReservation,
        reserveParkingSpace,
        reserveParkingSpaceManager,
        fetchReservations,
        checkInReservation,
        getReservationSlotById,
    }
}