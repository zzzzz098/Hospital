import { createApp } from 'vue'
import { createPinia } from 'pinia'
import App from './App.vue'
import router from './router'
import { setupAuthGuard } from './router/guard'
import './style.css'

const app = createApp(App)
app.use(createPinia())
app.use(router)
setupAuthGuard(router)
app.mount('#app')
