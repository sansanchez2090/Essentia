import Link from "next/link"
import { Header } from "@/components/header"
import { Button } from "@/components/ui/button"

export default function NotFound() {
  return (
    <div className="min-h-screen flex flex-col">
      <Header />
      <main className="flex-1 flex items-center justify-center">
        <div className="text-center space-y-6 px-4">
          <h1 className="text-6xl font-bold text-muted-foreground">404</h1>
          <div className="space-y-2">
            <h2 className="text-2xl font-semibold">Perfume not found</h2>
            <p className="text-muted-foreground">The perfume you're looking for doesn't exist or has been removed.</p>
          </div>
          <Button asChild>
            <Link href="/">Back to catalog</Link>
          </Button>
        </div>
      </main>
    </div>
  )
}
