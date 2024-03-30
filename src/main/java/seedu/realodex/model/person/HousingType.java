package seedu.realodex.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.realodex.commons.util.AppUtil.checkArgument;

/**
 * Represents a Housing Type in the realodex.
 * Guarantees: immutable; name is valid as declared in {@link #isValidHousingType(String)}
 */
public class HousingType {

    public static final String MESSAGE_CONSTRAINTS = "Housing type should be either 'HDB', 'CONDOMINIUM', " +
            "'LANDED_PROPERTY' or 'GOOD_CLASS_BUNGALOW'";
    public static final String VALIDATION_REGEX = "[\\p{Alnum} ]+";
    private House housingType;
    public enum House {
        HDB, CONDOMINIUM, LANDED_PROPERTY, GOOD_CLASS_BUNGALOW
    }

    public HousingType(String housingType) {
        requireNonNull(housingType);
        checkArgument(isValidHousingType(housingType), MESSAGE_CONSTRAINTS);
        this.housingType = House.valueOf(housingType.toUpperCase());
    }
    
    /**
     * Returns true if a given string is a valid tag name.
     */
    public static boolean isValidHousingType(String test) {
        try {
            House.valueOf(test.toUpperCase().replace(" ", "_"));
        } catch (IllegalArgumentException e) {
            return false;
        }
        return test.matches(VALIDATION_REGEX);
    }

    /**
     * Returns a string representation of the family size with additional descriptive text.
     *
     * @return A string representation with descriptive text.
     */
    public String toStringWithRepresentation() {
        return "Preferred housing type is " + housingType;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof HousingType)) {
            return false;
        }

        HousingType otherHouse = (HousingType) other;
        return this.housingType.equals(otherHouse.housingType);
    }

    @Override
    public int hashCode() {
        return housingType.hashCode();
    }

    /**
     * Format state as text for viewing.
     */
    public String toString() {
        return '[' + housingType.toString() + ']';
    }

}
