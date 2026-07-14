import type { Router } from 'vue-router'
import { useUserStore } from '@/stores/user'

export function setupAuthGuard(router: Router) {
  router.beforeEach((to, _from, next) => {
    const store = useUserStore()
    if (to.path === '/login' && store.token) {
      next('/')
      return
    }
    if (to.meta.requiresAuth) {
      if (!store.token) {
        next({ path: '/login', query: { redirect: to.fullPath } })
        return
      }
      if (to.meta.role && store.userInfo?.role !== to.meta.role) {
        next('/')
        return
      }
    }
    next()
  })
}
