import {z} from "zod";

export const fetchReservationsResponseSchema = z.array(z.object({
    reservationId: z.string().uuid(),
    date: z.string(),
    parkingNumber: z.string(),
    isElectric: z.boolean(),
}));
export type FetchReservationsResponse = z.infer<typeof fetchReservationsResponseSchema>;