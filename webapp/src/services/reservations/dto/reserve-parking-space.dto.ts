import {z} from "zod";

export const reserveParkingSpaceDtoSchema = z.object({
    date: z.string(),
    isElectric: z.boolean(),
});
export type ReserveParkingSpaceDto = z.infer<typeof reserveParkingSpaceDtoSchema>;

export const reserveParkingSpaceManagerDtoSchema = z.object({
    bookedFor: z.string(),
    daysNumber: z.number(),
    isElectric: z.boolean(),
});
export type ReserveParkingSpaceManagerDto = z.infer<typeof reserveParkingSpaceManagerDtoSchema>;

export const reserveParkingSpaceBodySchema = z.object({
    bookedFor: z.string(),
    electricalPlaceNeeded: z.boolean(),
})
export type ReserveParkingSpaceBody = z.infer<typeof reserveParkingSpaceBodySchema>;

export const reserveParkingSpaceManagerBodySchema = z.object({
    bookedFor: z.string(),
    daysNumber: z.number(),
    electricalPlaceNeeded: z.boolean(),
})
export type ReserveParkingSpaceManagerBody = z.infer<typeof reserveParkingSpaceManagerBodySchema>;

export const reserveParkingSpaceManagerResponseSchema = z.array(z.object({
    reservationId: z.string().uuid(),
    date: z.string(),
    parkingNumber: z.string(),
    isElectric: z.boolean(),
}));
export type ReserveParkingSpaceManagerResponse = z.infer<typeof reserveParkingSpaceManagerResponseSchema>;

export const reserveParkingSpaceResponseSchema = z.object({
    reservationId: z.string().uuid(),
    date: z.string(),
    parkingNumber: z.string(),
    isElectric: z.boolean(),
});
export type ReserveParkingSpaceResponse = z.infer<typeof reserveParkingSpaceResponseSchema>;