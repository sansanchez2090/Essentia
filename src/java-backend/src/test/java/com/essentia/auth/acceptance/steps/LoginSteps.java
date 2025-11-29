package com.essentia.auth.acceptance.steps;

import io.cucumber.java.en.*;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import static org.junit.jupiter.api.Assertions.*;

public class LoginSteps {

    private Response response;

    @Given("the auth service is running")
    public void authServiceRunning() {
        RestAssured.baseURI = "http://localhost:8081/api";
    }

    @Given("a user exists with email {string} and password {string}")
    public void ensureUserExists(String email, String password) {
        // Create the user (assumes register endpoint exists)
        RestAssured.given()
                .header("Content-Type", "application/json")
                .body("{\"username\":\"testuser\", \"email\":\"" + email + "\", \"password\":\"" + password + "\"}")
                .post("/auth/register");
    }

    @When("I send a POST request to {string} with:")
    public void sendPostRequest(String endpoint, String jsonBody) {
        response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(jsonBody)
                .post(endpoint);
    }

    @Then("the response status should be {int}")
    public void responseStatusShouldBe(int status) {
        assertEquals(status, response.getStatusCode());
    }

    @Then("the response should contain a JWT token")
    public void responseShouldContainToken() {
        String token = response.jsonPath().getString("token");
        assertNotNull(token, "Token should not be null");
    }
}
