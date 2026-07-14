import axios from 'axios'
import router from '@/router'

const request = axios.create({
  baseURL: import.meta.env.VITE_API_URL || '',
  timeout: 10000
})

request.interceptors.request.use(config => {
  const token = localStorage.getItem('token')
  if (token) config.headers['satoken'] = token
  return config
})

request.interceptors.response.use(
  response => {
    const body = response.data
    if (body.code === 200) {
      return body.data  // 解包 ResponseResult，只返回 data 部分
    }
    // 业务错误：抛出异常让调用方 catch
    return Promise.reject(new Error(body.message || '请求失败'))
  },
  error => {
    if (error.response?.status === 401) {
      localStorage.removeItem('token')
      localStorage.removeItem('userInfo')
      router.push('/login')
    }
    return Promise.reject(error)
  }
)

export default request
