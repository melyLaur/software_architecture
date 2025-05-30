import {RequestBuilder} from "~/api/request-builder";
import {
    type ReserveParkingSpaceBody,
    reserveParkingSpaceBodySchema,
    type ReserveParkingSpaceDto,
    type ReserveParkingSpaceManagerBody,
    reserveParkingSpaceManagerBodySchema,
    type ReserveParkingSpaceManagerDto,
    type ReserveParkingSpaceManagerResponse,
    reserveParkingSpaceManagerResponseSchema,
    type ReserveParkingSpaceResponse,
    reserveParkingSpaceResponseSchema
} from "~/services/reservations/dto/reserve-parking-space.dto";
import type {CancelReservationDto} from "~/services/reservations/dto/cancel-reservation.dto";
import {useAuthSession} from "~/composables/auth/useAuthSession";
import {
    type GetReservationSlotByIdResponse,
    getReservationSlotByIdResponseSchema
} from "~/services/reservations/dto/get-reservation-by-id";


export const useReservationApi = () => {
    const config = useRuntimeConfig()
    const RESERVATION_API_URL = `${config.public.apiBase}`
    const isLoading = ref(false)

    const getReservationSlotById = async (reservationId: string) => {
        isLoading.value = true
        return new RequestBuilder(RESERVATION_API_URL)
            .get(`/reservations/${reservationId}`)
            .withResponse<GetReservationSlotByIdResponse>(getReservationSlotByIdResponseSchema)
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

    const reserveParkingSpaceManager = async (requestDto: ReserveParkingSpaceManagerDto) => {
        const {currentUser} = useAuthSession()

        isLoading.value = true
        return new RequestBuilder(RESERVATION_API_URL)
            .post(`/managers/${currentUser.value?.id}/reservations`)
            .withBody<ReserveParkingSpaceManagerBody>(reserveParkingSpaceManagerBodySchema)
            .withResponse<ReserveParkingSpaceManagerResponse>(reserveParkingSpaceManagerResponseSchema)
            .execute({
                body: {
                    bookedFor: requestDto.bookedFor,
                    daysNumber: requestDto.daysNumber,
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

    const checkInReservation = async (requestDto: { reservationId: string }) => {
        isLoading.value = true
        return new RequestBuilder(RESERVATION_API_URL)
            .patch(`/reservations/${requestDto.reservationId}/check-in`)
            .execute()
            .finally(() => (isLoading.value = false))
    }

    return {
        isLoading,
        reserveParkingSpace,
        reserveParkingSpaceManager,
        cancelReservation,
        fetchReservations,
        checkInReservation,
        getReservationSlotById,
    }
}