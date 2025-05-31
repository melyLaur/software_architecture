import {RequestBuilder} from "~/api/request-builder";
import {type GetEmployeesResponse, getEmployeesResponseSchema} from "~/services/employees/dto/get-employee.dto";
import {
    type CreateEmployeeBody,
    createEmployeeBodySchema, type UpdateEmployeeBody,
    updateEmployeeBodySchema
} from "~/services/employees/dto/create-employee.dto";


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

    const createEmployee = async (requestDto: CreateEmployeeBody) => {
        isLoading.value = true
        return new RequestBuilder(EMPLOYEE_API_URL)
            .post('/employees')
            .withBody<CreateEmployeeBody>(createEmployeeBodySchema)
            .execute({ body: requestDto })
            .finally(() => (isLoading.value = false))
    }

    const deleteEmployee = async (id: string) => {
        isLoading.value = true
        return new RequestBuilder(EMPLOYEE_API_URL)
            .delete(`/employees/${id}`)
            .execute()
            .finally(() => (isLoading.value = false))
    }

    const updateEmployee = async (id: string, requestDto: UpdateEmployeeBody) => {
        isLoading.value = true
        return new RequestBuilder(EMPLOYEE_API_URL)
            .put(`/employees/${id}`)
            .withBody<UpdateEmployeeBody>(updateEmployeeBodySchema)
            .execute({ body: requestDto })
            .finally(() => (isLoading.value = false))
    }



    return {
        isLoading,
        getEmployees,
        createEmployee,
        deleteEmployee,
        updateEmployee
    }
}