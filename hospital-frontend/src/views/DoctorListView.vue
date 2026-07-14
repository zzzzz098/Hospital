<template>
  <div>
    <h1 class="page-title">找医生</h1>
    <div class="card search-bar">
      <div class="grid-2">
        <div class="form-group">
          <label>科室</label>
          <select v-model="filterOffice" @change="search">
            <option value="">全部科室</option>
            <option v-for="o in offices" :key="o.officename" :value="o.officename">{{ o.officename }}</option>
          </select>
        </div>
        <div class="form-group">
          <label>医生姓名</label>
          <input v-model="filterName" placeholder="输入医生姓名" @keyup.enter="search" />
        </div>
      </div>
    </div>

    <div v-if="loading" class="loading">加载中...</div>
    <div v-else-if="doctors.length === 0" class="empty">暂无医生信息</div>
    <div v-else class="grid-3">
      <router-link v-for="d in doctors" :key="d.did" :to="`/doctors/${d.did}`" class="card doctor-card">
        <div class="doc-avatar">{{ d.dname?.charAt(0) }}</div>
        <h3>{{ d.dname }}</h3>
        <p class="doc-dept">{{ d.office }}</p>
        <p class="doc-title">{{ d.career }}</p>
        <div class="doc-footer">
          <span class="tag tag-success">¥{{ d.fee }}</span>
          <span class="arrow">→</span>
        </div>
      </router-link>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { doctorApi } from '@/api/doctor'
import type { Doctor, Office } from '@/types'

const doctors = ref<Doctor[]>([])
const offices = ref<Office[]>([])
const filterOffice = ref('')
const filterName = ref('')
const loading = ref(false)

async function search() {
  loading.value = true
  try {
    doctors.value = await doctorApi.list({ office: filterOffice.value, name: filterName.value }) as any
  } finally { loading.value = false }
}

onMounted(async () => {
  try { offices.value = await doctorApi.offices() as any } catch {}
  search()
})
</script>

<style scoped>
.search-bar { margin-bottom: 24px; }
.doctor-card { text-align: center; transition: transform 0.2s; cursor: pointer; }
.doctor-card:hover { transform: translateY(-4px); }
.doc-avatar { width: 60px; height: 60px; border-radius: 50%; background: linear-gradient(135deg, #2d8f5e, #4caf50); color: #fff; font-size: 24px; font-weight: 700; display: flex; align-items: center; justify-content: center; margin: 0 auto 12px; }
.doctor-card h3 { font-size: 17px; color: #333; margin-bottom: 4px; }
.doc-dept { font-size: 13px; color: #2d8f5e; margin-bottom: 4px; }
.doc-title { font-size: 13px; color: #999; margin-bottom: 12px; }
.doc-footer { display: flex; justify-content: space-between; align-items: center; }
.arrow { color: #2d8f5e; font-size: 18px; }
</style>
