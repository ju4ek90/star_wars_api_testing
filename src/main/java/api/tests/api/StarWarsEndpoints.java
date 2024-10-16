package api.tests.api;


public enum StarWarsEndpoints {

    FILMS("/films"),
    PEOPLE("/people");

    private final String value;

    StarWarsEndpoints(String val) {
        this.value = val;
    }

    public String getValue() {
        return value;
    }

}
