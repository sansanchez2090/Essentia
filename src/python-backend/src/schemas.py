"""This module sets up the Pydantic schemas for data validation and serialization."""

# pylint: disable=R0903
from typing import Optional
from pydantic import BaseModel # type: ignore

class PerfumeBase(BaseModel):
    """Base schema for Perfume."""
    name: str
    description: str
    release_year: int
    gender: str
    image_url: str

class PerfumeUpdate(PerfumeBase):
    """Schema for updating Perfume; all fields are optional."""
    name: Optional[str] = None
    description: Optional[str] = None
    release_year: Optional[int] = None
    gender: Optional[str] = None
    image_url: Optional[str] = None

class PerfumeCreate(PerfumeBase):
    """Schema for creating a new Perfume."""

class PerfumeResponse(PerfumeBase):
    """Schema for Perfume response, includes ID."""
    id: int

    class Config:
        """Pydantic configuration to enable ORM mode."""
        orm_mode = True
