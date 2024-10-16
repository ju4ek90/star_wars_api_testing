package api.tests.enums;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.Properties;

public enum ApplicationProperties {
    HTTP_HOST("http.host"),

    ENV("dev"),
    RUN_STATE("local");

    static Properties properties = new Properties();
    public static final Logger LOGGER = LoggerFactory.getLogger(ApplicationProperties.class);

    //private static final ErrorManager LOGGER = null;

    static {

        RUN_STATE.name = Optional.ofNullable(System.getProperty("runState"))
                .orElse(RUN_STATE.name);

        ENV.name = Optional.ofNullable(System.getProperty("env"))
                .orElse(ENV.name);

        //LOGGER.info("Running in environment: {}", ENV.name);
        String envPropertiesFileName = String.format("properties/%s.application.properties", ENV.name);

        try (
                InputStream envInput = ApplicationProperties.class.getClassLoader().getResourceAsStream(envPropertiesFileName)
        ) {
            /* Properties.load() does not delete existing entries */
            properties.load(envInput);

            /*
             * Try to find property value in application properties files. Can be overridden by system properties.
             */
            for (ApplicationProperties property : ApplicationProperties.values()) {
                property.value = Optional.ofNullable(System.getProperty(property.name))
                        .orElseGet(() -> properties.getProperty(property.name));
            }

        } catch (IOException | NullPointerException e) {
             LOGGER.error("Could not load properties from {}", envPropertiesFileName, e);
        }
    }

    String value;
    String name;

    ApplicationProperties(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

}
