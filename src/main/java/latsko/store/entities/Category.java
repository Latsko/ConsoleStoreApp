package latsko.store.entities;

public record Category(String name, int id) {
    private static final String NAME_VALIDATION_REGEX = "(^[ąęŁłśćźńóżA-Za-z]+(\\s[ąęŁłśćźńóżA-Za-z]+){0,3})$";

    public Category {
        checkName(name);
    }

    private void checkName(final String name) {
        if (!name.matches(NAME_VALIDATION_REGEX)) {
            throw new IllegalArgumentException("Category name must consist of letters only and up to 4 words");
        }
    }

    public static boolean isNameCorrect(final String name) {
        return name.matches(NAME_VALIDATION_REGEX);
    }
}
