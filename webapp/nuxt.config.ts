import tailwindcss from "@tailwindcss/vite";

// https://nuxt.com/docs/api/configuration/nuxt-config
export default defineNuxtConfig({
  srcDir: 'src/',
  ssr: false,
  css: ['@/styles/styles.css'],
  compatibilityDate: '2024-11-01',
  devtools: { enabled: true },
  ui: {
    colorMode: false,
  },
  vite: {
    plugins: [
      tailwindcss()
    ]
  },
  runtimeConfig: {
    public: {
      apiBase: 'http://localhost:8080/api', // À adapter
    }
  },
  modules: [
    '@nuxt/eslint',
    '@nuxt/fonts',
    '@nuxt/icon',
    '@nuxt/ui',
    '@nuxt/test-utils',
    '@pinia/nuxt',
    // '@nuxt/image'
  ],
})