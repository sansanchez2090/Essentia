import Link from "next/link"
import Image from "next/image"
import { Star } from "lucide-react"
import { Card, CardContent } from "@/components/ui/card"
import { Badge } from "@/components/ui/badge"
import type { Perfume } from "@/lib/types"

interface PerfumeCardProps {
  perfume: Perfume
}

export function PerfumeCard({ perfume }: PerfumeCardProps) {
  return (
    <Link href={`/perfume/${perfume.id}`}>
      <Card className="group overflow-hidden transition-all hover:shadow-xl hover:-translate-y-1">
        <CardContent className="p-0">
          <div className="relative aspect-[3/4] overflow-hidden bg-secondary">
            <Image
              src={perfume.imageUrl || "/placeholder.svg"}
              alt={perfume.name}
              fill
              className="object-cover transition-transform duration-500 group-hover:scale-110"
            />
            <div className="absolute top-3 right-3">
              <Badge variant="secondary" className="bg-background/90 backdrop-blur-sm">
                {perfume.concentration}
              </Badge>
            </div>
          </div>
          <div className="p-5 space-y-3">
            <div>
              <p className="text-sm text-muted-foreground font-medium">{perfume.brand}</p>
              <h3 className="text-lg font-semibold text-balance leading-tight mt-1">{perfume.name}</h3>
            </div>
            <div className="flex items-center gap-1">
              <Star className="w-4 h-4 fill-accent text-accent" />
              <span className="text-sm font-medium">{perfume.rating}</span>
              <span className="text-sm text-muted-foreground ml-1">({perfume.size}ml)</span>
            </div>
            <div className="flex items-center justify-between pt-2 border-t">
              <span className="text-2xl font-bold text-accent">${perfume.price}</span>
              <Badge variant="outline" className="capitalize">
                {perfume.category}
              </Badge>
            </div>
          </div>
        </CardContent>
      </Card>
    </Link>
  )
}
