<script lang="ts" setup>

import {useAuthSession} from "~/composables/auth/useAuthSession";

const { setToken, setUser } = useAuthSession()

setUser({
  id: '00000000-0000-0000-0000-000000000001',
  name: 'Jean Dumont',
  email: 'c.lec@fe.fr',
  role: 'EMPLOYEE',
})
setToken('dummy-token');


</script>

<template>
  <UApp>
    <NuxtLayout>
      <NuxtPage />
    </NuxtLayout>
  </UApp>
</template>
