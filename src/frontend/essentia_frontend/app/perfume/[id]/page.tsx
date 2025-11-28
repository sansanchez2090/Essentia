import { notFound } from "next/navigation"
import { Star, Sparkles } from "lucide-react"
import { Header } from "@/components/header"
import { BackButton } from "@/components/back-button"
import { PerfumeImageSlider } from "@/components/perfume-image-slider"
import { Badge } from "@/components/ui/badge"
import { Card, CardContent } from "@/components/ui/card"
import { Separator } from "@/components/ui/separator"
import { perfumeStore } from "@/lib/perfume-store"

interface PageProps {
  params: Promise<{ id: string }>
}

export default async function PerfumeDetailPage({ params }: PageProps) {
  const { id } = await params
  const perfume = perfumeStore.getById(id)

  if (!perfume) {
    notFound()
  }

  const images = [
    {
      url: perfume.imageUrl || "/placeholder.svg",
      alt: perfume.name,
    },
    {
      url: perfume.boxImageUrl || "/placeholder.svg",
      alt: `${perfume.name} - Box`,
    },
  ]

  return (
    <div className="min-h-screen flex flex-col">
      <Header />

      <main className="flex-1">
        <div className="container mx-auto px-4 sm:px-6 lg:px-8 py-8">
          <BackButton />

          <div className="grid lg:grid-cols-2 gap-12">
            <PerfumeImageSlider images={images} />

            {/* Details Section */}
            <div className="space-y-8">
              <div className="space-y-4">
                <div className="flex items-start justify-between gap-4">
                  <div className="space-y-2">
                    <p className="text-sm text-muted-foreground font-medium uppercase tracking-wide">{perfume.brand}</p>
                    <h1 className="text-4xl font-bold text-balance">{perfume.name}</h1>
                  </div>
                  <div className="text-right">
                    <p className="text-4xl font-bold text-accent">${perfume.price}</p>
                    <p className="text-sm text-muted-foreground mt-1">{perfume.size}ml</p>
                  </div>
                </div>

                <div className="flex items-center gap-4 flex-wrap">
                  <div className="flex items-center gap-1">
                    <Star className="w-5 h-5 fill-accent text-accent" />
                    <span className="font-semibold">{perfume.rating}</span>
                    <span className="text-sm text-muted-foreground">/5.0</span>
                  </div>
                  <Badge variant="secondary" className="capitalize">
                    {perfume.category}
                  </Badge>
                  <Badge variant="outline" className="capitalize">
                    {perfume.gender}
                  </Badge>
                  <Badge variant="outline">{perfume.concentration}</Badge>
                </div>
              </div>

              <Separator />

              <div className="space-y-3">
                <h2 className="text-lg font-semibold">Description</h2>
                <p className="text-muted-foreground leading-relaxed text-pretty">{perfume.description}</p>
              </div>

              <Separator />

              {/* Olfactory Notes */}
              <div className="space-y-6">
                <h2 className="text-lg font-semibold flex items-center gap-2">
                  <Sparkles className="w-5 h-5 text-accent" />
                  Olfactory Notes
                </h2>

                <div className="space-y-4">
                  <Card className="bg-secondary/50">
                    <CardContent className="p-5">
                      <h3 className="font-semibold mb-3 text-sm uppercase tracking-wide">Top Notes</h3>
                      <div className="flex flex-wrap gap-2">
                        {perfume.notes.top.map((note) => (
                          <Badge key={note} variant="secondary" className="bg-background">
                            {note}
                          </Badge>
                        ))}
                      </div>
                    </CardContent>
                  </Card>

                  <Card className="bg-secondary/50">
                    <CardContent className="p-5">
                      <h3 className="font-semibold mb-3 text-sm uppercase tracking-wide">Heart Notes</h3>
                      <div className="flex flex-wrap gap-2">
                        {perfume.notes.heart.map((note) => (
                          <Badge key={note} variant="secondary" className="bg-background">
                            {note}
                          </Badge>
                        ))}
                      </div>
                    </CardContent>
                  </Card>

                  <Card className="bg-secondary/50">
                    <CardContent className="p-5">
                      <h3 className="font-semibold mb-3 text-sm uppercase tracking-wide">Base Notes</h3>
                      <div className="flex flex-wrap gap-2">
                        {perfume.notes.base.map((note) => (
                          <Badge key={note} variant="secondary" className="bg-background">
                            {note}
                          </Badge>
                        ))}
                      </div>
                    </CardContent>
                  </Card>
                </div>
              </div>

              <Separator />

              {/* Additional Info */}
              <div className="grid grid-cols-2 gap-4 text-sm">
                <div>
                  <p className="text-muted-foreground">Perfume House</p>
                  <p className="font-semibold mt-1">{perfume.brand}</p>
                </div>
                <div>
                  <p className="text-muted-foreground">Concentration</p>
                  <p className="font-semibold mt-1 capitalize">{perfume.concentration}</p>
                </div>
                <div>
                  <p className="text-muted-foreground">Category</p>
                  <p className="font-semibold mt-1 capitalize">{perfume.category}</p>
                </div>
                <div>
                  <p className="text-muted-foreground">Gender</p>
                  <p className="font-semibold mt-1 capitalize">{perfume.gender}</p>
                </div>
              </div>
            </div>
          </div>
        </div>
      </main>
    </div>
  )
}
