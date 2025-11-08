from fastapi.testclient import TestClient # type: ignore
from main import app

client = TestClient(app)

def test_get_all_perfumes():
    response = client.get("/perfumes")
    assert response.status_code == 200
    assert isinstance(response.json(), list)

def test_get_perfume_by_id():
    # Crear un perfume primero
    perfume = {
        "name": "Azure Bloom",
        "description": "Fresh oceanic floral fragrance.",
        "release_year": 2021,
        "gender": "Feminine",
        "image_url": "https://example.com/azurebloom.jpg"
    }
    create_response = client.post("/perfumes", json=perfume)
    perfume_id = create_response.json()["id"]

    # Obtenerlo por ID
    response = client.get(f"/perfumes/{perfume_id}")
    assert response.status_code == 200
    data = response.json()
    assert data["name"] == "Azure Bloom"
    assert data["gender"] == "Feminine"

def test_delete_perfume():
    # Crear perfume
    perfume = {
        "name": "Velvet Amber",
        "description": "Smooth amber and vanilla notes.",
        "release_year": 2019,
        "gender": "Unisex",
        "image_url": "https://example.com/velvetamber.jpg"
    }
    create_response = client.post("/perfumes", json=perfume)
    perfume_id = create_response.json()["id"]

    # Eliminarlo
    delete_response = client.delete(f"/perfumes/{perfume_id}")
    assert delete_response.status_code == 200
    assert delete_response.json()["detail"] == "Perfume deleted successfully"

    # Confirmar que ya no existe
    get_response = client.get(f"/perfumes/{perfume_id}")
    assert get_response.status_code == 404
