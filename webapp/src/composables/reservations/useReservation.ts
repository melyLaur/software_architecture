import type {Reservation} from "~/types/reservation";
import {useReservationApi} from "~/services/reservations/reservation.api";

export const MAX_RESERVATION = 5

export const useReservation = () => {
    const isLoading = ref(false)
    const isError = ref(false)
    const reservationSlots = ref<Reservation[] | null[]>(Array.from({length: MAX_RESERVATION}, () => null))

    //------- utils functions
    const setReservationSlots = (slots: Reservation[]) => {
        if (slots.length > MAX_RESERVATION) {
            console.warn(`Received more slots than expected: ${slots.length}, expected: ${MAX_RESERVATION}`);
            slots = slots.slice(0, MAX_RESERVATION);
        }
        reservationSlots.value = Array.from({length: MAX_RESERVATION}, (_, index) => {
            return slots[index] || null;
        });
    }
    const addReservationSlot = (slot: Reservation) => {
        const index = reservationSlots.value.findIndex(s => s === null);
        if (index !== -1) {
            reservationSlots.value[index] = slot;
        } else {
            console.warn("No empty slot available to add the reservation.");
        }
    }
    const removeReservationSlot = (reservationId: string) => {
        const slots = reservationSlots.value
            .filter(slot => slot !== null)
            .filter(slot => slot.reservationId !== reservationId);
        setReservationSlots(slots);
    }


    //------- domain functions
    const getReservationSlotBy = async () => {
        const { getReservationSlotBy } = useReservationApi();
        try {
            isLoading.value = true;
            isError.value = false;
            return await getReservationSlotBy();
        } catch(e) {
            console.error(e);
            isError.value = true;
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
            isError.value = true;
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
            isError.value = true;
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
            isError.value = true;
        } finally {
            isLoading.value = false;
        }
    }

    const checkInReservation = async (token: string) => {
        const { checkInReservation } = useReservationApi()
        try {
            isLoading.value = true;
            isError.value = false;
            await checkInReservation({ token });
            setTimeout(() => {
                isLoading.value = false;
            }, 750);
        } catch (e) {
            console.error(e);
            isError.value = true;
            isLoading.value = false
        }
    }

    return {
        isLoading,
        isError,
        reservationSlots,
        cancelReservation,
        reserveParkingSpace,
        fetchReservations,
        checkInReservation,
        getReservationSlotBy,
    }
}