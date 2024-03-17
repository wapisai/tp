package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.ADDRESS_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.ADDRESS_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.EMAIL_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.EMAIL_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.FAMILY_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.FAMILY_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.INCOME_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.INCOME_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_ADDRESS_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_EMAIL_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_NAME_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_PHONE_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_TAG_DESC;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.PHONE_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.PHONE_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.PREAMBLE_NON_EMPTY;
import static seedu.address.logic.commands.CommandTestUtil.PREAMBLE_WHITESPACE;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.REMARK_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.REMARK_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_REMARK_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_REMARK_BOB;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_FAMILY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_INCOME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_REMARK;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalPersons.BOB;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.CommandTestUtil;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.tag.Tag;
import seedu.address.testutil.PersonBuilder;

public class AddCommandParserTest {
    private AddCommandParser parser = new AddCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        Person expectedPerson = new PersonBuilder(BOB).build();

        // whitespace only preamble
        assertParseSuccess(parser,
                           PREAMBLE_WHITESPACE + NAME_DESC_BOB + PHONE_DESC_BOB + INCOME_DESC_BOB + EMAIL_DESC_BOB
                                   + ADDRESS_DESC_BOB + FAMILY_DESC_BOB + TAG_DESC_BOB + REMARK_DESC_BOB,
                new AddCommand(expectedPerson));


        // multiple tags - all accepted
        Person expectedPersonMultipleTags = new PersonBuilder(BOB).withTags(VALID_TAG_BOB, VALID_TAG_AMY)
                .build();
        assertParseSuccess(parser,
                NAME_DESC_BOB + PHONE_DESC_BOB + INCOME_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB + FAMILY_DESC_BOB
                        + TAG_DESC_BOB + TAG_DESC_AMY + REMARK_DESC_BOB,
                new AddCommand(expectedPersonMultipleTags));
    }

    @Test
    public void parse_repeatedNonTagValue_failure() {
        String validExpectedPersonString = NAME_DESC_BOB + PHONE_DESC_BOB + INCOME_DESC_BOB + EMAIL_DESC_BOB
                + ADDRESS_DESC_BOB + FAMILY_DESC_BOB + TAG_DESC_BOB;

        // multiple names
        assertParseFailure(parser, NAME_DESC_AMY + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME));

        // multiple phones
        assertParseFailure(parser, PHONE_DESC_AMY + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE));

        // multiple emails
        assertParseFailure(parser, EMAIL_DESC_AMY + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_EMAIL));

        // multiple addresses
        assertParseFailure(parser, ADDRESS_DESC_AMY + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_ADDRESS));

        // multiple fields repeated
        assertParseFailure(parser,
                validExpectedPersonString + PHONE_DESC_AMY + INCOME_DESC_AMY + EMAIL_DESC_AMY
                        + NAME_DESC_AMY + ADDRESS_DESC_AMY + FAMILY_DESC_AMY
                        + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME, PREFIX_INCOME, PREFIX_ADDRESS, PREFIX_EMAIL,
                                                             PREFIX_PHONE, PREFIX_FAMILY));

        // invalid value followed by valid value

        // invalid name
        assertParseFailure(parser, INVALID_NAME_DESC + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME));

        // invalid email
        assertParseFailure(parser, INVALID_EMAIL_DESC + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_EMAIL));

        // invalid phone
        assertParseFailure(parser, INVALID_PHONE_DESC + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE));

        // invalid address
        assertParseFailure(parser, INVALID_ADDRESS_DESC + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_ADDRESS));

        // valid value followed by invalid value

        // invalid name
        assertParseFailure(parser, validExpectedPersonString + INVALID_NAME_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME));

        // invalid email
        assertParseFailure(parser, validExpectedPersonString + INVALID_EMAIL_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_EMAIL));

        // invalid phone
        assertParseFailure(parser, validExpectedPersonString + INVALID_PHONE_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE));

        // invalid address
        assertParseFailure(parser, validExpectedPersonString + INVALID_ADDRESS_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_ADDRESS));
    }

    @Test
    public void parse_compulsoryFieldMissing_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE);

        // missing name prefix
        assertParseFailure(parser,
                           VALID_NAME_BOB + PHONE_DESC_BOB + INCOME_DESC_BOB + EMAIL_DESC_BOB
                                   + ADDRESS_DESC_BOB + FAMILY_DESC_BOB + TAG_DESC_BOB,
                expectedMessage);

        // missing phone prefix
        assertParseFailure(parser,
                           NAME_DESC_BOB + VALID_PHONE_BOB + INCOME_DESC_BOB
                                   + EMAIL_DESC_BOB + ADDRESS_DESC_BOB + FAMILY_DESC_BOB + TAG_DESC_BOB,
                expectedMessage);

        // missing email prefix
        assertParseFailure(parser,
                           NAME_DESC_BOB + PHONE_DESC_BOB + INCOME_DESC_BOB
                                   + VALID_EMAIL_BOB + ADDRESS_DESC_BOB + FAMILY_DESC_BOB + TAG_DESC_BOB,
                expectedMessage);

        // missing address prefix
        assertParseFailure(parser,
                           NAME_DESC_BOB + PHONE_DESC_BOB + INCOME_DESC_BOB
                                   + EMAIL_DESC_BOB + VALID_ADDRESS_BOB + FAMILY_DESC_BOB + TAG_DESC_BOB,
                expectedMessage);

        // all prefixes missing
        assertParseFailure(parser,
                           VALID_NAME_BOB + VALID_PHONE_BOB + INCOME_DESC_BOB
                                   + VALID_EMAIL_BOB + VALID_ADDRESS_BOB + FAMILY_DESC_BOB + VALID_TAG_BOB,
                expectedMessage);

        // missing tag prefix
        assertParseFailure(parser,
                VALID_NAME_BOB + VALID_PHONE_BOB + INCOME_DESC_BOB
                        + VALID_EMAIL_BOB + VALID_ADDRESS_BOB + FAMILY_DESC_BOB,
                expectedMessage);
    }

    @Test
    public void parse_invalidValue_failure() {
        System.out.println(INVALID_NAME_DESC + PHONE_DESC_BOB + INCOME_DESC_BOB + EMAIL_DESC_BOB
                + ADDRESS_DESC_BOB + FAMILY_DESC_BOB + TAG_DESC_BOB
                + CommandTestUtil.TAG_DESC_BOB);

        // invalid name
        assertParseFailure(parser,
                           INVALID_NAME_DESC + PHONE_DESC_BOB + INCOME_DESC_BOB + EMAIL_DESC_BOB
                                   + ADDRESS_DESC_BOB + FAMILY_DESC_BOB + TAG_DESC_BOB, Name.MESSAGE_CONSTRAINTS);

        // invalid phone
        assertParseFailure(parser,
                           NAME_DESC_BOB + INVALID_PHONE_DESC + INCOME_DESC_BOB + EMAIL_DESC_BOB
                                   + ADDRESS_DESC_BOB + FAMILY_DESC_BOB
                                   + TAG_DESC_BOB, Phone.MESSAGE_CONSTRAINTS);

        // invalid email
        assertParseFailure(parser,
                           NAME_DESC_BOB + PHONE_DESC_BOB
                                   + INCOME_DESC_BOB + INVALID_EMAIL_DESC + ADDRESS_DESC_BOB + FAMILY_DESC_BOB
                                   + CommandTestUtil.TAG_DESC_BOB + TAG_DESC_BOB, Email.MESSAGE_CONSTRAINTS);

        // invalid address
        assertParseFailure(parser,
                           NAME_DESC_BOB + PHONE_DESC_BOB + INCOME_DESC_BOB
                                   + EMAIL_DESC_BOB + INVALID_ADDRESS_DESC + FAMILY_DESC_BOB
                                   + CommandTestUtil.TAG_DESC_BOB + TAG_DESC_BOB, Address.MESSAGE_CONSTRAINTS);

        // invalid tag
        assertParseFailure(parser,
                           NAME_DESC_BOB + PHONE_DESC_BOB + INCOME_DESC_BOB
                                   + EMAIL_DESC_BOB + ADDRESS_DESC_BOB + FAMILY_DESC_BOB
                                   + INVALID_TAG_DESC + VALID_TAG_BOB, Tag.MESSAGE_CONSTRAINTS);

        // two invalid values, only first invalid value reported
        assertParseFailure(parser,
                           INVALID_NAME_DESC + PHONE_DESC_BOB + INCOME_DESC_BOB
                                   + EMAIL_DESC_BOB + INVALID_ADDRESS_DESC + FAMILY_DESC_BOB,
                           Name.MESSAGE_CONSTRAINTS);

        // non-empty preamble
        assertParseFailure(parser,
                           PREAMBLE_NON_EMPTY + NAME_DESC_BOB + PHONE_DESC_BOB + INCOME_DESC_BOB + EMAIL_DESC_BOB
                + ADDRESS_DESC_BOB + FAMILY_DESC_BOB + CommandTestUtil.TAG_DESC_BOB + TAG_DESC_BOB,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
    }
}
