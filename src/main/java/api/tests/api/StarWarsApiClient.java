package api.tests.api;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static api.tests.enums.ApplicationProperties.HTTP_HOST;

public class StarWarsApiClient {


    private static final Logger logger = LoggerFactory.getLogger(StarWarsApiClient.class);

    public static Response getResponseForEndpoint(String endpoint, int statusCode) {
        Response response = null;
        try {
            response =
                    RestAssured
                            .given()
                            .baseUri(String.valueOf(HTTP_HOST.getValue()))
                            .when()
                            .get(endpoint)
                            .then()
                            .statusCode(statusCode)
                            .extract().response();
        } catch (Exception e) {
            logger.error("Error occurred while retrieving {} with expected statusCode {}", endpoint, statusCode);
        }
        return response;
    }


    public static List<String> getResponseForEndpointWithPagination(String endpoint, int statusCode) {
        List<String> characterUrls = getResponseForEndpoint(endpoint, statusCode).jsonPath().getList("results.url");

        int i = 2;
        String check = "";
        while (check != null) {
            try {
                check = getResponseForEndpoint(endpoint + "/?page=" + i, statusCode).jsonPath().get("next").toString();
            } catch (Exception e) {
                break;
            }
            List<String> characterUrlsTemp = getResponseForEndpoint(endpoint + "/?page=" + i, statusCode).jsonPath().getList("results.url");
            characterUrls.addAll(characterUrlsTemp);
            i++;
        }
        return characterUrls;
    }

}
