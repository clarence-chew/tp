package foodwhere.logic.parser;

import static foodwhere.testutil.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import foodwhere.logic.commands.*;
import org.junit.jupiter.api.Test;

import foodwhere.commons.core.Messages;
import foodwhere.logic.commands.SEditCommand;
import foodwhere.logic.parser.exceptions.ParseException;
import foodwhere.model.review.Review;
import foodwhere.model.stall.NameContainsKeywordsPredicate;
import foodwhere.model.stall.Stall;
import foodwhere.testutil.EditStallDescriptorBuilder;
import foodwhere.testutil.ReviewBuilder;
import foodwhere.testutil.StallBuilder;
import foodwhere.testutil.StallUtil;
import foodwhere.testutil.TypicalIndexes;

public class AddressBookParserTest {

    private final AddressBookParser parser = new AddressBookParser();

    @Test
    public void parseCommand_sadd() throws Exception {
        Stall stall = new StallBuilder().build();
        SAddCommand command = (SAddCommand) parser.parseCommand(StallUtil.getSAddCommand(stall));
        assertEquals(new SAddCommand(stall), command);
    }

    @Test
    public void parseCommand_radd() throws Exception {
        Review review = new ReviewBuilder().build();
        RAddCommand command = (RAddCommand) parser.parseCommand(RAddCommand.COMMAND_WORD + " "
                + "s/" + TypicalIndexes.INDEX_FIRST_REVIEW.getOneBased() + " "
                + "d/" + review.getDate() + " "
                + "c/" + review.getContent() + " "
                + "r/" + review.getRating());

        assertEquals(new RAddCommand(TypicalIndexes.INDEX_FIRST_REVIEW, review.getDate(),
                review.getContent(), review.getRating(), review.getTags()), command);
    }

    @Test
    public void parseCommand_clear() throws Exception {
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD) instanceof ClearCommand);
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD + " 3") instanceof ClearCommand);
    }

    @Test
    public void parseCommand_sdelete() throws Exception {
        SDeleteCommand command = (SDeleteCommand) parser.parseCommand(
                SDeleteCommand.COMMAND_WORD + " " + TypicalIndexes.INDEX_FIRST_STALL.getOneBased());
        assertEquals(new SDeleteCommand(TypicalIndexes.INDEX_FIRST_STALL), command);
    }

    @Test
    public void parseCommand_rdelete() throws Exception {
        RDeleteCommand command = (RDeleteCommand) parser.parseCommand(
                RDeleteCommand.COMMAND_WORD + " " + TypicalIndexes.INDEX_FIRST_REVIEW.getOneBased());
        assertEquals(new RDeleteCommand(TypicalIndexes.INDEX_FIRST_REVIEW), command);
    }

    @Test
    public void parseCommand_edit() throws Exception {
        Stall stall = new StallBuilder().build();
        SEditCommand.EditStallDescriptor descriptor = new EditStallDescriptorBuilder(stall).build();
        SEditCommand command = (SEditCommand) parser.parseCommand(SEditCommand.COMMAND_WORD + " "
                + TypicalIndexes.INDEX_FIRST_STALL.getOneBased() + " "
                + StallUtil.getEditStallDescriptorDetails(descriptor));
        assertEquals(new SEditCommand(TypicalIndexes.INDEX_FIRST_STALL, descriptor), command);
    }

    @Test
    public void parseCommand_exit() throws Exception {
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD) instanceof ExitCommand);
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD + " 3") instanceof ExitCommand);
    }

    @Test
    public void parseCommand_sfind() throws Exception {
        List<String> keywords = Arrays.asList("foo", "bar", "baz");
        SFindCommand command = (SFindCommand) parser.parseCommand(
                SFindCommand.COMMAND_WORD + " " + keywords.stream().collect(Collectors.joining(" ")));
        assertEquals(new SFindCommand(new NameContainsKeywordsPredicate(keywords)), command);
    }

    @Test
    public void parseCommand_help() throws Exception {
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD) instanceof HelpCommand);
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD + " 3") instanceof HelpCommand);
    }

    @Test
    public void parseCommand_slist() throws Exception {
        assertTrue(parser.parseCommand(SListCommand.COMMAND_WORD) instanceof SListCommand);
        assertTrue(parser.parseCommand(SListCommand.COMMAND_WORD + " 3") instanceof SListCommand);
    }

    @Test
    public void parseCommand_rlist() throws Exception {
        assertTrue(parser.parseCommand(RListCommand.COMMAND_WORD) instanceof RListCommand);
        assertTrue(parser.parseCommand(RListCommand.COMMAND_WORD + " 3") instanceof RListCommand);
    }

    @Test
    public void parseCommand_unrecognisedInput_throwsParseException() {
        assertThrows(ParseException.class,
                String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE), () ->
                        parser.parseCommand(""));
    }

    @Test
    public void parseCommand_unknownCommand_throwsParseException() {
        assertThrows(ParseException.class,
                Messages.MESSAGE_UNKNOWN_COMMAND, () -> parser.parseCommand("unknownCommand"));
    }
}
