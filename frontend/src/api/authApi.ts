import axios from 'axios'
import type { UserResponse, RegisterRequest, LoginRequest } from '../types/api.types'

const api = axios.create({
baseURL: '/api',  // proxied to localhost:8081 via vite config
headers: { 'Content-Type': 'application/json' }
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