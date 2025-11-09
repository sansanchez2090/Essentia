# 🧠 Essentia – Python Backend

This backend provides RESTful APIs for managing **perfumes and related entities**.  
It is built with **FastAPI**  and **SQLAlchemy** connected to a **PostgreSQL** database.  
The project exposes CRUD operations, automated API documentation via **Swagger**,  
and includes unit tests using **pytest**.

---

## ⚙️ Database Configuration

**Database:** PostgreSQL  
**ORM:** SQLAlchemy   

### 📁 Connection Script (`db.py`)

```python
from sqlalchemy import create_engine
from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy.orm import sessionmaker

DATABASE_URL = "postgresql://postgres:password@localhost:5432/essentia_db"

engine = create_engine(DATABASE_URL)
SessionLocal = sessionmaker(autocommit=False, autoflush=False, bind=engine)
Base = declarative_base()

💡 Replace postgres:password@localhost:5432 with your PostgreSQL credentials.
