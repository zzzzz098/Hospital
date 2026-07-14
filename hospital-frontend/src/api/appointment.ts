import request from '@/utils/request'

export const appointmentApi = {
  create(data: any) {
    return request.post('/api/appointments', data)
  },
  myList() {
    return request.get('/api/appointments')
  },
  cancel(rid: number) {
    return request.put(`/api/appointments/${rid}/cancel`)
  },
  allList() {
    return request.get('/api/appointments/all')
  }
}
