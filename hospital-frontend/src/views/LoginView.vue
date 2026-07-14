<template>
  <div class="login-page">
    <div class="login-card card">
      <h1 class="login-title">健康医院在线预约挂号系统</h1>
      <div class="tabs">
        <button :class="['tab', { active: tab === 'login' }]" @click="tab = 'login'">登录</button>
        <button :class="['tab', { active: tab === 'register' }]" @click="tab = 'register'">注册</button>
      </div>

      <form v-if="tab === 'login'" @submit.prevent="handleLogin" class="login-form">
        <div class="form-group">
          <label>账号</label>
          <input v-model="loginForm.account" type="text" placeholder="请输入账号" required />
        </div>
        <div class="form-group">
          <label>密码</label>
          <input v-model="loginForm.password" type="password" placeholder="请输入密码" required />
        </div>
        <p v-if="loginError" class="error-text">{{ loginError }}</p>
        <button type="submit" class="btn btn-primary" style="width:100%">登 录</button>
      </form>

      <form v-else @submit.prevent="handleRegister" class="login-form">
        <div class="form-group">
          <label>账号</label>
          <input v-model="regForm.account" type="text" placeholder="请输入8位数字账号" maxlength="8" required />
        </div>
        <div class="form-group">
          <label>姓名</label>
          <input v-model="regForm.name" type="text" placeholder="请输入姓名" required />
        </div>
        <div class="form-group">
          <label>密码</label>
          <input v-model="regForm.password" type="password" placeholder="请输入密码" required />
        </div>
        <p v-if="regError" class="error-text">{{ regError }}</p>
        <p v-if="regSuccess" class="success-text">{{ regSuccess }}</p>
        <button type="submit" class="btn btn-primary" style="width:100%">注 册</button>
      </form>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { authApi } from '@/api/auth'

const router = useRouter()
const route = useRoute()
const store = useUserStore()
const tab = ref('login')
const loginForm = ref({ account: '', password: '' })
const regForm = ref({ account: '', password: '', name: '' })
const loginError = ref('')
const regError = ref('')
const regSuccess = ref('')

async function handleLogin() {
  loginError.value = ''
  try {
    await store.login(loginForm.value.account, loginForm.value.password, '')
    const redirect = route.query.redirect as string || '/'
    router.push(redirect)
  } catch (e: any) {
    loginError.value = e?.response?.data?.message || e?.message || '登录失败'
  }
}

async function handleRegister() {
  regError.value = ''
  regSuccess.value = ''
  if (!/^\d{8}$/.test(regForm.value.account)) {
    regError.value = '账号必须为8位纯数字'; return
  }
  try {
    await authApi.register(regForm.value)
    regSuccess.value = '注册成功！请切换到登录页登录'
    regForm.value = { account: '', password: '', name: '' }
  } catch (e: any) {
    regError.value = e?.response?.data?.message || e?.message || '注册失败'
  }
}
</script>

<style scoped>
.login-page { min-height: 100vh; display: flex; align-items: center; justify-content: center; background: linear-gradient(135deg, #e8f5e9, #c8e6c9, #a5d6a7); }
.login-card { width: 420px; max-width: 90%; }
.login-title { text-align: center; font-size: 24px; color: #2d8f5e; margin-bottom: 24px; }
.tabs { display: flex; margin-bottom: 24px; border-bottom: 2px solid #eee; }
.tab { flex: 1; padding: 12px; background: none; border: none; font-size: 16px; color: #999; cursor: pointer; border-bottom: 2px solid transparent; margin-bottom: -2px; }
.tab.active { color: #2d8f5e; border-bottom-color: #2d8f5e; font-weight: 600; }
.login-form { display: flex; flex-direction: column; gap: 4px; }
</style>
