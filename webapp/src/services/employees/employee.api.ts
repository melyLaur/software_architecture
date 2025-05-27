import {RequestBuilder} from "~/api/request-builder";
import {type GetEmployeesResponse, getEmployeesResponseSchema} from "~/services/employees/dto/get-employee.dto";


export const useEmployeeApi = () => {
    const config = useRuntimeConfig()
    const EMPLOYEE_API_URL = `${config.public.apiBase}`
    const isLoading = ref(false)

    const getEmployees = async () => {
        isLoading.value = true
        return new RequestBuilder(EMPLOYEE_API_URL)
            .get('/employees')
            .withResponse<GetEmployeesResponse>(getEmployeesResponseSchema)
            .execute()
            .finally(() => (isLoading.value = false))
    }

    return {
        isLoading,
        getEmployees
    }
}