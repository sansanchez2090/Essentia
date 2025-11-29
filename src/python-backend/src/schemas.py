"""Defines the Pydantic schemas for data validation and serialization."""

import datetime
from typing import List, Optional
from pydantic import BaseModel # type: ignore


class PerfumeBase(BaseModel):
    """Schema base for Perfume data."""
    name: str
    description: str
    release_year: int
    gender: str
    image_url: str

class PerfumeUpdate(PerfumeBase):
    """Schema for updating Perfume data. All fields are optional."""
    name: Optional[str] = None
    description: Optional[str] = None
    release_year: Optional[int] = None
    gender: Optional[str] = None
    image_url: Optional[str] = None

class PerfumeCreate(PerfumeBase):
    """Schema for creating a new Perfume."""

class ConcentrationBase(BaseModel):
    """Schema base for Perfume Concentration data."""
    name: str
    percentage: float

class PerfumeTypeBase(BaseModel):
    """Schema base for Perfume Type data."""
    name: str
    description: str

class PerfumeHouseBase(BaseModel):
    """Schema base for Perfume House data."""
    name: str
    country: str
    founding_year:int
    description: str
    website: str
    perfumes: List[PerfumeBase] = []

class OlfactiveNoteBase(BaseModel):
    """Schema base for Olfactive Note data."""
    name: str
    category: str

class PerfumerBase(BaseModel):
    """Schema base for Perfumer data."""
    name: str
    nationality: str
    biography: str

class ReviewBase(BaseModel):
    """Schema base for Perfume Review data."""
    rating: int
    comment: str
    date: datetime.date
