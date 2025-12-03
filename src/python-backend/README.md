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

#### ✔ Install Poetry
If you haven't already:
```bash
(Invoke-WebRequest -Uri https://install.python-poetry.org -UseBasicParsing).Content | python -

```
### 3️⃣ Install dependencies and run the environment

Poetry 2.x no longer enables poetry shell by default, so activation works differently.

#### 🟦 Poetry ≥ 2.0.0 (recommended)
```bash
poetry install
```
**Run the environment**

```bash
poetry run uvicorn src.main:app --reload
```

#### 🟩 Poetry ≤ 1.6 (legacy versions)
Older versions:

```bash
poetry shell

```

```bash
uvicorn main:app --reload

```

### 4️⃣ SetUp Database

Ensure your PostgreSQL server is running and update your credentials in db.py.

### 5️⃣ Run the FastAPI Api
API Available at:

➡️ `http://localhost:8000`

## Project Testing 🧪

This project utilizes two main types of tests to ensure quality and correct API behavior: **Unit Tests** (using Pytest) and **Acceptance Tests** (using Behave). Both are executed from within your Poetry-managed virtual environment.

---

## 1. 🔬 Unit Tests (Pytest)

Unit tests are designed to verify the functioning of the smallest units of code (functions, methods) in isolation.

### Location

Tests are located in the root `tests/` directory and follow the naming pattern `test_*.py`.

### Execution

To run **all** unit tests, use the following command from the project root (`python-backend/`):

```bash
poetry run pytest

```

## 2. 🧩 Acceptance Tests (Behave)

Acceptance tests (or functional tests) verify that the system meets business requirements by testing the full application flow (FastAPI endpoints, business logic, and database persistence).

### Location

ests are located in `src/acceptance_tests/features/`

### Execution

To run **all** unit tests, use the following command from the project root (`python-backend/`):

```bash
poetry run behave src/acceptance_tests/features

```

## 🧱 Folder Structure

```graphql
python-backend/
├── src/  # Todo el código fuente
│   ├── acceptance_tests/ 
│   │   ├── features/
│   │   │   ├── perfume.feature
│   │   │   ├── environment.py
│   │   │   └── steps/
│   │   │       └── perfumes_steps.py
│   │
│   ├── routes/
│   │   └── perfume_routes.py
│   ├── models.py
│   ├── schemas.py
│   ├── main.py  
│   └── db.py
│
│   ├──tests/
│   └── test_perfumes.py
├── pyproject.toml
└── README.md                 
```