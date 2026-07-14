<template>
  <div>
    <h1 class="page-title">患者档案管理</h1>
    <div v-if="loading" class="loading">加载中...</div>
    <div v-else-if="patients.length === 0" class="empty">暂无患者数据</div>
    <table v-else class="data-table">
      <thead>
        <tr><th>ID</th><th>账号</th><th>姓名</th><th>邮箱</th><th>诚信度</th></tr>
      </thead>
      <tbody>
        <tr v-for="p in patients" :key="p.id">
          <td>{{ p.id }}</td>
          <td>{{ p.account }}</td>
          <td>{{ p.name }}</td>
          <td>{{ p.email }}</td>
          <td><span :class="['tag', p.integrity >= 80 ? 'tag-success' : 'tag-warning']">{{ p.integrity }}</span></td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { adminApi } from '@/api/admin'

const patients = ref<any[]>([])
const loading = ref(false)

onMounted(async () => {
  loading.value = true
  try { patients.value = await adminApi.patients() as any } catch {}
  loading.value = false
})
</script>

<style scoped>
.data-table { width: 100%; border-collapse: collapse; background: #fff; border-radius: 10px; overflow: hidden; box-shadow: 0 2px 12px rgba(0,0,0,0.08); }
.data-table th { background: #2d8f5e; color: #fff; padding: 14px; text-align: left; font-size: 14px; }
.data-table td { padding: 14px; border-bottom: 1px solid #f0f0f0; font-size: 14px; }
.data-table tr:hover td { background: #f9fdfa; }
</style>
