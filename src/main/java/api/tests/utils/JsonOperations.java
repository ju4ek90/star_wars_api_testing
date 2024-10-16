package api.tests.utils;

import api.tests.api.StarWarsApiClient;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

import static io.restassured.RestAssured.get;

public class JsonOperations {

    private static final Logger logger = LoggerFactory.getLogger(StarWarsApiClient.class);



    // Method to get the latest entity in descending list (by path tag)
    public static JSONObject getLatestEntity(List<?> entities, String pathTag) {
        return entities.stream()
                .map(entity -> new JSONObject((LinkedHashMap<?, ?>) entity)) // Convert LinkedHashMap to JSONObject
                .max(Comparator.comparing(entity -> entity.getString(pathTag)))
                .orElseThrow(() -> new RuntimeException("No data found"));
    }

//    // Method to get the latest entity in descending list with sending request to multiple urls (by path tag)
//    public static String getBiggestEntityFromUrls(List<String> entitiesUrl, String pathTag, String pathField) {
//        String latestEntityFromUrl = null;
//        try {
//            latestEntityFromUrl = entitiesUrl.stream()
//                    .map(url -> get(url).jsonPath().getString(pathTag) + ": " + get(url).jsonPath().getString(pathField))
//                    .max(Comparator.comparingInt(height -> Integer.parseInt(height.split(": ")[1])))
//                    .get();
//        } catch (Exception e) {
//            logger.error("Response empty or not success");
//        }
//        return latestEntityFromUrl;
//    }

    public static String getBiggestEntityFromUrls(List<String> entitiesUrl, String pathTag, String pathField) {
        String latestEntityFromUrl = null;
        try {
            latestEntityFromUrl = entitiesUrl.stream()
                    .map(url -> {
                        try {
                            // Fetch values from the JSON response
                            String tagValue = get(url).jsonPath().getString(pathTag);
                            String fieldValue = get(url).jsonPath().getString(pathField);

                            // Check if the fieldValue is a valid number before proceeding
                            if (fieldValue == null || fieldValue.isEmpty() || !fieldValue.matches("\\d+")) {
                                throw new NumberFormatException("Invalid number format: " + fieldValue);
                            }

                            return tagValue + ": " + fieldValue;
                        } catch (Exception e) {
                            // Log or handle error, continue with the stream
                            logger.error("Error processing URL: " + url, e);
                            return null;
                        }
                    })
                    // Filter out nulls caused by failed requests or errors
                    .filter(Objects::nonNull)
                    // Find the entity with the largest field value
                    .max(Comparator.comparingInt(entity -> {
                        String[] parts = entity.split(": ");
                        return Integer.parseInt(parts[1]); // parts[1] should now safely contain the number
                    }))
                    .orElseThrow(() -> new RuntimeException("No valid entity found"));
        } catch (Exception e) {
            logger.error("Response empty or not success", e);
        }
        return latestEntityFromUrl;
    }

}
