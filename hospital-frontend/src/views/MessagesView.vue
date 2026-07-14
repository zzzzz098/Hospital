<template>
  <div>
    <h1 class="page-title">问诊留言</h1>
    <div class="card" style="margin-bottom:24px">
      <h3 style="color:#2d8f5e;margin-bottom:14px">发送留言</h3>
      <div class="form-group">
        <textarea v-model="form.content" rows="3" placeholder="请输入您的问诊问题..."></textarea>
      </div>
      <div class="send-row">
        <span></span>
        <button class="btn btn-primary" @click="send" :disabled="sending">{{ sending ? '发送中...' : '发送留言' }}</button>
      </div>
    </div>

    <div v-if="loading" class="loading">加载中...</div>
    <div v-else-if="messages.length === 0" class="empty">暂无留言</div>
    <div v-else class="msg-list">
      <div v-for="m in messages" :key="m.id" class="card msg-card">
        <div class="msg-bubble">
          <p class="msg-content">💬 {{ m.content }}</p>
          <p class="msg-time">{{ m.createTime }}</p>
        </div>
        <div v-if="m.reply" class="msg-bubble reply">
          <p class="msg-content">👨‍⚕️ {{ m.reply }}</p>
          <p class="msg-time">{{ m.replyTime }}</p>
        </div>
        <div v-else class="wait-row">
          <span class="wait-text">{{ m.id === sentMsgId ? '留言发送成功，等待医生回复...' : '等待医生回复...' }}</span>
          <button class="btn-delete" @click="del(m.id)" :disabled="deleting === m.id">删除</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { messageApi } from '@/api/message'
import type { Message } from '@/types'

const messages = ref<Message[]>([])
const loading = ref(false)
const form = ref({ content: '' })
const sending = ref(false)
const deleting = ref<number | null>(null)
const sentMsgId = ref<number | null>(null)

onMounted(async () => {
  loading.value = true
  try { messages.value = await messageApi.myList() as any } catch {}
  loading.value = false
})

async function send() {
  if (!form.value.content.trim()) return
  sending.value = true
  try {
    await messageApi.create({ content: form.value.content })
    form.value.content = ''
    messages.value = await messageApi.myList() as any
    if (messages.value.length > 0) sentMsgId.value = messages.value[0].id
  } catch (e: any) {
    alert(e?.message || '发送失败')
  }
  sending.value = false
}

async function del(id: number) {
  if (!confirm('确定删除这条留言吗？')) return
  deleting.value = id
  try {
    await messageApi.delete(id)
    messages.value = messages.value.filter(m => m.id !== id)
  } catch {}
  deleting.value = null
}
</script>

<style scoped>
.msg-list { display: flex; flex-direction: column; gap: 12px; }
.msg-bubble { background: #e8f5e9; padding: 14px; border-radius: 10px; margin-bottom: 8px; }
.msg-bubble.reply { background: #e3f2fd; }
.msg-content { font-size: 14px; line-height: 1.6; }
.msg-time { font-size: 12px; color: #999; margin-top: 6px; }
.send-row { display: flex; justify-content: space-between; align-items: center; }
.wait-row { display: flex; justify-content: space-between; align-items: center; padding: 0 4px; }
.wait-text { color: #999; font-size: 13px; }
.btn-delete { background: none; border: 1px solid #e0e0e0; color: #999; font-size: 12px; cursor: pointer; padding: 4px 12px; border-radius: 4px; transition: all 0.2s; }
.btn-delete:hover { color: #e74c3c; border-color: #e74c3c; }
textarea { resize: none; font-family: inherit; padding: 10px 14px; border: 1px solid #dcdcdc; border-radius: 6px; font-size: 14px; width: 100%; outline: none; }
textarea:focus { border-color: #2d8f5e; }
</style>
