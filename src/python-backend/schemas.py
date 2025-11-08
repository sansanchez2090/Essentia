from pydantic import BaseModel # type: ignore
from typing import List

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

