import type {EnumRole} from "~/types/role";

export interface Employee {
    employeeId: string
    firstName: string,
    lastName: string,
    email: string
    role: EnumRole,
}