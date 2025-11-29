"""Defines the API routes for managing Perfume entities."""

from typing import Annotated, List
from fastapi import APIRouter, Depends, HTTPException # type: ignore
from sqlalchemy.orm import Session # type: ignore
from src import models, schemas
from src.db import get_db


router = APIRouter(prefix="/perfumes", tags=["Perfumes"])

@router.get("/", response_model=List[schemas.PerfumeBase])
async def get_perfumes(db: Annotated[Session, Depends(get_db)]):
    """
    Retrieves all perfumes.
    """
    perfumes = db.query(models.Perfume).all()
    return perfumes

@router.get("/{perfume_id}", response_model=schemas.PerfumeBase)
async def get_perfumes_by_id(perfume_id: int, db: Annotated[Session, Depends(get_db)]):
    """
    Retrieve a perfume by it'd ID.
    """
    perfume = db.query(models.Perfume).filter(models.Perfume.id == perfume_id).first()
    if perfume is None:
        raise HTTPException(status_code=404, detail=f"Perfume with ID: {perfume_id} not found")
    return perfume

@router.post("/", response_model=schemas.PerfumeBase)
def create_perfume(perfume: schemas.PerfumeCreate, db: Annotated[Session, Depends(get_db)]):
    """
    Creates a perfume
    """
    db_perfume = models.Perfume(**perfume.model_dump())
    db.add(db_perfume)
    db.commit()
    db.refresh(db_perfume)

    return db_perfume

@router.delete("/{perfume_id}")
async def delete_perfume(perfume_id: int, db: Annotated[Session, Depends(get_db)]):
    """
    Deletes a perfume by it'd ID.
    """
    perfume = db.query(models.Perfume).filter(models.Perfume.id == perfume_id).first()
    if perfume is None:
        raise HTTPException(status_code=404, detail=f"Perfume with ID: {perfume_id} not found")
    db.delete(perfume)
    db.commit()
    return {"detail": "Perfume deleted successfully"}

@router.put("/{perfume_id}", response_model=schemas.PerfumeBase)
async def update_perfume(perfume_id: int,
                         updated_perfume: schemas.PerfumeUpdate,
                         db: Annotated[Session, Depends(get_db)]):
    """
    Updates information by it's ID.
    """

    perfume_query = db.query(models.Perfume).filter(models.Perfume.id == perfume_id).first()

    if perfume_query is None:
        raise HTTPException(status_code=404, detail=f"Perfume with ID: {perfume_id} not found")

    perfume_data = updated_perfume.model_dump(exclude_unset=True)

    for key, value in perfume_data.items():
        setattr(perfume_query, key, value)

    db.commit()

    db.refresh(perfume_query)

    return perfume_query
