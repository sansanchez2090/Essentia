"use client"

import type React from "react"

import { useState } from "react"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Textarea } from "@/components/ui/textarea"
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import type { Perfume } from "@/lib/types"

interface PerfumeFormProps {
  perfume?: Perfume
  onSubmit: (data: Omit<Perfume, "id" | "createdAt" | "rating">) => void
  onCancel: () => void
}

export function PerfumeForm({ perfume, onSubmit, onCancel }: PerfumeFormProps) {
  const [formData, setFormData] = useState({
    name: perfume?.name || "",
    brand: perfume?.brand || "",
    price: perfume?.price || 0,
    description: perfume?.description || "",
    imageUrl: perfume?.imageUrl || "",
    boxImageUrl: perfume?.boxImageUrl || "",
    topNotes: perfume?.notes.top.join(", ") || "",
    heartNotes: perfume?.notes.heart.join(", ") || "",
    baseNotes: perfume?.notes.base.join(", ") || "",
    category: perfume?.category || "floral",
    gender: perfume?.gender || "unisex",
    concentration: perfume?.concentration || "eau de parfum",
    size: perfume?.size || 100,
  })

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault()

    const perfumeData = {
      name: formData.name,
      brand: formData.brand,
      price: Number(formData.price),
      description: formData.description,
      imageUrl: formData.imageUrl,
      boxImageUrl: formData.boxImageUrl,
      notes: {
        top: formData.topNotes.split(",").map((n) => n.trim()),
        heart: formData.heartNotes.split(",").map((n) => n.trim()),
        base: formData.baseNotes.split(",").map((n) => n.trim()),
      },
      category: formData.category as Perfume["category"],
      gender: formData.gender as Perfume["gender"],
      concentration: formData.concentration as Perfume["concentration"],
      size: Number(formData.size),
      rating: perfume?.rating || 4.5,
    }

    onSubmit(perfumeData)
  }

  return (
    <form onSubmit={handleSubmit} className="space-y-6">
      <Card>
        <CardHeader>
          <CardTitle>Basic Information</CardTitle>
        </CardHeader>
        <CardContent className="space-y-4">
          <div className="grid sm:grid-cols-2 gap-4">
            <div className="space-y-2">
              <Label htmlFor="name">Perfume Name *</Label>
              <Input
                id="name"
                value={formData.name}
                onChange={(e) => setFormData({ ...formData, name: e.target.value })}
                required
              />
            </div>

            <div className="space-y-2">
              <Label htmlFor="brand">Perfume House *</Label>
              <Input
                id="brand"
                value={formData.brand}
                onChange={(e) => setFormData({ ...formData, brand: e.target.value })}
                required
              />
            </div>
          </div>

          <div className="space-y-2">
            <Label htmlFor="description">Description *</Label>
            <Textarea
              id="description"
              value={formData.description}
              onChange={(e) => setFormData({ ...formData, description: e.target.value })}
              rows={4}
              required
            />
          </div>

          <div className="grid sm:grid-cols-2 gap-4">
            <div className="space-y-2">
              <Label htmlFor="price">Price (USD) *</Label>
              <Input
                id="price"
                type="number"
                min="0"
                step="0.01"
                value={formData.price}
                onChange={(e) => setFormData({ ...formData, price: Number(e.target.value) })}
                required
              />
            </div>

            <div className="space-y-2">
              <Label htmlFor="size">Size (ml) *</Label>
              <Input
                id="size"
                type="number"
                min="1"
                value={formData.size}
                onChange={(e) => setFormData({ ...formData, size: Number(e.target.value) })}
                required
              />
            </div>
          </div>
        </CardContent>
      </Card>

      <Card>
        <CardHeader>
          <CardTitle>Images</CardTitle>
        </CardHeader>
        <CardContent className="space-y-4">
          <div className="space-y-2">
            <Label htmlFor="imageUrl">Perfume Image URL *</Label>
            <Input
              id="imageUrl"
              type="url"
              value={formData.imageUrl}
              onChange={(e) => setFormData({ ...formData, imageUrl: e.target.value })}
              placeholder="https://example.com/perfume.jpg"
              required
            />
          </div>

          <div className="space-y-2">
            <Label htmlFor="boxImageUrl">Box Image URL *</Label>
            <Input
              id="boxImageUrl"
              type="url"
              value={formData.boxImageUrl}
              onChange={(e) => setFormData({ ...formData, boxImageUrl: e.target.value })}
              placeholder="https://example.com/box.jpg"
              required
            />
          </div>
        </CardContent>
      </Card>

      <Card>
        <CardHeader>
          <CardTitle>Olfactory Notes</CardTitle>
        </CardHeader>
        <CardContent className="space-y-4">
          <div className="space-y-2">
            <Label htmlFor="topNotes">Top Notes *</Label>
            <Input
              id="topNotes"
              value={formData.topNotes}
              onChange={(e) => setFormData({ ...formData, topNotes: e.target.value })}
              placeholder="Bergamot, Lemon, Orange (comma separated)"
              required
            />
          </div>

          <div className="space-y-2">
            <Label htmlFor="heartNotes">Heart Notes *</Label>
            <Input
              id="heartNotes"
              value={formData.heartNotes}
              onChange={(e) => setFormData({ ...formData, heartNotes: e.target.value })}
              placeholder="Rose, Jasmine, Lavender (comma separated)"
              required
            />
          </div>

          <div className="space-y-2">
            <Label htmlFor="baseNotes">Base Notes *</Label>
            <Input
              id="baseNotes"
              value={formData.baseNotes}
              onChange={(e) => setFormData({ ...formData, baseNotes: e.target.value })}
              placeholder="Vanilla, Musk, Amber (comma separated)"
              required
            />
          </div>
        </CardContent>
      </Card>

      <Card>
        <CardHeader>
          <CardTitle>Classification</CardTitle>
        </CardHeader>
        <CardContent className="space-y-4">
          <div className="grid sm:grid-cols-3 gap-4">
            <div className="space-y-2">
              <Label htmlFor="category">Category *</Label>
              <Select
                value={formData.category}
                onValueChange={(value) => setFormData({ ...formData, category: value })}
              >
                <SelectTrigger id="category">
                  <SelectValue />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="floral">Floral</SelectItem>
                  <SelectItem value="woody">Woody</SelectItem>
                  <SelectItem value="fresh">Fresh</SelectItem>
                  <SelectItem value="oriental">Oriental</SelectItem>
                  <SelectItem value="citrus">Citrus</SelectItem>
                </SelectContent>
              </Select>
            </div>

            <div className="space-y-2">
              <Label htmlFor="gender">Gender *</Label>
              <Select value={formData.gender} onValueChange={(value) => setFormData({ ...formData, gender: value })}>
                <SelectTrigger id="gender">
                  <SelectValue />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="masculine">Masculine</SelectItem>
                  <SelectItem value="feminine">Feminine</SelectItem>
                  <SelectItem value="unisex">Unisex</SelectItem>
                </SelectContent>
              </Select>
            </div>

            <div className="space-y-2">
              <Label htmlFor="concentration">Concentration *</Label>
              <Select
                value={formData.concentration}
                onValueChange={(value) => setFormData({ ...formData, concentration: value })}
              >
                <SelectTrigger id="concentration">
                  <SelectValue />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="parfum">Parfum</SelectItem>
                  <SelectItem value="eau de parfum">Eau de Parfum</SelectItem>
                  <SelectItem value="eau de toilette">Eau de Toilette</SelectItem>
                  <SelectItem value="eau de cologne">Eau de Cologne</SelectItem>
                </SelectContent>
              </Select>
            </div>
          </div>
        </CardContent>
      </Card>

      <div className="flex gap-3 justify-end">
        <Button type="button" variant="outline" onClick={onCancel}>
          Cancel
        </Button>
        <Button type="submit">{perfume ? "Update Perfume" : "Create Perfume"}</Button>
      </div>
    </form>
  )
}
