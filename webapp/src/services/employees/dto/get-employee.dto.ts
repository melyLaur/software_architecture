import {z} from "zod";
import {EnumRole} from "~/types/role"

export const getEmployeesResponseSchema = z.array(
    z.object({
        employeeId: z.string(),
        firstName: z.string(),
        lastName: z.string(),
        email: z.string(),
        role: z.nativeEnum(EnumRole),
    })
);
export type GetEmployeesResponse = z.infer<typeof getEmployeesResponseSchema>
