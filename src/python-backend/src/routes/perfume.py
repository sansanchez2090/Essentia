"""This module defines the API routes for managing perfumes."""

from typing import Annotated
from fastapi import Depends, HTTPException, APIRouter # type: ignore
from sqlalchemy.orm import Session # type: ignore
from src import models, schemas
from src.db import get_db


router = APIRouter()

@router.get("/perfumes")
async def get_perfumes(db: Annotated[Session, Depends(get_db)]):
    """
    Retrieve all perfumes
    """
    perfumes = db.query(models.Perfume).all()
    return perfumes

@router.get("/perfumes/{perfume_id}")
async def get_perfumes_by_id(perfume_id: int, db: Annotated[Session, Depends(get_db)]):
    """
    Retrieve a perfume by it's id 
    """
    perfume = db.query(models.Perfume).filter(models.Perfume.id == perfume_id).first()
    if perfume is None:
        raise HTTPException(status_code=404, detail=f"Perfume with ID: {perfume_id} not found")
    return perfume

@router.post("/perfumes", response_model=schemas.PerfumeResponse)
def create_perfume(perfume: schemas.PerfumeCreate, db: Annotated[Session, Depends(get_db)]):
    """create a perfume
"""
    db_perfume = models.Perfume(name=perfume.name,
                                description=perfume.description,
                                release_year=perfume.release_year,
                                gender=perfume.gender,
                                image_url=perfume.image_url)
    db.add(db_perfume)
    db.commit()
    db.refresh(db_perfume)

    return db_perfume

@router.delete("/perfumes/{perfume_id}")
async def delete_perfume(perfume_id: int, db: Annotated[Session, Depends(get_db)]):
    """delete a perfume"""
    perfume = db.query(models.Perfume).filter(models.Perfume.id == perfume_id).first()
    if perfume is None:
        raise HTTPException(status_code=404, detail=f"Perfume with ID: {perfume_id} not found")
    db.delete(perfume)
    db.commit()
    return {"detail": "Perfume deleted successfully"}

@router.put("/perfumes/{perfume_id}", response_model=schemas.PerfumeResponse)
async def update_perfume(perfume_id: int,
                         updated_perfume: schemas.PerfumeUpdate,
                         db: Annotated[Session, Depends(get_db)]):
    """update a perfume"""

    perfume_query = db.query(models.Perfume).filter(models.Perfume.id == perfume_id).first()

    if perfume_query is None:
        raise HTTPException(status_code=404, detail=f"Perfume with ID: {perfume_id} not found")

    perfume_data = updated_perfume.model_dump(exclude_unset=True)
    # Use exclude_unset to avoid updating fields that were not provided

    for key, value in perfume_data.items():
        setattr(perfume_query, key, value)

    db.commit()

    db.refresh(perfume_query)

    return perfume_query
