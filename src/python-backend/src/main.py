from fastapi import FastAPI# type: ignore
from src import models
from src.db import engine
from src.routes import perfume

# Create db tables
models.Base.metadata.create_all(bind=engine)

app = FastAPI(title="Essentia API")

app.include_router(perfume.router)

@app.get("/")
def root():
    return {"message": "Welcome to the Essentia API!"}