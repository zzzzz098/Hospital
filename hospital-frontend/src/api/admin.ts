import request from '@/utils/request'

export const adminApi = {
  dashboard() {
    return request.get('/api/admin/dashboard')
  },
  patients() {
    return request.get('/api/admin/patients')
  },
  allAppointments() {
    return request.get('/api/admin/appointments')
  },
  createPush(data: { title: string; content: string }) {
    return request.post('/api/admin/medicine-push', data)
  },
  listPushes() {
    return request.get('/api/admin/medicine-push')
  },
  deletePush(id: number) {
    return request.delete(`/api/admin/medicine-push/${id}`)
  },
  updateSchedule(wid: number, data: { state: string; nsnum: number }) {
    return request.put(`/api/admin/schedule/${wid}`, data)
  }
}
