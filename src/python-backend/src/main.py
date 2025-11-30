"""This module initializes the FastAPI application and includes all route definitions."""

from fastapi import FastAPI  # type: ignore
from src import models
from src.db import ENGINE
from src.routes import perfume

# Create db tables
models.Base.metadata.create_all(bind=ENGINE)

app = FastAPI(title="Essentia API")

app.include_router(perfume.router)


@app.get("/")
def root():
    """
    Healthcheck and welcome route.
    """
    return {"message": "Welcome to the Essentia API!"}
