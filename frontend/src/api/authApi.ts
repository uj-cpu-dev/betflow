import axios from 'axios'
import type { UserResponse, RegisterRequest, LoginRequest } from '../types/api.types'

const api = axios.create({
baseURL: '/api',  // proxied to localhost:8081 via vite config
headers: { 'Content-Type': 'application/json' }
})

api.interceptors.request.use((config) => {
  const auth = localStorage.getItem('betflow-auth')
  if (auth) {
    const { token } = JSON.parse(auth)
    if (token) config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

export const authApi = {
register: async (data: RegisterRequest): Promise<UserResponse> => {
    const res = await api.post<UserResponse>('/users/register', data)
    return res.data
  },

  login: async (data: LoginRequest): Promise<UserResponse> => {
    const res = await api.post<UserResponse>('/auth/login', data)
    return res.data
  },

  getMe: async (token: string): Promise<UserResponse> => {
    const res = await api.get<UserResponse>('/users/me', {
      headers: { Authorization: `Bearer ${token}` }
    })
    return res.data
  }
}