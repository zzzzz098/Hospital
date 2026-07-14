<template>
  <div>
    <h1 class="page-title">确认预约</h1>
    <div class="card" style="max-width:500px">
      <p style="font-size:16px;margin-bottom:8px">医生：<strong>{{ doctor?.dname }}</strong></p>
      <p style="font-size:16px;margin-bottom:20px;color:#666">科室：{{ doctor?.office }}</p>
      <div class="form-group">
        <label>选择日期</label>
        <select v-model="selectedDay" @change="loadSlots">
          <option value="">请选择日期</option>
          <option v-for="(d, i) in availableDays" :key="i" :value="d.worktime">{{ d.label }}</option>
        </select>
      </div>
      <div class="form-group">
        <label>选择时段</label>
        <select v-model="selectedSlot">
          <option value="">请选择时段</option>
          <option v-for="s in availableSlots" :key="s.wid" :value="s">{{ s.ampm }} - {{ s.nstime }} ({{ s.state }})</option>
        </select>
      </div>
      <p v-if="msg" :class="msgType">{{ msg }}</p>
      <button class="btn btn-primary" style="width:100%;margin-top:16px" @click="confirm" :disabled="!selectedSlot">确认预约</button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { doctorApi } from '@/api/doctor'
import { appointmentApi } from '@/api/appointment'
import type { Doctor, WorkDay } from '@/types'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()
const doctor = ref<Doctor | null>(null)
const schedule = ref<WorkDay[]>([])
const selectedDay = ref('')
const selectedSlot = ref<any>(null)
const msg = ref('')
const msgType = ref('')

const availableDays = ref<any[]>([])

function loadSlots() {
  selectedSlot.value = null
}

const dateMap: Record<number, string> = { 0:'2026-06-07', 1:'2026-06-08', 2:'2026-06-02', 3:'2026-06-03', 4:'2026-06-04', 5:'2026-06-05', 6:'2026-06-06' }
function getDateForDay(dayNum: number) { return dateMap[dayNum] || '' }

onMounted(async () => {
  const did = Number(route.params.did)
  try {
    doctor.value = await doctorApi.detail(did) as any
    schedule.value = await doctorApi.schedule(did) as any
    availableDays.value = [...new Set(schedule.value.map(s => s.worktime))]
      .map((w: string) => ({ worktime: w, label: getDateForDay(Number(w)) }))
      .sort((a, b) => a.label.localeCompare(b.label))
  } catch {}
})

const availableSlots = ref<any[]>([])
// Watch selectedDay
import { watch } from 'vue'
watch(selectedDay, (day) => {
  if (!day) { availableSlots.value = []; return }
  availableSlots.value = schedule.value.filter(s => s.worktime === day && s.state === '预约')
})

async function confirm() {
  if (!selectedSlot.value) return
  msg.value = ''
  try {
    await appointmentApi.create({
      pid: userStore.userInfo?.id,
      did: doctor.value?.did,
      nid: selectedSlot.value.wid,
      serialnumber: 1,
      visitdate: getDateForDay(Number(selectedDay.value)),
      visitnoon: selectedSlot.value.ampm,
      visittime: selectedSlot.value.nstime
    })
    msg.value = '预约成功！'
    msgType.value = 'success-text'
    setTimeout(() => router.push('/my-appointments'), 1500)
  } catch (e: any) {
    msg.value = e?.response?.data?.message || '预约失败'
    msgType.value = 'error-text'
  }
}
</script>
