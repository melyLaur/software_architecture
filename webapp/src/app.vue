<script lang="ts" setup>

import {useAuthSession} from "~/composables/auth/useAuthSession";

const { setToken, setUser } = useAuthSession()

setUser({
  id: 1,
  name: 'Jean',
  email: 'jean@toto.fr',
  role: 'secretary',
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
