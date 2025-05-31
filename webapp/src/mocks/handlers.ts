import {http, HttpResponse} from 'msw'
import type {GetEmployeesResponse} from "~/services/employees/dto/get-employee.dto";

export const handlers = [
    // http.get('http://localhost:8080/employees', async () => {
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

    // Mocking the reservations API
    // http.get('http://localhost:8080/reservations/:reservationId', async () => {
    //     return HttpResponse.json<{ success: boolean, data: ReserveParkingSpaceResponse }>({
    //         success: true,
    //         data: {
    //             reservationId: '1ea2719c-d754-4680-89d4-0c2c9b3f840d',
    //             date: '2023-10-01T10:00:00Z',
    //             parkingNumber: 'A1',
    //             isElectric: false
    //         },
    //     })
    // }),
    // http.post('http://localhost:8080/reservations/check-in', async () => {
    //     return HttpResponse.json<{ success: boolean, data: object }>({
    //         success: true,
    //         data: {}
    //     })
    // }),
    // http.get('http://localhost:8080/reservations', async () => {
    //     return HttpResponse.json<{ success: boolean, data: FetchReservationsResponse }>({
    //         success: true,
    //         data: [
    //             {
    //                 reservationId: '1ea2719c-d754-4680-89d4-0c2c9b3f840d',
    //                 date: '2023-10-01T10:00:00Z',
    //                 parkingNumber: 'A1',
    //                 isElectric: false
    //             },
    //             {
    //                 reservationId: '1b8acd29-581b-4287-b14e-b1c236a5c7b5',
    //                 date: '2023-10-02T11:00:00Z',
    //                 parkingNumber: 'B2',
    //                 isElectric: true
    //             }
    //         ]
    //     })
    // }),
    // http.post('http://localhost:8080/reservations', async () => {
    //     return HttpResponse.json<{ success: boolean, data: ReserveParkingSpaceResponse }>({
    //         success: true,
    //         data: {
    //             reservationId: '1ea2719c-d754-4680-89d4-0c2c9b3f840d',
    //             date: '2023-10-01T10:00:00Z',
    //             parkingNumber: 'A1',
    //             isElectric: true,
    //         }
    //     });
    // }),
    // http.delete('http://localhost:8080/reservations/:reservationId', async () => {
    //     return HttpResponse.json<{ success: boolean }>({
    //         success: true
    //     });
    // })
]