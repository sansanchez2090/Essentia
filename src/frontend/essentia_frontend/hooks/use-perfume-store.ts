"use client"

import { useEffect, useState } from "react"
import { perfumeStore } from "@/lib/perfume-store"
import type { Perfume } from "@/lib/types"

export function usePerfumes() {
  const [perfumes, setPerfumes] = useState<Perfume[]>(perfumeStore.getAll())

  useEffect(() => {
    const unsubscribe = perfumeStore.subscribe(() => {
      setPerfumes(perfumeStore.getAll())
    })
    return unsubscribe
  }, [])

  return perfumes
}
