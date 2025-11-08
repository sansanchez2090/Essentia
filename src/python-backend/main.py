from fastapi import FastAPI, Depends, HTTPException # type: ignore
from sqlalchemy.orm import Session # type: ignore
import models, schemas
from db import SessionLocal, engine
from typing import Annotated

# Create db tables
models.Base.metadata.create_all(bind=engine)

app = FastAPI(title="Essentia API")

# Obtain session to the database

def get_db():
    db = SessionLocal()
    try:
        yield db
    finally:
        db.close()

@app.get("/")
def root():
    return {"message": "Welcome to the Essentia API!"}

@app.get("/perfumes/{perfume_id}")
async def read_perfumes(perfume_id: int, db: Annotated[Session, Depends(get_db)]):
    perfume = db.query(models.Perfume).filter(models.Perfume.id == perfume_id).first()
    if perfume is None:
        raise HTTPException(status_code=404, detail="Perfume not found")
    return perfume

@app.post("/perfumes")
def create_perfume(perfume: schemas.PerfumeBase, db: Annotated[Session, Depends(get_db)]):
    db_perfume = models.Perfume(name=perfume.name,
                                description=perfume.description,
                                release_year=perfume.release_year,
                                gender=perfume.gender,
                                image_url=perfume.image_url)
    db.add(db_perfume)
    db.commit()
    db.refresh(db_perfume)