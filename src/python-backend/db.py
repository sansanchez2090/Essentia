from sqlalchemy import create_engine #type: ignore
from sqlalchemy.orm import sessionmaker #type:ignore
from sqlalchemy.ext.declarative import declarative_base #type: ignore
import os
from dotenv import load_dotenv #type: ignore

load_dotenv()

engine = create_engine(os.getenv('URL_DATABASE'))

SessionLocal = sessionmaker(autocommit=False, autoflush=False, bind=engine)

Base = declarative_base()