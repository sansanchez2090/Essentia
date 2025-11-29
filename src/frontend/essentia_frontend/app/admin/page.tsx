"use client"

import { useState, useEffect } from "react"
import { useRouter } from "next/navigation"
import { Plus } from "lucide-react"
import { Header } from "@/components/header"
import { Button } from "@/components/ui/button"
import { PerfumeForm } from "@/components/perfume-form"
import { PerfumeTable } from "@/components/perfume-table"
import { usePerfumes } from "@/hooks/use-perfume-store"
import { perfumeStore } from "@/lib/perfume-store"
import type { Perfume } from "@/lib/types"
import { Dialog, DialogContent, DialogDescription, DialogHeader, DialogTitle } from "@/components/ui/dialog"
import {
  AlertDialog,
  AlertDialogAction,
  AlertDialogCancel,
  AlertDialogContent,
  AlertDialogDescription,
  AlertDialogFooter,
  AlertDialogHeader,
  AlertDialogTitle,
} from "@/components/ui/alert-dialog"
import { useToast } from "@/hooks/use-toast"
import { Toaster } from "@/components/ui/toaster"
import { useAuth } from "@/hooks/use-auth" // Import useAuth hook

export default function AdminPage() {
  const perfumes = usePerfumes()
  const [isFormOpen, setIsFormOpen] = useState(false)
  const [editingPerfume, setEditingPerfume] = useState<Perfume | null>(null)
  const [deletingId, setDeletingId] = useState<string | null>(null)
  const { toast } = useToast()
  const { user, isAuthenticated } = useAuth() // Use useAuth hook
  const router = useRouter()

  useEffect(() => {
    if (!isAuthenticated) {
      router.push("/login")
    }
  }, [isAuthenticated, router])

  const handleCreate = () => {
    setEditingPerfume(null)
    setIsFormOpen(true)
  }

  const handleEdit = (perfume: Perfume) => {
    setEditingPerfume(perfume)
    setIsFormOpen(true)
  }

  const handleSubmit = (data: Omit<Perfume, "id" | "createdAt" | "rating">) => {
    if (editingPerfume) {
      perfumeStore.update(editingPerfume.id, data)
      toast({
        title: "Perfume updated",
        description: `${data.name} has been updated successfully.`,
      })
    } else {
      perfumeStore.create(data)
      toast({
        title: "Perfume created",
        description: `${data.name} has been added to the catalog.`,
      })
    }
    setIsFormOpen(false)
    setEditingPerfume(null)
  }

  const handleDelete = (id: string) => {
    setDeletingId(id)
  }

  const confirmDelete = () => {
    if (deletingId) {
      const perfume = perfumeStore.getById(deletingId)
      perfumeStore.delete(deletingId)
      toast({
        title: "Perfume deleted",
        description: `${perfume?.name} has been removed from the catalog.`,
        variant: "destructive",
      })
      setDeletingId(null)
    }
  }

  if (!isAuthenticated || !user) {
    return null
  }

  return (
    <div className="min-h-screen flex flex-col">
      <Header />

      <main className="flex-1">
        <div className="container mx-auto px-4 sm:px-6 lg:px-8 py-8">
          <div className="space-y-6">
            <div className="flex items-center justify-between">
              <div>
                <h1 className="text-3xl font-bold">Admin Dashboard</h1>
                <p className="text-muted-foreground mt-1">Manage the perfume catalog</p>
              </div>
              <Button onClick={handleCreate}>
                <Plus className="w-4 h-4 mr-2" />
                New Perfume
              </Button>
            </div>

            <PerfumeTable perfumes={perfumes} onEdit={handleEdit} onDelete={handleDelete} />
          </div>
        </div>
      </main>

      <Dialog open={isFormOpen} onOpenChange={setIsFormOpen}>
        <DialogContent className="max-w-3xl max-h-[90vh] overflow-y-auto">
          <DialogHeader>
            <DialogTitle>{editingPerfume ? "Edit Perfume" : "Create New Perfume"}</DialogTitle>
            <DialogDescription>
              {editingPerfume
                ? "Update the perfume information in the form."
                : "Complete the form to add a new perfume to the catalog."}
            </DialogDescription>
          </DialogHeader>
          <PerfumeForm
            perfume={editingPerfume || undefined}
            onSubmit={handleSubmit}
            onCancel={() => {
              setIsFormOpen(false)
              setEditingPerfume(null)
            }}
          />
        </DialogContent>
      </Dialog>

      <AlertDialog open={!!deletingId} onOpenChange={() => setDeletingId(null)}>
        <AlertDialogContent>
          <AlertDialogHeader>
            <AlertDialogTitle>Are you sure?</AlertDialogTitle>
            <AlertDialogDescription>
              This action cannot be undone. The perfume will be permanently removed from the catalog.
            </AlertDialogDescription>
          </AlertDialogHeader>
          <AlertDialogFooter>
            <AlertDialogCancel>Cancel</AlertDialogCancel>
            <AlertDialogAction onClick={confirmDelete} className="bg-destructive text-destructive-foreground">
              Delete
            </AlertDialogAction>
          </AlertDialogFooter>
        </AlertDialogContent>
      </AlertDialog>

      <Toaster />
    </div>
  )
}
