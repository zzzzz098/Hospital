<template>
  <div>
    <h1 class="page-title">预约统计</h1>
    <p style="color:#666;margin-bottom:20px">已预约您的患者列表</p>
    <div v-if="loading" class="loading">加载中...</div>
    <div v-else-if="list.length === 0" class="empty">暂无患者预约</div>
    <table v-else class="data-table">
      <thead>
        <tr><th>患者ID</th><th>就诊日期</th><th>时段</th><th>时间</th><th>序号</th><th>状态</th></tr>
      </thead>
      <tbody>
        <tr v-for="a in list" :key="a.rid">
          <td>{{ a.pid }}</td>
          <td>{{ a.visitdate }}</td>
          <td>{{ a.visitnoon }}</td>
          <td>{{ a.visittime }}</td>
          <td>{{ a.serialnumber }}</td>
          <td><span :class="['tag', statusTag(a.state)]">{{ a.state }}</span></td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useUserStore } from '@/stores/user'
import { appointmentApi } from '@/api/appointment'

const store = useUserStore()
const list = ref<any[]>([])
const loading = ref(false)

function statusTag(s: string) { const m: any = { '成功':'tag-success','取消':'tag-danger','完成':'tag-info','爽约':'tag-warning' }; return m[s]||'tag-info' }

onMounted(async () => {
  loading.value = true
  try { list.value = await appointmentApi.allList() as any } catch {}
  loading.value = false
})
</script>

<style scoped>
.data-table { width: 100%; border-collapse: collapse; background: #fff; border-radius: 10px; overflow: hidden; box-shadow: 0 2px 12px rgba(0,0,0,0.08); }
.data-table th { background: #2d8f5e; color: #fff; padding: 14px; text-align: left; font-size: 14px; }
.data-table td { padding: 14px; border-bottom: 1px solid #f0f0f0; font-size: 14px; }
.data-table tr:hover td { background: #f9fdfa; }
</style>
