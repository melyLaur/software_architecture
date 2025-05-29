import {z} from "zod";

export const reserveParkingSpaceDtoSchema = z.object({
    date: z.string(),
    isElectric: z.boolean(),
});
export type ReserveParkingSpaceDto = z.infer<typeof reserveParkingSpaceDtoSchema>;

export const reserveParkingSpaceBodySchema = z.object({
    bookedFor: z.string(),
    electricalPlaceNeeded: z.boolean(),
})
export type ReserveParkingSpaceBody = z.infer<typeof reserveParkingSpaceBodySchema>;

export const reserveParkingSpaceResponseSchema = z.object({
    reservationId: z.string().uuid(),
    date: z.string(),
    parkingNumber: z.string(),
    isElectric: z.boolean(),
});
export type ReserveParkingSpaceResponse = z.infer<typeof reserveParkingSpaceResponseSchema>;