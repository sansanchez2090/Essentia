"use client"

import { useState, useEffect } from "react"
import type { User } from "@/lib/auth-types"
import { authStore } from "@/lib/auth-store"

export function useAuth() {
  const [user, setUser] = useState<User | null>(null)
  const [isLoading, setIsLoading] = useState(true)

  useEffect(() => {
    setUser(authStore.getCurrentUser())
    setIsLoading(false)
  }, [])

  const login = (email: string, password: string) => {
    const result = authStore.login(email, password)
    if (result.success && result.user) {
      setUser(result.user)
    }
    return result
  }

  const register = (email: string, password: string, name: string) => {
    const result = authStore.register(email, password, name)
    if (result.success && result.user) {
      setUser(result.user)
    }
    return result
  }

  const logout = () => {
    authStore.logout()
    setUser(null)
  }

  const updateProfile = (updates: Partial<User>) => {
    const result = authStore.updateProfile(updates)
    if (result.success && result.user) {
      setUser(result.user)
    }
    return result
  }

  return {
    user,
    isLoading,
    isAuthenticated: !!user,
    login,
    register,
    logout,
    updateProfile,
  }
}
