import {useAuthSession} from "~/composables/auth/useAuthSession";
import type {Role} from "~/types/role";

export default defineNuxtRouteMiddleware((to) => {
    const {getUser} = useAuthSession();

    const user = getUser()
    const roles = to.meta.roles as Role[] | undefined

    if (!user || (roles && !roles.includes(user?.role))) {
        return navigateTo('/unauthorized')
    }
})