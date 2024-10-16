package api.tests.functional.tests;

import com.jayway.jsonpath.JsonPath;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.util.List;

import static api.tests.api.StarWarsApiClient.getResponseForEndpoint;
import static api.tests.api.StarWarsApiClient.getResponseForEndpointWithPagination;
import static api.tests.api.StarWarsEndpoints.FILMS;
import static api.tests.api.StarWarsEndpoints.PEOPLE;
import static api.tests.test.data.StatusCodes.SUCCESS_STATUS_CODE;
import static api.tests.utils.JsonOperations.getBiggestEntityFromUrls;
import static api.tests.utils.JsonOperations.getLatestEntity;

public class UseCase1 {

    @Test
    public void findTallestCharacterInLatestFilm() {

        // Step 1: Get all films and find the latest release
        List<JSONObject> films = getResponseForEndpoint(FILMS.getValue(), SUCCESS_STATUS_CODE).jsonPath().getList("results");

        // Sort films by release date in descending order and get last entity for specified tag
        String lastFilm = getLatestEntity(films, "release_date").toString();

        // Step 2: Get the characters from the latest film
        List<String> characterUrls = JsonPath.parse(lastFilm).read("$.characters", List.class);

        // Step 3: Find the tallest character
        String tallestCharacter = getBiggestEntityFromUrls(characterUrls, "name", "height");

        System.out.println("Tallest Character in Latest Film: " + tallestCharacter);
    }

    @Test
    public void findTallestCharacterOverall() {

        // Step 1: Get all characters (pagination may be required for larger datasets)
        List<String> characterUrls = getResponseForEndpointWithPagination(PEOPLE.getValue(), SUCCESS_STATUS_CODE);

        // Step 2: Find the tallest character across all films
        String tallestCharacter = getBiggestEntityFromUrls(characterUrls, "name", "height");

        System.out.println("Tallest Character Overall: " + tallestCharacter);
    }
}

