package foodwhere.model.stall;

import static foodwhere.testutil.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import foodwhere.logic.commands.CommandTestUtil;
import foodwhere.testutil.TypicalReviews;
import foodwhere.testutil.TypicalStalls;

public class StallTest {

    @Test
    public void asObservableList_modifyList_throwsUnsupportedOperationException() {
        Stall stall = new StallBuilder().build();
        assertThrows(UnsupportedOperationException.class, () -> stall.getTags().remove(0));
    }

    @Test
    public void isSameStall() {
        // same object -> returns true
        assertTrue(TypicalStalls.ALICE.isSameStall(TypicalStalls.ALICE));

        // null -> returns false
        assertFalse(TypicalStalls.ALICE.isSameStall(null));

        // same name, all other attributes different -> returns true
        Stall editedAlice = new StallBuilder(TypicalStalls.ALICE)
                .withAddress(CommandTestUtil.VALID_ADDRESS_BOB)
                .withTags(CommandTestUtil.VALID_TAG_HUSBAND)
                .withReviews(TypicalReviews.BOB)
                .build();
        assertTrue(TypicalStalls.ALICE.isSameStall(editedAlice));

        // different name, all other attributes same -> returns false
        editedAlice = new StallBuilder(TypicalStalls.ALICE).withName(CommandTestUtil.VALID_NAME_BOB).build();
        assertFalse(TypicalStalls.ALICE.isSameStall(editedAlice));

        // name has trailing spaces, all other attributes same -> returns false
        String nameWithTrailingSpaces = CommandTestUtil.VALID_NAME_BOB + " ";
        Stall editedBob = new StallBuilder(TypicalStalls.BOB).withName(nameWithTrailingSpaces).build();
        assertFalse(TypicalStalls.BOB.isSameStall(editedBob));
    }

    @Test
    public void equals() {
        // same values -> returns true
        Stall aliceCopy = new StallBuilder(TypicalStalls.ALICE).build();
        assertTrue(TypicalStalls.ALICE.equals(aliceCopy));

        // same object -> returns true
        assertTrue(TypicalStalls.ALICE.equals(TypicalStalls.ALICE));

        // null -> returns false
        assertFalse(TypicalStalls.ALICE.equals(null));

        // different type -> returns false
        assertFalse(TypicalStalls.ALICE.equals(5));

        // different stall -> returns false
        assertFalse(TypicalStalls.ALICE.equals(TypicalStalls.BOB));

        // different name -> returns false
        Stall editedAlice = new StallBuilder(TypicalStalls.ALICE).withName(CommandTestUtil.VALID_NAME_BOB).build();
        assertFalse(TypicalStalls.ALICE.equals(editedAlice));

        // different address -> returns false
        editedAlice = new StallBuilder(TypicalStalls.ALICE).withAddress(CommandTestUtil.VALID_ADDRESS_BOB).build();
        assertFalse(TypicalStalls.ALICE.equals(editedAlice));

        // different tags -> returns false
        editedAlice = new StallBuilder(TypicalStalls.ALICE).withTags(CommandTestUtil.VALID_TAG_HUSBAND).build();
        assertFalse(TypicalStalls.ALICE.equals(editedAlice));

        // different reviews -> returns false
        Stall alice = new StallBuilder(TypicalStalls.ALICE).withReviews(TypicalReviews.ALICE).build();
        editedAlice = new StallBuilder(TypicalStalls.ALICE).withReviews(TypicalReviews.BOB).build();
        assertFalse(alice.equals(editedAlice));
    }
}
