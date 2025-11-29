"""This module defines the SQLAlchemy models for the database schema."""
# pylint: disable=R0903
from sqlalchemy import Column, Integer, String, Text #type: ignore
from src.db import Base


class Perfume(Base):
    """SQLAlchemy model for the Perfume table."""
    __tablename__ = "perfumes"

    id = Column(Integer, primary_key=True, index=True)
    name = Column(String(255), nullable=False)
    description = Column(Text)
    release_year = Column(Integer)
    gender = Column(String(20))
    image_url = Column(String(255))
