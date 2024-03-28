package seedu.realodex.logic.parser;

import static seedu.realodex.logic.parser.CliSyntax.PREFIX_REMARK;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import seedu.realodex.logic.Messages;
import seedu.realodex.logic.parser.exceptions.ParseException;

/**
 * Stores mapping of prefixes to their respective arguments.
 * Each key may be associated with multiple argument values.
 * Values for a given key are stored in a list, and the insertion ordering is maintained.
 * Keys are unique, but the list of argument values may contain duplicate argument values, i.e. the same argument value
 * can be inserted multiple times for the same prefix.
 */
public class ArgumentMultimap {

    /** Prefixes mapped to their respective arguments**/
    private final Map<Prefix, List<String>> argMultimap = new HashMap<>();

    /**
     * Associates the specified argument value with {@code prefix} key in this map.
     * If the map previously contained a mapping for the key, the new value is appended to the list of existing values.
     *
     * @param prefix   Prefix key with which the specified argument value is to be associated
     * @param argValue Argument value to be associated with the specified prefix key
     */
    public void put(Prefix prefix, String argValue) {
        List<String> argValues = getAllValues(prefix);
        argValues.add(argValue);
        argMultimap.put(prefix, argValues);
    }

    /**
     * Returns the last value of {@code prefix}.
     */
    public Optional<String> getValue(Prefix prefix) {
        List<String> values = getAllValues(prefix);
        if (prefix.equals(PREFIX_REMARK)) {
            return values.isEmpty() ? Optional.of("") : Optional.of(values.get(values.size() - 1));
        }
        return values.isEmpty() ? Optional.empty() : Optional.of(values.get(values.size() - 1));
    }

    /**
     * Returns all values of {@code prefix}.
     * If the prefix does not exist or has no values, this will return an empty list.
     * Modifying the returned list will not affect the underlying data structure of the ArgumentMultimap.
     */
    public List<String> getAllValues(Prefix prefix) {
        if (!argMultimap.containsKey(prefix)) {
            return new ArrayList<>();
        }
        return new ArrayList<>(argMultimap.get(prefix));
    }

    /**
     * Returns the preamble (text before the first valid prefix). Trims any leading/trailing spaces.
     */
    public String getPreamble() {
        return getValue(new Prefix("", "")).orElse("");
    }

    /**
     * Throws a {@code ParseException} if any of the prefixes given in {@code prefixes} appeared more than
     * once among the arguments.
     */
    public void verifyNoDuplicatePrefixesFor(Prefix... prefixes) throws ParseException {
        Prefix[] duplicatedPrefixes = Stream.of(prefixes).distinct()
                .filter(prefix -> argMultimap.containsKey(prefix) && argMultimap.get(prefix).size() > 1)
                .toArray(Prefix[]::new);

        if (duplicatedPrefixes.length > 0) {
            throw new ParseException(Messages.getErrorMessageForDuplicatePrefixes(duplicatedPrefixes));
        }
    }

    /**
     * Checks if the specified prefix is present in the argument multimap.
     * This method is useful for determining if an argument associated
     * with a particular prefix was provided by the user.
     *
     * @param prefix The {@link Prefix} to check for presence in the argument multimap.
     * @return {@code true} if the prefix is present in the argument multimap, {@code false} otherwise.
     */
    public boolean containsPrefix(Prefix prefix) {
        return argMultimap.containsKey(prefix);
    }

    //this will always return true for the PREFIX_REMARK due to its special nature
    public Prefix findPresentPrefix(Prefix...prefixes) {
        for (Prefix prefix : prefixes) {
            if (this.getValue(prefix).isPresent()) {
                return prefix;
            }
        }
        return null;
    }

    /**
     * Returns a message listing the missing prefixes from the given list of compulsory prefixes.
     *
     * @param listOfCompulsoryPrefix An array of Prefix objects representing compulsory prefixes.
     * @return A string message listing the missing prefixes.
     */
    public String returnMessageOfMissingPrefixes(Prefix[] listOfCompulsoryPrefix) {
        return Prefix.returnMessageOfMissingPrefixes(argMultimap, listOfCompulsoryPrefix);
    }

    /**
     * Returns an array of unique Prefix objects from the provided list of prefixes.
     *
     * @param prefixes Variable number of Prefix objects to be filtered for uniqueness.
     * @return An array of unique Prefix objects.
     */
    public Prefix[] returnListOfCompulsoryTags(Prefix... prefixes) {
        return Stream.of(prefixes)
                .distinct()
                .toArray(Prefix[]::new);
    }
}
