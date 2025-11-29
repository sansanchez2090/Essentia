"use client"

import { useRouter } from "next/navigation"
import { ArrowLeft } from "lucide-react"
import { Button } from "@/components/ui/button"

export function BackButton() {
  const router = useRouter()

  return (
    <Button variant="ghost" onClick={() => router.back()} className="mb-6 -ml-4">
      <ArrowLeft className="w-4 h-4 mr-2" />
      Volver al catálogo
    </Button>
  )
}
