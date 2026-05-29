import { useCallback } from 'react'
import { useNavigate } from 'react-router-dom'
import { useAuthStore } from '../store/authStore'
import { authApi } from '../api/authApi'
import type { LoginRequest, RegisterRequest } from '../types/api.types'

export function useAuth() {
  const { setAuth, logout, token, user, isAuthenticated } = useAuthStore()
  const navigate = useNavigate()

  const login = useCallback(async (data: LoginRequest) => {
    const response = await authApi.login(data)
    setAuth(response, response.accessToken)
    navigate('/home')
  }, [setAuth, navigate])

  const register = useCallback(async (data: RegisterRequest) => {
    const response = await authApi.register(data)
    setAuth(response, response.accessToken)
    navigate('/home')
  }, [setAuth, navigate])

  const logoutUser = useCallback(() => {
    logout()
    navigate('/login')
  }, [logout, navigate])

  return {
    login,
    register,
    logoutUser,
    token,
    user,
    isAuthenticated: isAuthenticated()
  }
}
