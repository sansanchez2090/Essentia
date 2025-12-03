"""This module contains tests for the perfume API routes."""

# pylint: disable=E0401, C0413

import os
import sys

current_dir = os.path.dirname(os.path.abspath(__file__))
backend_dir = os.path.abspath(os.path.join(current_dir, "..", ".."))
sys.path.insert(0, backend_dir)

from fastapi.testclient import TestClient  # type: ignore
from main import app

client = TestClient(app)


def test_get_all_perfumes():
    """Tester to get all perfumes"""
    response = client.get("/perfumes")
    assert response.status_code == 200
    assert isinstance(response.json(), list)


def test_get_perfume_by_id():
    """Tester to get perfume by ID"""
    # Create a perfume
    perfume = {
        "name": "Azure Bloom",
        "description": "Fresh oceanic floral fragrance.",
        "release_year": 2021,
        "gender": "Feminine",
        "image_url": "https://example.com/azurebloom.jpg",
    }
    create_response = client.post("/perfumes", json=perfume)
    perfume_id = create_response.json()["id"]

    # ID
    response = client.get(f"/perfumes/{perfume_id}")
    assert response.status_code == 200
    data = response.json()
    assert data["name"] == "Azure Bloom"
    assert data["gender"] == "Feminine"


def test_update_perfume():
    """Tester to update perfume by ID"""
    # Crear perfume
    perfume = {
        "name": "Crimson Oud",
        "description": "Rich smoky oud and rose.",
        "release_year": 2020,
        "gender": "Masculine",
        "image_url": "https://example.com/crimsonoud.jpg",
    }
    create_response = client.post("/perfumes", json=perfume)
    perfume_id = create_response.json()["id"]

    # Actualizar descripción
    updated_data = {"description": "Deep woody oud with a touch of rose."}
    response = client.put(f"/perfumes/{perfume_id}", json=updated_data)
    assert response.status_code == 200
    updated_perfume = response.json()
    assert updated_perfume["description"] == "Deep woody oud with a touch of rose."


def test_delete_perfume():
    """Tester to delete perfume by ID"""
    # Crear perfume
    perfume = {
        "name": "Velvet Amber",
        "description": "Smooth amber and vanilla notes.",
        "release_year": 2019,
        "gender": "Unisex",
        "image_url": "https://example.com/velvetamber.jpg",
    }
    create_response = client.post("/perfumes", json=perfume)
    perfume_id = create_response.json()["id"]

    # Deleting
    delete_response = client.delete(f"/perfumes/{perfume_id}")
    assert delete_response.status_code == 204
    assert not delete_response.text

    # Validating
    get_response = client.get(f"/perfumes/{perfume_id}")
    assert get_response.status_code == 404


def test_create_perfume():
    """Tester to create a new perfume"""
    new_perfume = {
        "name": "Gold Lash",
        "description": "A magical light amber scent.",
        "release_year": 2022,
        "gender": "Unisex",
        "image_url": "https://example.com/goldlash.jpg",
    }

    response = client.post("/perfumes", json=new_perfume)
    assert response.status_code == 201  # created
    data = response.json()
    assert data["name"] == new_perfume["name"]
    assert data["gender"] == new_perfume["gender"]
    assert "id" in data
