<script lang="ts" setup>
import {useAuthSession} from "~/composables/auth/useAuthSession";

const { getUser } = useAuthSession()

watchEffect(() => {
  const user = getUser()
  if (!user) return

  if (user.role === 'MANAGER') {
    return navigateTo('/admin/dashboard')
  } else if (user.role === 'SECRETARY') {
    return navigateTo('/admin/employees')
  } else {
    return navigateTo('/reservations')
  }
})

</script>

<template>
<div></div>
</template>
