import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import { resolve } from 'path'

export default defineConfig({
  plugins: [vue()],
  resolve: {
    alias: { '@': resolve(__dirname, 'src') }
  },
  server: {
    port: 9191,
    proxy: {
      '/api': { target: 'http://localhost:9190', changeOrigin: true },
      '/auth': { target: 'http://localhost:9190', changeOrigin: true }
    }
  }
})
