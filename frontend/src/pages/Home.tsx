import { useQuery } from '@tanstack/react-query'
import { useNavigate } from 'react-router-dom'
import { useAuth } from '../hooks/useAuth'
import { authApi } from '../api/authApi'

export default function Home() {
  const { token, logoutUser, isAuthenticated } = useAuth()
  const navigate = useNavigate()

  const { data: user, isLoading } = useQuery({
    queryKey: ['currentUser'],
    queryFn: () => authApi.getMe(token!),
    enabled: isAuthenticated,
    // If token is invalid, redirect to login
    onError: () => {
      logoutUser()
    }
  })

  if (!isAuthenticated) {
    navigate('/login')
    return null
  }

  if (isLoading) return <div>Loading...</div>
  if (!user) return null

  return (
    <div style={{ maxWidth: 600, margin: '60px auto', padding: 24 }}>
      <h2>Welcome, {user.username} 👋</h2>
      <p>Email: {user.email}</p>
      <p>Wallet balance: ${user.walletBalance.toFixed(2)}</p>
      <p>Role: {user.role}</p>
      <button onClick={logoutUser}>Log out</button>
    </div>
  )
}