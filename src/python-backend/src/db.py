"""This module sets up the database connection and session management."""
import os
from dotenv import load_dotenv #type: ignore
from sqlalchemy import create_engine #type: ignore
from sqlalchemy.orm import sessionmaker #type:ignore
from sqlalchemy.ext.declarative import declarative_base #type: ignore

load_dotenv()

ENGINE = create_engine(os.getenv('URL_DATABASE'))

SESSION_LOCAL = sessionmaker(autocommit=False, autoflush=False, bind=ENGINE)

Base = declarative_base()


def get_db():
    """
    This function provides a database session to the API routes.
    It ensures that the session is properly closed after use.
    """
    database = SESSION_LOCAL()
    try:
        yield database
    finally:
        database.close()
