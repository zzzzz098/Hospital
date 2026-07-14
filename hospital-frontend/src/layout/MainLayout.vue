<template>
  <div class="layout">
    <header class="header">
      <div class="container header-inner">
        <router-link to="/" class="logo">🏥 在线预约挂号系统</router-link>
        <nav class="nav-links">
          <router-link to="/">首页</router-link>
          <router-link to="/doctors">找医生</router-link>
          <template v-if="store.isLoggedIn">
            <router-link v-if="store.role === 'patient'" to="/my-appointments">我的预约</router-link>
            <router-link v-if="store.role === 'patient'" to="/messages">问诊留言</router-link>
            <router-link v-if="store.role === 'doctor'" to="/schedule-manage">排班管理</router-link>
            <router-link v-if="store.role === 'doctor'" to="/patient-files">患者档案</router-link>
            <router-link v-if="store.role === 'doctor'" to="/admin/data">数据管理</router-link>
          </template>
        </nav>
        <div class="header-right">
          <template v-if="store.isLoggedIn">
            <span class="user-name">{{ store.userInfo?.name }}</span>
            <button class="btn btn-outline" @click="handleLogout">退出</button>
          </template>
          <template v-else>
            <router-link to="/login" class="btn btn-primary">登录 / 注册</router-link>
          </template>
        </div>
      </div>
    </header>
    <main class="main-content">
      <div class="container">
        <router-view />
      </div>
    </main>
    <footer class="footer">
      <div class="container">
        <p>© 2024 在线预约挂号系统 - 便捷就医 · 健康相伴</p>
      </div>
    </footer>
  </div>
</template>

<script setup lang="ts">
import { useUserStore } from '@/stores/user'
import { useRouter } from 'vue-router'
const store = useUserStore()
const router = useRouter()
function handleLogout() { store.logout(); router.push('/') }
</script>

<style scoped>
.layout { min-height: 100vh; display: flex; flex-direction: column; }
.header { background: linear-gradient(135deg, #2d8f5e, #1a6b40); color: #fff; padding: 0 20px; height: 60px; position: fixed; top: 0; left: 0; right: 0; z-index: 100; box-shadow: 0 2px 8px rgba(0,0,0,0.15); }
.header-inner { display: flex; align-items: center; justify-content: space-between; height: 100%; }
.logo { font-size: 20px; font-weight: 700; color: #fff; }
.nav-links { display: flex; gap: 24px; }
.nav-links a { color: rgba(255,255,255,0.85); font-size: 15px; padding: 6px 0; border-bottom: 2px solid transparent; transition: all 0.2s; }
.nav-links a:hover, .nav-links a.router-link-active { color: #fff; border-bottom-color: #fff; }
.header-right { display: flex; align-items: center; gap: 12px; }
.user-name { font-size: 14px; color: rgba(255,255,255,0.9); }
.header .btn-outline { border-color: rgba(255,255,255,0.7); color: #fff; padding: 6px 16px; font-size: 13px; }
.header .btn-outline:hover { background: #fff; color: #2d8f5e; }
.header .btn-primary { background: #fff; color: #2d8f5e; padding: 6px 16px; font-size: 13px; }
.main-content { flex: 1; margin-top: 60px; padding: 30px 0; }
.footer { background: #1a6b40; color: rgba(255,255,255,0.7); text-align: center; padding: 20px; font-size: 13px; margin-top: auto; }
</style>
