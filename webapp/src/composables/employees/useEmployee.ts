import type {Role} from "~/types/role";
import {useEmployeeApi} from "~/services/employees/employee.api";

export const useEmployee = () => {
    const isLoading = ref(false);
    const isError = ref(false);

    const createEmployee = async (employee: { firstName: string, lastName: string, email: string, role: Role }) => {
        const { createEmployee } = useEmployeeApi();
        try {
            isLoading.value = true;
            isError.value = false;
            return await createEmployee(employee);
        } catch (e) {
            console.error(e);
            isError.value = e?.message ?? 'Erreur Inconnue'
        } finally {
            isLoading.value = false;
        }
    }
    
    const updateEmployee = async (employeeId: string, employee: { firstName: string, lastName: string, email?: string, role: Role }) => {
        const { updateEmployee } = useEmployeeApi();
        try {
            isLoading.value = true;
            isError.value = false;
            return await updateEmployee(employeeId, employee);
        } catch (e) {
            console.error(e);
            isError.value = e?.message ?? 'Erreur Inconnue'
        } finally {
            isLoading.value = false;
        }
    }

    const fetchEmployees = async () => {
        const { getEmployees } = useEmployeeApi();
        try {
            isLoading.value = true;
            isError.value = false;
            return await getEmployees();
        } catch (e) {
            console.error(e);
            isError.value = e?.message ?? 'Erreur Inconnue'
        } finally {
            isLoading.value = false;
        }
    }

    const deleteEmployee = async (employeeId: string) => {
        const { deleteEmployee } = useEmployeeApi();
        try {
            isLoading.value = true;
            isError.value = false;
            await deleteEmployee(employeeId);
        } catch (e) {
            console.error(e);
            isError.value = e?.message ?? 'Erreur Inconnue'
        } finally {
            isLoading.value = false;
        }
    }

    return {
        isLoading,
        isError,
        createEmployee,
        updateEmployee,
        fetchEmployees,
        deleteEmployee
    };
}