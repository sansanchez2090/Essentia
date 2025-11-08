"use client"

import { useState, useMemo } from "react"
import { Header } from "@/components/header"
import { PerfumeCard } from "@/components/perfume-card"
import { PerfumeFilters } from "@/components/perfume-filters"
import { usePerfumes } from "@/hooks/use-perfume-store"

export default function HomePage() {
  const perfumes = usePerfumes()
  const [search, setSearch] = useState("")
  const [category, setCategory] = useState("all")
  const [gender, setGender] = useState("all")

  const filteredPerfumes = useMemo(() => {
    return perfumes.filter((perfume) => {
      const matchesSearch =
        search === "" ||
        perfume.name.toLowerCase().includes(search.toLowerCase()) ||
        perfume.brand.toLowerCase().includes(search.toLowerCase()) ||
        perfume.description.toLowerCase().includes(search.toLowerCase())

      const matchesCategory = category === "all" || perfume.category === category
      const matchesGender = gender === "all" || perfume.gender === gender

      return matchesSearch && matchesCategory && matchesGender
    })
  }, [perfumes, search, category, gender])

  const handleReset = () => {
    setSearch("")
    setCategory("all")
    setGender("all")
  }

  return (
    <div className="min-h-screen flex flex-col">
      <Header />

      <main className="flex-1">
        {/* Hero Section */}
        <section className="border-b bg-gradient-to-b from-secondary/30 to-background">
          <div className="container mx-auto px-4 sm:px-6 lg:px-8 py-16 sm:py-24">
            <div className="max-w-3xl mx-auto text-center space-y-6">
              <h1 className="text-4xl sm:text-5xl lg:text-6xl font-bold tracking-tight text-balance">
                Discover Your Perfect Fragrance
              </h1>
              <p className="text-lg sm:text-xl text-muted-foreground text-pretty">
                Explore our exclusive collection of perfumes from the world's most prestigious houses
              </p>
            </div>
          </div>
        </section>

        {/* Filters & Catalog */}
        <section className="container mx-auto px-4 sm:px-6 lg:px-8 py-12">
          <div className="space-y-8">
            <PerfumeFilters
              search={search}
              category={category}
              gender={gender}
              onSearchChange={setSearch}
              onCategoryChange={setCategory}
              onGenderChange={setGender}
              onReset={handleReset}
            />

            <div className="flex items-center justify-between">
              <p className="text-sm text-muted-foreground">
                {filteredPerfumes.length} {filteredPerfumes.length === 1 ? "perfume found" : "perfumes found"}
              </p>
            </div>

            {filteredPerfumes.length === 0 ? (
              <div className="text-center py-16">
                <p className="text-lg text-muted-foreground">No perfumes found with the selected filters</p>
                <button onClick={handleReset} className="mt-4 text-accent hover:underline">
                  Clear filters
                </button>
              </div>
            ) : (
              <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
                {filteredPerfumes.map((perfume) => (
                  <PerfumeCard key={perfume.id} perfume={perfume} />
                ))}
              </div>
            )}
          </div>
        </section>
      </main>

      <footer className="border-t py-8 mt-16">
        <div className="container mx-auto px-4 sm:px-6 lg:px-8">
          <p className="text-center text-sm text-muted-foreground">© 2025 Essentia. All rights reserved.</p>
        </div>
      </footer>
    </div>
  )
}
