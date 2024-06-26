package seedu.realodex.logic;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import seedu.realodex.logic.parser.Prefix;
import seedu.realodex.model.person.Person;

/**
 * Container for user visible messages.
 */
public class Messages {

    public static final String MESSAGE_UNKNOWN_COMMAND = "Unknown command";
    public static final String MESSAGE_INVALID_COMMAND_FORMAT = "Invalid command format! \n%1$s";
    public static final String MESSAGE_INVALID_PERSON_DISPLAYED_INDEX = "The client index provided is invalid";
    public static final String MESSAGE_INVALID_PERSON_DISPLAYED_NAME = "The client name provided is invalid";
    public static final String MESSAGE_PERSONS_LISTED_OVERVIEW = "%1$d persons listed!";
    public static final String MESSAGE_DUPLICATE_FIELDS =
                "Too many values specified for the following single-valued field(s): ";
    public static final String MESSAGE_MISSING_PREFIXES = "Missing compulsory prefixes in the command! "
            + "Prefixes That Are Missed Are: ";

    /**
     * Returns an error message indicating the duplicate prefixes.
     */
    public static String getErrorMessageForDuplicatePrefixes(Prefix... duplicatePrefixes) {
        assert duplicatePrefixes.length > 0;

        Set<String> duplicateFields =
                Stream.of(duplicatePrefixes).map(Prefix::toString).collect(Collectors.toSet());

        return MESSAGE_DUPLICATE_FIELDS + String.join(" ", duplicateFields);
    }

    public static String getErrorMessageForMissingPrefixes(String exceptionMessageForMissingPrefixes) {
        return MESSAGE_MISSING_PREFIXES + exceptionMessageForMissingPrefixes;
    }

    /**
     * Formats the {@code person} for display to the user.
     */
    public static String format(Person person) {
        final StringBuilder builder = new StringBuilder();
        builder.append(person.getName())
                .append("; Phone: ")
                .append(person.getPhone())
                .append("; Income: ")
                .append(person.getIncome())
                .append("; Email: ")
                .append(person.getEmail())
                .append("; Address: ")
                .append(person.getAddress())
                .append("; Family: ")
                .append(person.getFamily())
                .append("; Tags: ");
        person.getTags().forEach(builder::append);
        builder.append("; Remark: ")
                .append(person.getRemark());
        builder.append("; Birthday: ").append(person.getBirthday());
        return builder.toString();
    }

}
