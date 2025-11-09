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

```

💡 Replace postgres:password@localhost:5432 with your PostgreSQL credentials.

## 🧠 API Documentation

FastAPI automatically generates interactive API documentation:

| Type | URL |
| :--- | :--- |
| **Swagger UI** | `http://localhost:8000/docs` ↗️ |
| **ReDoc** | `http://localhost:8000/redoc` ↗️ |

## ⚙️SetUp Instructions

Follow these steps to run backend locally

### 1️⃣ Clone the repository

```bash
git clone [https://github.com/yourusername/Essentia.git](https://github.com/yourusername/Essentia.git)
cd src/python-backend

```

### 2️⃣ Create and activate Virtual Environment

```bash
python -m venv venv

# Windows
venv\Scripts\activate

# Linux / macOS
source venv/bin/activate

```

### 3️⃣ Install Dependencies

```bash
pip install -r requirements.txt
```

### 4️⃣ SetUp Database

Ensure your PostgreSQL server is running and update your credentials in db.py.

### 5️⃣ Run the FastAPI Api
API Available at:

➡️ `http://localhost:8000`

## 🧱 Folder Structure

```graphql
python-backend/
├── routes/
│   └── perfume_routes.py  # Endpoints for perfume CRUD
├── tests/
│   ├── test_perfumes.py  # CRUD test cases for perfume
├── schemas/ 
│   ├── Perfume.py       
│   ├── PerfumeHouse.py  
│   └── Concentration.py              
└── db.py                  # DB connection & Base config
```