import { useState } from 'react'
import { Link } from 'react-router-dom'
import { useAuth } from '../hooks/useAuth'
import type { LoginRequest } from '../types/api.types'

export default function Login() {
  const { login } = useAuth()
  const [form, setForm] = useState<LoginRequest>({ email: '', password: '' })
  const [error, setError] = useState<string | null>(null)
  const [loading, setLoading] = useState(false)

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    setLoading(true)
    setError(null)
    try {
      await login(form)
      // useAuth.login() handles redirect to /home
    } catch (err: any) {
      setError(err.response?.data?.message || 'Invalid email or password')
    } finally {
      setLoading(false)
    }
  }

  return (
    <div style={{ maxWidth: 400, margin: '100px auto', padding: 24 }}>
      <h2>Sign in</h2>
      <form onSubmit={handleSubmit}>
        <input placeholder="Email" type="email" value={form.email}
          onChange={e => setForm(p => ({ ...p, email: e.target.value }))} />
        <input placeholder="Password" type="password" value={form.password}
          onChange={e => setForm(p => ({ ...p, password: e.target.value }))} />
        <button type="submit" disabled={loading}>
          {loading ? 'Signing in...' : 'Sign in'}
        </button>
        {error && <p style={{ color: 'red' }}>{error}</p>}
      </form>
      <p>No account? <Link to="/register">Register</Link></p>
    </div>
  )
}