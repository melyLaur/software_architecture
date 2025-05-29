import {z} from "zod";

export const cancelReservationDtoSchema = z.object({
    reservationId: z.string().uuid(),
});
export type CancelReservationDto = z.infer<typeof cancelReservationDtoSchema>;