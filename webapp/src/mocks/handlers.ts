import {http, HttpResponse} from 'msw'

export const handlers = [
    http.post('http://localhost:8000/api/login', async () => {
        return HttpResponse.json()
        // return HttpResponse.error();
    }),

    http.get('http://localhost:8000/api/users/me', () => {
        return HttpResponse.json()
    })
]