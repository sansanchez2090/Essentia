"use client"

import Image from "next/image"
import { Pencil, Trash2, Eye } from "lucide-react"
import { Button } from "@/components/ui/button"
import { Badge } from "@/components/ui/badge"
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "@/components/ui/table"
import type { Perfume } from "@/lib/types"
import Link from "next/link"

interface PerfumeTableProps {
  perfumes: Perfume[]
  onEdit: (perfume: Perfume) => void
  onDelete: (id: string) => void
}

export function PerfumeTable({ perfumes, onEdit, onDelete }: PerfumeTableProps) {
  return (
    <div className="border rounded-lg overflow-hidden">
      <Table>
        <TableHeader>
          <TableRow>
            <TableHead className="w-20">Image</TableHead>
            <TableHead>Name</TableHead>
            <TableHead>Brand</TableHead>
            <TableHead>Price</TableHead>
            <TableHead>Category</TableHead>
            <TableHead>Rating</TableHead>
            <TableHead className="text-right">Actions</TableHead>
          </TableRow>
        </TableHeader>
        <TableBody>
          {perfumes.length === 0 ? (
            <TableRow>
              <TableCell colSpan={7} className="text-center py-8 text-muted-foreground">
                No perfumes registered
              </TableCell>
            </TableRow>
          ) : (
            perfumes.map((perfume) => (
              <TableRow key={perfume.id}>
                <TableCell>
                  <div className="relative w-12 h-16 rounded overflow-hidden bg-secondary">
                    <Image
                      src={perfume.imageUrl || "/placeholder.svg"}
                      alt={perfume.name}
                      fill
                      className="object-cover"
                    />
                  </div>
                </TableCell>
                <TableCell className="font-medium">{perfume.name}</TableCell>
                <TableCell>{perfume.brand}</TableCell>
                <TableCell>${perfume.price}</TableCell>
                <TableCell>
                  <Badge variant="outline" className="capitalize">
                    {perfume.category}
                  </Badge>
                </TableCell>
                <TableCell>{perfume.rating}</TableCell>
                <TableCell>
                  <div className="flex items-center justify-end gap-2">
                    <Button variant="ghost" size="icon" asChild>
                      <Link href={`/perfume/${perfume.id}`}>
                        <Eye className="w-4 h-4" />
                      </Link>
                    </Button>
                    <Button variant="ghost" size="icon" onClick={() => onEdit(perfume)}>
                      <Pencil className="w-4 h-4" />
                    </Button>
                    <Button variant="ghost" size="icon" onClick={() => onDelete(perfume.id)}>
                      <Trash2 className="w-4 h-4 text-destructive" />
                    </Button>
                  </div>
                </TableCell>
              </TableRow>
            ))
          )}
        </TableBody>
      </Table>
    </div>
  )
}
