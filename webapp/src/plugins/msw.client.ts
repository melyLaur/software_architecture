// plugins/msw.client.ts
import {defineNuxtPlugin} from '#app'

export default defineNuxtPlugin(async () => {
    if (process.env.NODE_ENV === 'development') {
        const {worker} = await import('@/mocks/browser')
        await worker.start({
            serviceWorker: {
                url: '/mockServiceWorker.js',
            },
            onUnhandledRequest: 'bypass'
        })
        console.debug('[MSW] Mock Service Worker started')
    }
})