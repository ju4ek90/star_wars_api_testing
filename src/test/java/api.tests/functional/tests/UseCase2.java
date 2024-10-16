package api.tests.functional.tests;


import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Paths;

import static api.tests.api.StarWarsApiClient.getResponseForEndpoint;
import static api.tests.api.StarWarsEndpoints.PEOPLE;
import static api.tests.test.data.StatusCodes.SUCCESS_STATUS_CODE;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchema;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class UseCase2 {


    // Test method to validate the /people API response schema
    @Test
    public void validatePeopleApiResponseSchema() {
        // Load JSON schema and validate response
        assertDoesNotThrow(() -> {
            // Step 1: Load the JSON schema from the file
            String peopleApiSchema = new String(Files.readAllBytes(Paths.get("src/test/resources/people-schema.json")));

            // Step 2: Validate the response body against the JSON schema
            getResponseForEndpoint(PEOPLE.getValue(), SUCCESS_STATUS_CODE)
                    .then()
                    .assertThat()
                    .body(matchesJsonSchema(peopleApiSchema));
        }, "Schema validation failed!");
    }
}
