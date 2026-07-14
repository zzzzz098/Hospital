<template>
  <div>
    <h1 class="page-title">问诊留言</h1>
    <div class="card" style="margin-bottom:24px">
      <h3 style="color:#2d8f5e;margin-bottom:14px">发送留言</h3>
      <div class="form-group">
        <label>留言内容</label>
        <textarea v-model="form.content" rows="4" placeholder="请输入您的问诊问题..."></textarea>
      </div>
      <button class="btn btn-primary" @click="send" :disabled="sending">{{ sending ? '发送中...' : '发送留言' }}</button>
      <p v-if="formMsg" :class="formMsgType">{{ formMsg }}</p>
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
        <p v-else style="color:#999;font-size:13px">等待医生回复...</p>
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
const formMsg = ref('')
const formMsgType = ref('')

onMounted(async () => {
  loading.value = true
  try { messages.value = await messageApi.myList() as any } catch {}
  loading.value = false
})

async function send() {
  if (!form.value.content.trim()) return
  sending.value = true
  formMsg.value = ''
  try {
    await messageApi.create({ content: form.value.content })
    formMsg.value = '留言发送成功！'
    formMsgType.value = 'success-text'
    form.value.content = ''
    messages.value = await messageApi.myList() as any
  } catch (e: any) {
    formMsg.value = e?.response?.data?.message || '发送失败'
    formMsgType.value = 'error-text'
  }
  sending.value = false
}
</script>

<style scoped>
.msg-list { display: flex; flex-direction: column; gap: 12px; }
.msg-bubble { background: #e8f5e9; padding: 14px; border-radius: 10px; margin-bottom: 8px; }
.msg-bubble.reply { background: #e3f2fd; }
.msg-content { font-size: 14px; line-height: 1.6; }
.msg-time { font-size: 12px; color: #999; margin-top: 6px; }
textarea { resize: vertical; font-family: inherit; }
</style>
