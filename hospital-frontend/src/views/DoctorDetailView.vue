<template>
  <div v-if="doctor">
    <div class="card doc-info">
      <div class="doc-header">
        <div class="doc-avatar">{{ doctor.dname?.charAt(0) }}</div>
        <div>
          <h1 class="page-title">{{ doctor.dname }}</h1>
          <p><span class="tag tag-info">{{ doctor.career }}</span> <span class="tag tag-success">{{ doctor.office }}</span></p>
          <p style="margin-top:8px;color:#666">{{ doctor.room }} · {{ doctor.gender }} · {{ doctor.age }}岁 · 出诊费 ¥{{ doctor.fee }}</p>
        </div>
      </div>
      <div class="doc-desc">
        <h3>医生介绍</h3>
        <p>{{ doctor.description }}</p>
      </div>
    </div>

    <h2 class="page-title" style="margin-top:30px">排班信息</h2>
    <div v-if="schedule.length === 0" class="empty">暂无排班信息</div>
    <div v-else class="schedule-grid">
      <div v-for="(day, i) in weekDays" :key="i" class="card schedule-day">
        <h3 class="day-title">{{ day }}</h3>
        <div v-for="j in [0, 1]" :key="j">
          <template v-if="getSlot(i, j)">
            <div :class="['slot', getSlot(i, j)?.state === '预约' ? 'slot-open' : 'slot-closed']">
              <span>{{ j === 0 ? '上午' : '下午' }}</span>
              <span>{{ getSlot(i, j)?.state }}</span>
              <span v-if="getSlot(i, j)?.nsnum">号源: {{ getSlot(i, j)?.nsnum }}</span>
            </div>
          </template>
        </div>
      </div>
    </div>

    <div style="margin-top:30px; text-align:center">
      <p v-if="!userStore.isLoggedIn" class="empty">
        请先<router-link to="/login" style="color:#2d8f5e">登录</router-link>后再预约
      </p>
      <router-link v-else-if="userStore.role === 'patient'" :to="`/appointment/${doctor.did}`" class="btn btn-primary" style="font-size:18px;padding:14px 48px">
        立即预约
      </router-link>
    </div>
  </div>
  <div v-else class="loading">加载中...</div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { doctorApi } from '@/api/doctor'
import type { Doctor, WorkDay } from '@/types'

const route = useRoute()
const userStore = useUserStore()
const doctor = ref<Doctor | null>(null)
const schedule = ref<WorkDay[]>([])
const weekDays = ['周日', '周一', '周二', '周三', '周四', '周五', '周六']

function getSlot(dayIndex: number, ampm: number) {
  const d = dayIndex.toString()
  const a = ampm === 0 ? '上午' : '下午'
  return schedule.value.find(s => s.worktime === d && s.ampm === a) || null
}

onMounted(async () => {
  const did = Number(route.params.did)
  try {
    doctor.value = await doctorApi.detail(did) as any
    schedule.value = await doctorApi.schedule(did) as any
  } catch {}
})
</script>

<style scoped>
.doc-header { display: flex; gap: 20px; align-items: center; margin-bottom: 20px; }
.doc-avatar { width: 80px; height: 80px; border-radius: 50%; background: linear-gradient(135deg, #2d8f5e, #4caf50); color: #fff; font-size: 32px; font-weight: 700; display: flex; align-items: center; justify-content: center; flex-shrink: 0; }
.doc-desc h3 { color: #2d8f5e; margin-bottom: 10px; }
.doc-desc p { color: #666; line-height: 1.8; font-size: 14px; }
.schedule-grid { display: grid; grid-template-columns: repeat(7, 1fr); gap: 12px; }
.schedule-day { text-align: center; padding: 16px; }
.day-title { color: #2d8f5e; margin-bottom: 10px; font-size: 15px; }
.slot { padding: 8px; border-radius: 6px; font-size: 13px; display: flex; flex-direction: column; gap: 4px; margin-bottom: 6px; }
.slot-open { background: #e8f5e9; color: #2d8f5e; }
.slot-closed { background: #f5f5f5; color: #999; }
@media (max-width: 800px) { .schedule-grid { grid-template-columns: repeat(4, 1fr); } }
</style>
