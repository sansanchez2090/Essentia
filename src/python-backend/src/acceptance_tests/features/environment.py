"""
This module sets up the test environment for acceptance tests
using Behave and FastAPI's TestClient.
   """
from fastapi.testclient import TestClient # type: ignore
from main import app 

def before_all(context):
    context.client = TestClient(app)
