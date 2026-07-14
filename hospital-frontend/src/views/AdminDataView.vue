<template>
  <div>
    <h1 class="page-title">药品咨询推送</h1>

    <div class="card" style="margin-bottom:24px">
      <h3 style="color:#2d8f5e;margin-bottom:14px">发布推送</h3>
      <div class="form-group">
        <label>标题</label>
        <input v-model="push.title" placeholder="推送标题" />
      </div>
      <div class="form-group">
        <label>内容</label>
        <textarea v-model="push.content" rows="3" placeholder="推送内容"></textarea>
      </div>
      <div class="push-row">
        <span></span>
        <button class="btn btn-primary" @click="createPush">发布推送</button>
      </div>
      <p v-if="pushMsg" :class="pushMsgType">{{ pushMsg }}</p>
    </div>

    <h2 class="page-title" style="font-size:20px">已发布的推送</h2>
    <div v-if="pushes.length === 0" class="empty">暂无推送</div>
    <div v-for="p in pushes" :key="p.id" class="card" style="margin-bottom:12px">
      <h3 style="color:#2d8f5e">{{ p.title }}</h3>
      <p style="color:#666;margin-top:8px;line-height:1.6">{{ p.content }}</p>
      <div class="push-footer">
        <span style="color:#999;font-size:12px">{{ p.pushTime }}</span>
        <button class="btn-delete" @click="delPush(p.id)">删除</button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { adminApi } from '@/api/admin'
import type { MedicinePush } from '@/types'

const pushes = ref<MedicinePush[]>([])
const push = ref({ title: '', content: '' })
const pushMsg = ref('')
const pushMsgType = ref('')

onMounted(async () => {
  try { pushes.value = await adminApi.listPushes() as any } catch {}
})

async function createPush() {
  if (!push.value.title.trim() || !push.value.content.trim()) {
    pushMsg.value = '标题和内容不能为空'; pushMsgType.value = 'error-text'; return
  }
  try {
    await adminApi.createPush(push.value)
    pushMsg.value = '发布成功'; pushMsgType.value = 'success-text'
    push.value = { title: '', content: '' }
    pushes.value = await adminApi.listPushes() as any
  } catch {
    pushMsg.value = '发布失败'; pushMsgType.value = 'error-text'
  }
}

async function delPush(id: number) {
  if (!confirm('确定删除这条推送吗？')) return
  try { await adminApi.deletePush(id); pushes.value = pushes.value.filter(p => p.id !== id) } catch {}
}
</script>

<style scoped>
.push-row { display: flex; justify-content: space-between; align-items: center; margin-top: 8px; }
textarea { resize: none; font-family: inherit; }
.push-footer { display: flex; justify-content: space-between; align-items: center; margin-top: 8px; }
.btn-delete { background: none; border: 1px solid #e0e0e0; color: #999; font-size: 12px; cursor: pointer; padding: 4px 12px; border-radius: 4px; }
.btn-delete:hover { color: #e74c3c; border-color: #e74c3c; }
</style>
