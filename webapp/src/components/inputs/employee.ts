import {z} from 'zod';
import {EnumRole} from "~/types/role";

export const createEmployeeFormSchema = z.object({
    firstName: z.string().min(1, 'Le prénom est requis').max(50, 'Le prénom ne doit pas dépasser 50 caractères'),
    lastName: z.string().min(1, 'Le nom est requis').max(50, 'Le nom ne doit pas dépasser 50 caractères'),
    email: z.string().email("l'email doit être valide").min(1, "L'email est requis"),
    role: z.nativeEnum(EnumRole, {
        errorMap: () => ({ message: "Le rôle doit être 'EMPLOYEE', 'MANAGER', SECRETARY'" }),
    })
})
export type CreateEmployeeFormSchema = z.infer<typeof createEmployeeFormSchema>;
