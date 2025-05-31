import {z} from 'zod';
import dayjs from 'dayjs';
import 'dayjs/locale/fr'

dayjs.locale('fr')

export const TODAY_RESERVATION_DEADLINE_HOUR = 11; //11:00 AM
const messageDate = 'La date de réservation doit être supérieure ou égale à aujourd’hui et pas les week-ends.';

const date = z.string().refine((date) => {
    const today = dayjs();
    const selectedDate = dayjs(date);
    return !selectedDate.isBefore(today, 'day');

}, messageDate);

export const createReservationFormSchema = z.object({
    date: date,
    isElectric: z.boolean(),
})
export type CreateReservationFormSchema = z.infer<typeof createReservationFormSchema>;

export const createReservationManagerFormSchema = z.object({
    date: date,
    daysNumber: z.number()
        .min(1, 'Le nombre de jours doit être supérieur à 0')
        .max(30, 'Le nombre de jours ne peut pas dépasser 30')
        .default(1),
    isElectric: z.boolean(),
})
export type CreateReservationManagerFormSchema = z.infer<typeof createReservationManagerFormSchema>;