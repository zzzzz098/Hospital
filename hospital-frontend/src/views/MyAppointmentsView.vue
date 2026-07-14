<template>
  <div>
    <h1 class="page-title">我的预约</h1>
    <div v-if="loading" class="loading">加载中...</div>
    <div v-else-if="list.length === 0" class="empty">暂无预约记录</div>
    <div v-else class="appointment-list">
      <div v-for="a in list" :key="a.rid" class="card appointment-item">
        <div class="appointment-info">
          <div>
            <span :class="['tag', statusTag(a.state)]">{{ a.state }}</span>
            <strong style="margin-left:10px">{{ a.visitdate }}</strong>
            <span style="margin-left:8px;color:#999">{{ a.visitnoon }} {{ a.visittime }}</span>
          </div>
          <div class="appointment-meta">
            <span>序号: {{ a.serialnumber }}</span>
            <span>医生ID: {{ a.did }}</span>
            <span>预约时间: {{ a.ordertime }}</span>
          </div>
        </div>
        <button v-if="a.state === '成功'" class="btn btn-danger" @click="handleCancel(a.rid)" :disabled="canceling === a.rid">
          {{ canceling === a.rid ? '取消中...' : '取消预约' }}
        </button>
      </div>
    </div>
    <p v-if="errorMsg" class="error-text">{{ errorMsg }}</p>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { appointmentApi } from '@/api/appointment'
import type { Appointment } from '@/types'

const list = ref<Appointment[]>([])
const loading = ref(false)
const errorMsg = ref('')
const canceling = ref<number | null>(null)

function statusTag(state: string) {
  const map: any = { '成功': 'tag-success', '取消': 'tag-danger', '完成': 'tag-info', '爽约': 'tag-warning' }
  return map[state] || 'tag-info'
}

onMounted(async () => {
  loading.value = true
  try { list.value = await appointmentApi.myList() as any } catch {}
  loading.value = false
})

async function handleCancel(rid: number) {
  canceling.value = rid
  try {
    await appointmentApi.cancel(rid)
    list.value = list.value.map(a => a.rid === rid ? { ...a, state: '取消' } : a)
  } catch (e: any) {
    errorMsg.value = e?.response?.data?.message || '取消失败'
  }
  canceling.value = null
}
</script>

<style scoped>
.appointment-list { display: flex; flex-direction: column; gap: 12px; }
.appointment-item { display: flex; justify-content: space-between; align-items: center; }
.appointment-meta { color: #999; font-size: 13px; margin-top: 8px; display: flex; gap: 16px; }
@media (max-width: 600px) { .appointment-item { flex-direction: column; gap: 12px; } }
</style>
