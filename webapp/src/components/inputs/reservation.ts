import {z} from 'zod';
import dayjs from 'dayjs';
import 'dayjs/locale/fr'

dayjs.locale('fr')

const TODAY_RESERVATION_DEADLINE_HOUR = 11; //11:00 AM
const messageDate = 'La date de réservation doit être supérieure ou égale à aujourd’hui et avant 11h00 le jour même et pas les week-ends.';

export const createReservationFormSchema = z.object({
    date: z.string().refine((date) => {
        const today = dayjs();
        const selectedDate = dayjs(date);
        if (selectedDate.isBefore(today, 'day')) {
            return false;
        }
        return !(today.hour() >= TODAY_RESERVATION_DEADLINE_HOUR && selectedDate.isSame(today, 'day'));
    }, messageDate),
    isElectric: z.boolean(),
})
export type CreateReservationFormSchema = z.infer<typeof createReservationFormSchema>;