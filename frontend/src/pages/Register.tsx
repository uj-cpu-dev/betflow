import { useState } from 'react'
import { authApi } from '../api/authApi'
import type { RegisterRequest } from '../types/api.types'

export default function Register() {
  const [form, setForm] = useState<RegisterRequest>({
    email: '', username: '', password: ''
  })
  const [error, setError] = useState<string | null>(null)
  const [loading, setLoading] = useState(false)

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    setLoading(true)
    setError(null)
    try {
      const user = await authApi.register(form)
      console.log('Registered:', user)
      console.log('Token:', user.accessToken)
    } catch (err: any) {
      setError(err.response?.data?.message || 'Registration failed')
    } finally {
      setLoading(false)
    }
  }

  return (
    <div style={{ maxWidth: 400, margin: '100px auto', padding: 24 }}>
      <h2>Create account</h2>
      <form onSubmit={handleSubmit}>
        <input placeholder="Email" value={form.email}
          onChange={e => setForm(p => ({ ...p, email: e.target.value }))} />
        <input placeholder="Username" value={form.username}
          onChange={e => setForm(p => ({ ...p, username: e.target.value }))} />
        <input type="password" placeholder="Password" value={form.password}
          onChange={e => setForm(p => ({ ...p, password: e.target.value }))} />
        <button type="submit" disabled={loading}>
          {loading ? 'Creating...' : 'Register'}
        </button>
        {error && <p style={{ color: 'red' }}>{error}</p>}
      </form>
    </div>
  )
}
