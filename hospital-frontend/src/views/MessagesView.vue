<template>
  <div>
    <h1 class="page-title">问诊留言</h1>

    <!-- 患者发送留言表单 -->
    <div v-if="store.role === 'patient'" class="card" style="margin-bottom:24px">
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
          <div class="reply-row">
            <div style="flex:1">
              <p class="msg-content">👨‍⚕️ {{ m.reply }}</p>
              <p class="msg-time">{{ m.replyTime }}</p>
            </div>
            <button v-if="store.role === 'doctor'" class="btn-delete" @click="delReply(m.id)">删除回复</button>
          </div>
        </div>
        <!-- 医生回复表单 -->
        <div v-if="store.role === 'doctor' && !m.reply" class="reply-form">
          <textarea v-model="replyTexts[m.id]" rows="2" placeholder="输入回复..."></textarea>
          <button class="btn btn-primary btn-sm" @click="reply(m.id)">回复</button>
        </div>
        <div v-if="store.role === 'patient' && !m.reply" class="wait-row">
          <span class="wait-text">{{ m.id === sentMsgId ? '留言发送成功，等待医生回复...' : '等待医生回复...' }}</span>
          <button class="btn-delete" @click="del(m.id)">删除</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { useUserStore } from '@/stores/user'
import { messageApi } from '@/api/message'
import type { Message } from '@/types'

const store = useUserStore()
const messages = ref<Message[]>([])
const loading = ref(false)
const form = ref({ content: '' })
const sending = ref(false)
const deleting = ref<number | null>(null)
const sentMsgId = ref<number | null>(null)
const replyTexts = reactive<Record<number, string>>({})

async function load() {
  loading.value = true
  try {
    if (store.role === 'doctor') {
      messages.value = await messageApi.allList() as any
    } else {
      messages.value = await messageApi.myList() as any
    }
  } catch {}
  loading.value = false
}

onMounted(load)

async function send() {
  if (!form.value.content.trim()) return
  sending.value = true
  try {
    await messageApi.create({ content: form.value.content })
    form.value.content = ''
    await load()
    if (messages.value.length > 0) sentMsgId.value = messages.value[0].id
  } catch (e: any) { alert(e?.message || '发送失败') }
  sending.value = false
}

async function del(id: number) {
  if (!confirm('确定删除这条留言吗？')) return
  deleting.value = id
  try { await messageApi.delete(id); await load() } catch {}
  deleting.value = null
}

async function reply(id: number) {
  const text = replyTexts[id]?.trim()
  if (!text) return
  try { await messageApi.reply(id, text); delete replyTexts[id]; await load() } catch {}
}

async function delReply(id: number) {
  if (!confirm('确定删除该回复吗？')) return
  try { await messageApi.reply(id, ''); await load() } catch {}
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
.reply-row { display: flex; align-items: flex-start; gap: 8px; }
.reply-form { display: flex; gap: 8px; align-items: flex-start; margin-top: 8px; }
.reply-form textarea { flex: 1; }
.btn-delete { background: none; border: 1px solid #e0e0e0; color: #999; font-size: 12px; cursor: pointer; padding: 4px 12px; border-radius: 4px; white-space: nowrap; }
.btn-delete:hover { color: #e74c3c; border-color: #e74c3c; }
.btn-sm { padding: 6px 16px; font-size: 13px; }
textarea { resize: none; font-family: inherit; padding: 10px 14px; border: 1px solid #dcdcdc; border-radius: 6px; font-size: 14px; width: 100%; outline: none; }
textarea:focus { border-color: #2d8f5e; }
</style>
