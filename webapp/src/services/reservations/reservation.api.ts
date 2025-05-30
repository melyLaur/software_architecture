import {RequestBuilder} from "~/api/request-builder";
import {
    type ReserveParkingSpaceBody,
    reserveParkingSpaceBodySchema,
    type ReserveParkingSpaceDto, type ReserveParkingSpaceResponse, reserveParkingSpaceResponseSchema
} from "~/services/reservations/dto/reserve-parking-space.dto";
import type {CancelReservationDto} from "~/services/reservations/dto/cancel-reservation.dto";
import {useAuthSession} from "~/composables/auth/useAuthSession";


export const useReservationApi = () => {
    const config = useRuntimeConfig()
    const RESERVATION_API_URL = `${config.public.apiBase}`
    const isLoading = ref(false)

    const getReservationSlotBy = async () => {
        isLoading.value = true
        return new RequestBuilder(RESERVATION_API_URL)
            .get('/reservations/:reservationId')
            .execute()
            .finally(() => (isLoading.value = false))
    }

    const fetchReservations = async () => {
        const {currentUser} = useAuthSession()
        isLoading.value = true
        return new RequestBuilder(RESERVATION_API_URL)
            .get(`/employees/${currentUser.value?.id}/reservations`)
            .execute()
            .finally(() => (isLoading.value = false))
    }

    const reserveParkingSpace = async (requestDto: ReserveParkingSpaceDto) => {
        const {currentUser} = useAuthSession()

        isLoading.value = true
        return new RequestBuilder(RESERVATION_API_URL)
            .post(`/employees/${currentUser.value?.id}/reservations`)
            .withBody<ReserveParkingSpaceBody>(reserveParkingSpaceBodySchema)
            .withResponse<ReserveParkingSpaceResponse>(reserveParkingSpaceResponseSchema)
            .execute({
                body: {
                    bookedFor: requestDto.date,
                    electricalPlaceNeeded: requestDto.isElectric
                }
            })
            .finally(() => (isLoading.value = false))
    }

    const cancelReservation = async (requestDto: CancelReservationDto) => {
        isLoading.value = true
        return new RequestBuilder(RESERVATION_API_URL)
            .delete(`/reservations/${requestDto.reservationId}`)
            .execute()
            .finally(() => (isLoading.value = false))
    }

    const checkInReservation = async (requestDto: { token: string }) => {
        isLoading.value = true
        return new RequestBuilder(RESERVATION_API_URL)
            .post('/reservations/check-in')
            .execute()
            .finally(() => (isLoading.value = false))
    }

    return {
        isLoading,
        reserveParkingSpace,
        cancelReservation,
        fetchReservations,
        checkInReservation,
        getReservationSlotBy,
    }
}