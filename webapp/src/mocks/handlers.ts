import {http, HttpResponse} from 'msw'
import type {GetEmployeesResponse} from "~/services/employees/dto/get-employee.dto";

export const handlers = [
    // http.get('http://localhost:8080/api/v1/employees', async () => {
    //     return HttpResponse.json<{ success: boolean, data: GetEmployeesResponse }>({
    //         success: true,
    //         data: [
    //             {
    //                 id: '1',
    //                 lastName: 'Doe',
    //                 firstName: 'John'
    //             },
    //             {
    //                 id: '2',
    //                 lastName: 'Smith',
    //                 firstName: 'Jane'
    //             }
    //         ]
    //     })
    // }),
]