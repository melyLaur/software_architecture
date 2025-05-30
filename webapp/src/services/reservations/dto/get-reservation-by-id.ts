import {z} from "zod";

export const getReservationSlotByIdResponseSchema = z.object({
    reservationId: z.string().uuid(),
    date: z.string(),
    parkingNumber: z.string(),
    isElectric: z.boolean(),
});
export type GetReservationSlotByIdResponse = z.infer<typeof getReservationSlotByIdResponseSchema>;