from behave import given, when, then # type: ignore

# -------- ACTIONS --------

@when('I send a GET request to "{endpoint}"')
def step_get(context, endpoint):
    context.response = context.client.get(endpoint)


@when('I send a POST request to "{endpoint}"')
def step_post(context, endpoint):
    context.response = context.client.post(endpoint, json=context.payload)


@when('I send a PUT request to "{endpoint}"')
def step_put(context, endpoint):
    context.response = context.client.put(endpoint, json=context.payload)


@when('I send a DELETE request to "{endpoint}"')
def step_delete(context, endpoint):
    context.response = context.client.delete(endpoint)


# -------- GIVEN --------

@given('I have a valid perfume payload')
def step_payload(context):
    context.payload = {
        "name": "Essentia Test Perfume",
        "description": "A test perfume for acceptance tests.",
        "release_year": 2024,
        "gender": "Unisex",
        "image_url": "https://example.com/image.png"
    }


@given('a perfume exists with ID 1')
def step_existing_perfume(context):
    payload = {
        "name": "Existing Perfume",
        "description": "Initial description.",
        "release_year": 2020,
        "gender": "Male",
        "image_url": "https://example.com/existing.png"
    }

    context.client.post("/perfumes", json=payload)


@given('I have an updated perfume payload')
def step_updated_payload(context):
    context.payload = {
        "name": "Updated Perfume Name",
        "description": "Updated test description.",
        "release_year": 2025,
        "gender": "Female",
        "image_url": "https://example.com/updated.png"
    }


# -------- ASSERTIONS --------

@then('the response status should be {status:d}')
def step_status(context, status):
    assert context.response.status_code == status, \
        f"Expected {status}, got {context.response.status_code}"


@then('the response should contain a list of perfumes')
def step_list(context):
    data = context.response.json()
    assert isinstance(data, list), "Response is not a list"


@then('the response should contain the created perfume')
def step_created(context):
    data = context.response.json()
    for key in context.payload:
        assert data[key] == context.payload[key], f"Mismatch in {key}"


@then('the response should contain the updated perfume')
def step_updated(context):
    data = context.response.json()
    for key in context.payload:
        assert data[key] == context.payload[key], f"Mismatch in {key}"
