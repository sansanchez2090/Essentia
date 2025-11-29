"""This module defines the SQLAlchemy models for the Perfume application."""

# pylint: disable=R0903
from sqlalchemy import Column, Integer, String, Text, Float, ForeignKey, Table  # type: ignore
from sqlalchemy.orm import relationship  # type:ignore
from src.db import Base

perfume_olfactive_notes = Table(
    "perfume_olfactive_notes",
    Base.metadata,
    Column("perfume_id", Integer, ForeignKey("perfumes.id")),
    Column("olfactive_note_id", Integer, ForeignKey("olfactive_notes.id")),
)

perfume_perfumers = Table(
    "perfume_perfumers",
    Base.metadata,
    Column("perfume_id", Integer, ForeignKey("perfumes.id")),
    Column("perfumer_id", Integer, ForeignKey("perfumers.id")),
)


class PerfumeHouse(Base):
    """Model to represent Perfume Houses (e.g., Chanel, Dior)."""

    __tablename__ = "perfume_houses"
    id = Column(Integer, primary_key=True, index=True)
    name = Column(String(255), nullable=False)
    country = Column(String(100))
    founding_year = Column(Integer)
    description = Column(Text)
    website = Column(String(255))
    perfumes = relationship("Perfume", back_populates="house")

class PerfumeType(Base):
    """Model to represent Perfume Types (e.g., Eau de Parfum)."""

    __tablename__ = "perfume_types"

    id = Column(Integer, primary_key=True, index=True)
    name = Column(String(100), nullable=False)
    description = Column(Text)


class Concentration(Base):
    """Model to represent Perfume Concentration (e.g. EDP, EDT)."""

    __tablename__ = "concentrations"

    id = Column(Integer, primary_key=True, index=True)
    name = Column(String(50), nullable=False)
    percentage = Column(Float)


class OlfactiveNote(Base):
    """Model to represent Olfactive Notes (e.g. Vainilla, Oud)."""

    __tablename__ = "olfactive_notes"

    id = Column(Integer, primary_key=True, index=True)
    name = Column(String(100))
    category = Column(String(50))


class Perfumer(Base):
    """Model to represent Perfumers"""

    __tablename__ = "perfumers"

    id = Column(Integer, primary_key=True, index=True)
    name = Column(String(100))
    nationality = Column(String(50))
    biography = Column(Text)


class Perfume(Base):
    """Model to represent a Perfume"""

    __tablename__ = "perfumes"

    id = Column(Integer, primary_key=True, index=True)
    name = Column(String(255), nullable=False)
    description = Column(Text)
    release_year = Column(Integer)
    gender = Column(String(20))
    image_url = Column(String(255))
    perfume_type_id = Column(Integer, ForeignKey("perfume_types.id"))
    concentration_id = Column(Integer, ForeignKey("concentrations.id"))
    house_id = Column(Integer, ForeignKey("perfume_houses.id"))

    # Relaciones
    house = relationship("PerfumeHouse", back_populates="perfumes")
    type = relationship("PerfumeType")
    concentration = relationship("Concentration")
    olfactive_notes = relationship("OlfactiveNote", secondary=perfume_olfactive_notes)
    perfumers = relationship("Perfumer", secondary=perfume_perfumers)
    reviews = relationship("Review", back_populates="perfume")


class Review(Base):
    """Model to represent Perfume Reviews"""

    __tablename__ = "reviews"

    id = Column(Integer, primary_key=True, index=True)
    perfume_id = Column(Integer, ForeignKey("perfumes.id"))
    rating = Column(Float)
    comment = Column(Text)

    perfume = relationship("Perfume", back_populates="reviews")
