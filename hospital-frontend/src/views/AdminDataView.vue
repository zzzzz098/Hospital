<template>
  <div>
    <h1 class="page-title">后台数据管理</h1>

    <div class="grid-4 stats" style="margin-bottom:30px">
      <div class="card stat-card"><h2>{{ stats.patients }}</h2><p>患者总数</p></div>
      <div class="card stat-card"><h2>{{ stats.doctors }}</h2><p>医生总数</p></div>
      <div class="card stat-card"><h2>{{ stats.offices }}</h2><p>科室数量</p></div>
      <div class="card stat-card"><h2>{{ stats.appointments }}</h2><p>预约总数</p></div>
    </div>

    <h2 class="page-title" style="font-size:20px">所有预约记录</h2>
    <div v-if="appointments.length === 0" class="empty">暂无预约数据</div>
    <table v-else class="data-table" style="margin-bottom:30px">
      <thead>
        <tr><th>ID</th><th>患者ID</th><th>医生ID</th><th>日期</th><th>时段</th><th>状态</th><th>预约时间</th></tr>
      </thead>
      <tbody>
        <tr v-for="a in appointments" :key="a.rid">
          <td>{{ a.rid }}</td><td>{{ a.pid }}</td><td>{{ a.did }}</td>
          <td>{{ a.visitdate }}</td><td>{{ a.visitnoon }}</td>
          <td><span :class="['tag', statusTag(a.state)]">{{ a.state }}</span></td>
          <td>{{ a.ordertime }}</td>
        </tr>
      </tbody>
    </table>

    <h2 class="page-title" style="font-size:20px">药品咨询推送</h2>
    <div class="card" style="margin-bottom:24px">
      <div class="form-group"><label>标题</label><input v-model="push.title" placeholder="推送标题" /></div>
      <div class="form-group"><label>内容</label><textarea v-model="push.content" rows="3" placeholder="推送内容"></textarea></div>
      <button class="btn btn-primary" @click="createPush">发布推送</button>
      <p v-if="pushMsg" :class="pushMsgType">{{ pushMsg }}</p>
    </div>

    <div v-for="p in pushes" :key="p.id" class="card" style="margin-bottom:12px">
      <h3 style="color:#2d8f5e">{{ p.title }}</h3>
      <p style="color:#666;margin-top:8px">{{ p.content }}</p>
      <span style="color:#999;font-size:12px">{{ p.pushTime }}</span>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { adminApi } from '@/api/admin'
import type { MedicinePush, Appointment } from '@/types'

const stats = ref({ patients: 0, doctors: 0, offices: 0, appointments: 0 })
const appointments = ref<Appointment[]>([])
const pushes = ref<MedicinePush[]>([])
const push = ref({ title: '', content: '' })
const pushMsg = ref('')
const pushMsgType = ref('')

function statusTag(s: string) { const m: any = { '成功':'tag-success','取消':'tag-danger','完成':'tag-info','爽约':'tag-warning' }; return m[s]||'tag-info' }

onMounted(async () => {
  try {
    const d = await adminApi.dashboard() as any
    stats.value = d
    appointments.value = await adminApi.allAppointments() as any
    pushes.value = await adminApi.listPushes() as any
  } catch {}
})

async function createPush() {
  try {
    await adminApi.createPush(push.value)
    pushMsg.value = '发布成功'
    pushMsgType.value = 'success-text'
    push.value = { title: '', content: '' }
    pushes.value = await adminApi.listPushes() as any
  } catch {
    pushMsg.value = '发布失败'
    pushMsgType.value = 'error-text'
  }
}
</script>

<style scoped>
.stat-card { text-align: center; }
.stat-card h2 { font-size: 32px; color: #2d8f5e; }
.stat-card p { color: #999; margin-top: 4px; }
.data-table { width: 100%; border-collapse: collapse; background: #fff; border-radius: 10px; overflow: hidden; box-shadow: 0 2px 12px rgba(0,0,0,0.08); }
.data-table th { background: #2d8f5e; color: #fff; padding: 14px; text-align: left; font-size: 14px; }
.data-table td { padding: 14px; border-bottom: 1px solid #f0f0f0; font-size: 14px; }
.data-table tr:hover td { background: #f9fdfa; }
textarea { resize: vertical; font-family: inherit; }
</style>
