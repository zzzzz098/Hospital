import request from '@/utils/request'

export const doctorApi = {
  list(params: { office?: string; name?: string; page?: number; size?: number }) {
    return request.get('/api/doctors', { params })
  },
  detail(did: number) {
    return request.get(`/api/doctors/${did}`)
  },
  schedule(did: number) {
    return request.get(`/api/doctors/${did}/schedule`)
  },
  offices() {
    return request.get('/api/offices')
  }
}
