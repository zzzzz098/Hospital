<template>
  <div>
    <h1 class="page-title">排班管理</h1>
    <div class="card" style="margin-bottom:24px">
      <div class="form-group">
        <label>选择医生</label>
        <select v-model="selectedDid" @change="loadSchedule">
          <option v-for="d in doctors" :key="d.did" :value="d.did">{{ d.dname }} - {{ d.office }}</option>
        </select>
      </div>
    </div>

    <div v-if="loading" class="loading">加载中...</div>
    <div v-else-if="schedule.length === 0" class="empty">暂无排班数据</div>
    <div v-else class="grid-3">
      <div v-for="s in schedule" :key="s.wid" class="card schedule-card">
        <h3 style="color:#2d8f5e;margin-bottom:12px">{{ weekDays[Number(s.worktime)] }} {{ s.ampm }}</h3>
        <p>状态：<span :class="s.state === '预约' ? 'tag tag-success' : s.state === '停诊' ? 'tag tag-danger' : 'tag tag-warning'">{{ s.state }}</span></p>
        <p style="margin-top:6px">号源数：<strong>{{ s.nsnum }}</strong></p>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { doctorApi } from '@/api/doctor'
import type { Doctor, WorkDay } from '@/types'

const doctors = ref<Doctor[]>([])
const selectedDid = ref<number | null>(null)
const schedule = ref<WorkDay[]>([])
const loading = ref(false)
const weekDays = ['周日', '周一', '周二', '周三', '周四', '周五', '周六']

onMounted(async () => {
  try { doctors.value = await doctorApi.list({ size: 100 }) as any; if (doctors.value.length) { selectedDid.value = doctors.value[0].did; loadSchedule() } } catch {}
})

async function loadSchedule() {
  if (!selectedDid.value) return
  loading.value = true
  try { schedule.value = await doctorApi.schedule(selectedDid.value) as any } catch {}
  loading.value = false
}
</script>

<style scoped>
.schedule-card { text-align: center; }
</style>
