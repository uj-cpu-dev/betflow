import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { useAuthStore } from '../store/authStore'
import { authApi } from '../api/authApi'
import type { UserResponse } from '../types/api.types'

export default function Home() {
  const { token, logout } = useAuthStore()
  const navigate = useNavigate()
  const [user, setUser] = useState<UserResponse | null>(null)
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    // If no token, send them to register
    if (!token) {
      navigate('/register')
      return
    }

    // Call /me to get fresh user data
    authApi.getMe(token)
      .then(setUser)
      .catch(() => {
        // Token expired or invalid — log out and redirect
        logout()
        navigate('/register')
      })
      .finally(() => setLoading(false))
  }, [token])

  if (loading) return <div>Loading...</div>
    if (!user) return null

    return (
      <div style={{ maxWidth: 600, margin: '60px auto', padding: 24 }}>
        <h2>Welcome, {user.username} 👋</h2>
        <p>Email: {user.email}</p>
        <p>Wallet balance: ${user.walletBalance.toFixed(2)}</p>
        <p>Role: {user.role}</p>
        <button onClick={() => { logout(); navigate('/register') }}>
          Log out
        </button>
      </div>
    )
 }
