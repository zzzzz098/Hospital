import request from '@/utils/request'

export const messageApi = {
  create(data: { content: string; did?: number }) {
    return request.post('/api/messages', data)
  },
  myList() {
    return request.get('/api/messages')
  },
  reply(id: number, reply: string) {
    return request.put(`/api/messages/${id}/reply`, { reply })
  },
  allList() {
    return request.get('/api/messages/all')
  }
}
