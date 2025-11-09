from pydantic import BaseModel # type: ignore
from typing import List
from datetime import date

"""This module sets up the Pydantic schemas for data validation and serialization."""

class PerfumeBase(BaseModel):
    name: str
    description: str
    release_year: int
    gender: str
    image_url: str

class ConcentrationBase(BaseModel):
    name: str
    percentage: float

class PerfumeTypeBase(BaseModel):
    name: str
    description: str  

class PerfumeHouseBase(BaseModel):
    name: str
    country: str
    founding_year:int
    description: str
    website: str
    Perfumes: List[PerfumeBase] = []

class OlfactiveNoteBase(BaseModel):
    name: str
    category: str

class PerfumerBase(BaseModel):
    name: str
    nationality: str
    biography: str

class ReviewBase(BaseModel):
    rating: int
    comment: str
    date: date # AAAA-MM-DD