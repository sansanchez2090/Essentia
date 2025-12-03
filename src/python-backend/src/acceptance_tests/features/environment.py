"""
This module sets up the test environment for acceptance tests
using Behave and FastAPI's TestClient.
"""

# pylint: disable=E0401, C0413
import os
import sys

current_dir = os.path.dirname(os.path.abspath(__file__))
backend_dir = os.path.abspath(os.path.join(current_dir, "..", ".."))
sys.path.insert(0, backend_dir)

from fastapi.testclient import TestClient  # type: ignore
from main import app


def before_all(context):
    """Set up the test client before all tests."""
    context.client = TestClient(app)
