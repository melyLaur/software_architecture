import {z} from "zod";

export const getEmployeesResponseSchema = z.array(
  z.object({
    id: z.string(),
    lastName: z.string(),
    firstName: z.string(),
  })
);
export type GetEmployeesResponse = z.infer<typeof getEmployeesResponseSchema>
