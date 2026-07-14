<template>
  <div class="home">
    <!-- 医生欢迎区 -->
    <section v-if="store.role === 'doctor'" class="hero doctor-hero">
      <h1>欢迎{{ store.userInfo?.name }}</h1>
      <p class="subtitle">健康医院在线预约挂号系统</p>
    </section>

    <!-- 患者/未登录英雄区 -->
    <section v-else class="hero">
      <h1>在线预约挂号系统</h1>
      <p class="subtitle">便捷就医 · 健康相伴</p>
      <router-link to="/doctors" class="btn btn-primary hero-btn">立即预约挂号</router-link>
    </section>

    <section class="features">
      <h2 class="section-title">系统功能</h2>
      <div class="grid-3">
        <router-link v-for="f in visibleFeatures" :key="f.title" :to="f.link" class="card feature-card">
          <div class="feature-icon">{{ f.icon }}</div>
          <h3>{{ f.title }}</h3>
          <p>{{ f.desc }}</p>
        </router-link>
      </div>
    </section>

    <section class="pushed" v-if="pushes.length">
      <h2 class="section-title">💊 药品咨询</h2>
      <div v-for="p in pushes" :key="p.id" class="card push-card">
        <h3>{{ p.title }}</h3>
        <p>{{ p.content }}</p>
        <span class="push-time">{{ p.pushTime }}</span>
      </div>
    </section>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useUserStore } from '@/stores/user'
import { adminApi } from '@/api/admin'
import type { MedicinePush } from '@/types'

const store = useUserStore()

const patientFeatures = [
  { icon: '🔍', title: '科室医生查询', desc: '按科室或姓名快速查找医生，查看医生详情与排班信息', link: '/doctors' },
  { icon: '📅', title: '在线预约挂号', desc: '选择合适时段在线预约，方便快捷免排队', link: '/doctors' },
  { icon: '📋', title: '预约记录管理', desc: '随时查看预约记录，支持取消或修改预约', link: '/my-appointments' },
  { icon: '💬', title: '在线问诊留言', desc: '向医生在线留言咨询，获取专业医疗建议', link: '/messages' },
]

const doctorFeatures = [
  { icon: '📊', title: '预约统计', desc: '查看已预约自己的患者列表', link: '/appointment-stats' },
  { icon: '📋', title: '患者档案管理', desc: '查看所有患者的档案信息', link: '/patient-files' },
  { icon: '💬', title: '问诊留言', desc: '回复患者的留言咨询，可删除自己的回复', link: '/messages' },
  { icon: '💊', title: '药品推送', desc: '编写推送药品咨询信息给患者', link: '/admin/data' },
  { icon: '📅', title: '排班管理', desc: '查看和管理所有医生的排班信息', link: '/schedule-manage' },
]

const guestFeatures = [
  { icon: '🔍', title: '科室医生查询', desc: '按科室或姓名快速查找医生，查看医生详情与排班信息', link: '/doctors' },
  { icon: '📅', title: '在线预约挂号', desc: '选择合适时段在线预约，方便快捷免排队', link: '/doctors' },
  { icon: '💊', title: '药品咨询', desc: '及时获取药品资讯与用药指导', link: '/' },
]

const visibleFeatures = computed(() => {
  if (store.role === 'doctor') return doctorFeatures
  if (store.role === 'patient') return patientFeatures
  return guestFeatures
})

const pushes = ref<MedicinePush[]>([])

onMounted(async () => {
  try { pushes.value = await adminApi.listPushes() as any } catch {}
})
</script>

<style scoped>
.hero { background: linear-gradient(135deg, #2d8f5e 0%, #1a6b40 50%, #0d4f28 100%); color: #fff; text-align: center; padding: 80px 20px; margin-bottom: 40px; border-radius: 0 0 40px 40px; }
.hero h1 { font-size: 42px; font-weight: 800; margin-bottom: 12px; letter-spacing: 4px; }
.doctor-hero h1 { font-size: 36px; }
.doctor-hero { padding: 50px 20px; }
.subtitle { font-size: 20px; opacity: 0.9; margin-bottom: 32px; }
.hero-btn { font-size: 18px; padding: 14px 40px; background: #fff; color: #2d8f5e; }
.hero-btn:hover { background: #e8f5e9; }
.section-title { text-align: center; font-size: 28px; color: #2d8f5e; margin-bottom: 30px; }
.features { margin-bottom: 40px; }
.feature-card { text-align: center; padding: 30px 20px; transition: transform 0.2s; }
.feature-card:hover { transform: translateY(-4px); }
.feature-icon { font-size: 40px; margin-bottom: 14px; }
.feature-card h3 { color: #2d8f5e; margin-bottom: 10px; font-size: 17px; }
.feature-card p { color: #777; font-size: 14px; line-height: 1.6; }
.pushed { margin-bottom: 40px; }
.push-card { margin-bottom: 16px; }
.push-card h3 { color: #2d8f5e; margin-bottom: 8px; }
.push-card p { color: #555; font-size: 14px; line-height: 1.6; }
.push-time { color: #999; font-size: 12px; }
</style>
