<script lang="ts" setup>
import {useAuthSession} from "~/composables/auth/useAuthSession";

const { getUser } = useAuthSession()

watchEffect(() => {
  const user = getUser()
  if (!user) return

  if (user.role === 'manager') {
    return navigateTo('/admin/dashboard')
  } else if (user.role === 'secretary') {
    return navigateTo('/admin/employees')
  } else {
    return navigateTo('/reservations')
  }
})

</script>

<template>
<div></div>
</template>
