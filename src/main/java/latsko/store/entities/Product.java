package latsko.store.entities;

public record Product(int id, double price, String name, Category category, int quantity) {
    private static final String NAME_VALIDATION_REGEX = "^([ąęŁłśćźńóża-zA-Z+\\-0-9.]+)(\\s[ąęŁłśćźńóża-zA-Z+\\-0-9.]+){0,7}$";

    public Product {
        checkPrice(price);
        checkQuantity(quantity);
        checkCategory(category);
        checkName(name);
    }


    private void checkName(final String name) {
        if (!name.matches(NAME_VALIDATION_REGEX) || name.length() > 50) {
            throw new IllegalArgumentException("Name should have no more than 50 characters and consist up to 8 words");
        }
    }

    private void checkCategory(final Category category) {
        if (category == null) {
            throw new IllegalArgumentException("Category must not be null");
        }
    }

    private void checkPrice(final double price) {
        if (price < 0) {
            throw new IllegalArgumentException("Price lesser then zero cannot be assigned");
        }
    }

    private void checkQuantity(final int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity lesser then zero cannot be assigned");
        }
    }

    public static boolean nameIsCorrect(final String name) {
        return name.matches(NAME_VALIDATION_REGEX);
    }

}
