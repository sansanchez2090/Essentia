"use client"

import { Search, SlidersHorizontal } from "lucide-react"
import { Input } from "@/components/ui/input"
import { Button } from "@/components/ui/button"
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select"
import { Sheet, SheetContent, SheetHeader, SheetTitle, SheetTrigger } from "@/components/ui/sheet"
import { Label } from "@/components/ui/label"

interface PerfumeFiltersProps {
  search: string
  category: string
  gender: string
  onSearchChange: (value: string) => void
  onCategoryChange: (value: string) => void
  onGenderChange: (value: string) => void
  onReset: () => void
}

export function PerfumeFilters({
  search,
  category,
  gender,
  onSearchChange,
  onCategoryChange,
  onGenderChange,
  onReset,
}: PerfumeFiltersProps) {
  return (
    <div className="space-y-4">
      <div className="relative">
        <Search className="absolute left-3 top-1/2 -translate-y-1/2 w-5 h-5 text-muted-foreground" />
        <Input
          placeholder="Search perfumes, brands..."
          value={search}
          onChange={(e) => onSearchChange(e.target.value)}
          className="pl-10 h-12"
        />
      </div>

      <div className="flex gap-3">
        <Select value={category} onValueChange={onCategoryChange}>
          <SelectTrigger className="flex-1">
            <SelectValue placeholder="Category" />
          </SelectTrigger>
          <SelectContent>
            <SelectItem value="all">All categories</SelectItem>
            <SelectItem value="floral">Floral</SelectItem>
            <SelectItem value="woody">Woody</SelectItem>
            <SelectItem value="fresh">Fresh</SelectItem>
            <SelectItem value="oriental">Oriental</SelectItem>
            <SelectItem value="citrus">Citrus</SelectItem>
          </SelectContent>
        </Select>

        <Select value={gender} onValueChange={onGenderChange}>
          <SelectTrigger className="flex-1">
            <SelectValue placeholder="Gender" />
          </SelectTrigger>
          <SelectContent>
            <SelectItem value="all">All</SelectItem>
            <SelectItem value="masculine">Masculine</SelectItem>
            <SelectItem value="feminine">Feminine</SelectItem>
            <SelectItem value="unisex">Unisex</SelectItem>
          </SelectContent>
        </Select>

        <Sheet>
          <SheetTrigger asChild>
            <Button variant="outline" size="icon" className="shrink-0 bg-transparent">
              <SlidersHorizontal className="w-5 h-5" />
            </Button>
          </SheetTrigger>
          <SheetContent>
            <SheetHeader>
              <SheetTitle>Advanced Filters</SheetTitle>
            </SheetHeader>
            <div className="space-y-6 mt-6">
              <div className="space-y-2">
                <Label>Category</Label>
                <Select value={category} onValueChange={onCategoryChange}>
                  <SelectTrigger>
                    <SelectValue placeholder="Select category" />
                  </SelectTrigger>
                  <SelectContent>
                    <SelectItem value="all">All categories</SelectItem>
                    <SelectItem value="floral">Floral</SelectItem>
                    <SelectItem value="woody">Woody</SelectItem>
                    <SelectItem value="fresh">Fresh</SelectItem>
                    <SelectItem value="oriental">Oriental</SelectItem>
                    <SelectItem value="citrus">Citrus</SelectItem>
                  </SelectContent>
                </Select>
              </div>

              <div className="space-y-2">
                <Label>Gender</Label>
                <Select value={gender} onValueChange={onGenderChange}>
                  <SelectTrigger>
                    <SelectValue placeholder="Select gender" />
                  </SelectTrigger>
                  <SelectContent>
                    <SelectItem value="all">All</SelectItem>
                    <SelectItem value="masculine">Masculine</SelectItem>
                    <SelectItem value="feminine">Feminine</SelectItem>
                    <SelectItem value="unisex">Unisex</SelectItem>
                  </SelectContent>
                </Select>
              </div>

              <Button onClick={onReset} variant="outline" className="w-full bg-transparent">
                Clear Filters
              </Button>
            </div>
          </SheetContent>
        </Sheet>
      </div>
    </div>
  )
}
