import {z} from 'zod';

export const createEmployeeBodySchema = z.object({
  firstName: z.string().min(1, "Le nom est requis"),
  lastName: z.string().min(1, "Le nom est requis"),
  email: z.string().email("Email invalide"),
  role: z.enum(["EMPLOYEE", "MANAGER", "SECRETARY"]),
});
export type CreateEmployeeBody = z.infer<typeof createEmployeeBodySchema>;

export const updateEmployeeBodySchema = z.object({
  firstName: z.string().min(1, "Le nom est requis"),
  lastName: z.string().min(1, "Le nom est requis"),
  email: z.string().email().optional(),
  role: z.enum(["EMPLOYEE", "MANAGER", "SECRETARY"]),
});
export type UpdateEmployeeBody = z.infer<typeof updateEmployeeBodySchema>;
