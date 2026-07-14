import request from '@/utils/request'

export const authApi = {
  login(account: string, password: string, role: string) {
    return request.post('/auth/login', { account, password, role })
  },
  register(form: { account: string; password: string; name: string; email: string }) {
    return request.post('/auth/register', form)
  }
}
