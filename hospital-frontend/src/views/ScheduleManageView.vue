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
        <div class="form-group">
          <label>状态</label>
          <select v-model="edits[s.wid].state">
            <option value="预约">预约</option>
            <option value="停诊">停诊</option>
            <option value="已满">已满</option>
          </select>
        </div>
        <div class="form-group">
          <label>号源数</label>
          <input v-model.number="edits[s.wid].nsnum" type="number" min="0" />
        </div>
        <button class="btn btn-primary" style="width:100%" @click="save(s.wid)">保存</button>
        <p v-if="msgs[s.wid]" :class="msgTypes[s.wid]">{{ msgs[s.wid] }}</p>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { doctorApi } from '@/api/doctor'
import { adminApi } from '@/api/admin'
import type { Doctor, WorkDay } from '@/types'

const doctors = ref<Doctor[]>([])
const selectedDid = ref<number | null>(null)
const schedule = ref<WorkDay[]>([])
const loading = ref(false)
const weekDays = ['周日', '周一', '周二', '周三', '周四', '周五', '周六']
const edits = reactive<Record<number, any>>({})
const msgs = reactive<Record<number, string>>({})
const msgTypes = reactive<Record<number, string>>({})

onMounted(async () => {
  try { doctors.value = await doctorApi.list({ size: 100 }) as any; if (doctors.value.length) { selectedDid.value = doctors.value[0].did; loadSchedule() } } catch {}
})

async function loadSchedule() {
  if (!selectedDid.value) return
  loading.value = true
  try {
    schedule.value = await doctorApi.schedule(selectedDid.value) as any
    schedule.value.forEach(s => { edits[s.wid] = { state: s.state, nsnum: s.nsnum } })
  } catch {}
  loading.value = false
}

async function save(wid: number) {
  try {
    await adminApi.updateSchedule(wid, edits[wid])
    msgs[wid] = '保存成功'; msgTypes[wid] = 'success-text'
  } catch {
    msgs[wid] = '保存失败'; msgTypes[wid] = 'error-text'
  }
}
</script>

<style scoped>
.schedule-card { text-align: left; }
</style>
