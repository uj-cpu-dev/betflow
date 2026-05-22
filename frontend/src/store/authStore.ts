import { create } from 'zustand'
import { persist } from 'zustand/middleware'
import type { UserResponse } from '../types/api.types'

interface AuthState {
user: UserResponse | null
token: string | null
setAuth: (user: UserResponse, token: string) => void
  logout: () => void
  isAuthenticated: () => boolean
}

export const useAuthStore = create<AuthState>()(
  persist(
    (set, get) => ({
      user: null,
      token: null,

      setAuth: (user, token) => set({ user, token }),

      logout: () => set({ user: null, token: null }),

      // Derived state — is the user logged in?
      isAuthenticated: () => get().token !== null,
    }),
    {
      name: 'betflow-auth',  // key in localStorage
      // Only persist token and user — not the functions
      partialize: (state) => ({
        user: state.user,
        token: state.token
      })
    }
)
)