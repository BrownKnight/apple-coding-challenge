package com.applecodingchallenge;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class InMemoryStoreTest {
    //region Tests for the "happy path"
    @Test
    void GivenSingleLengthSequenceAColourCanBeStoredAndRetrieved() {
        InMemoryStore inMemoryStore = new InMemoryStore();
        String inputString = "00-00";
        Colour expectedColour = Colour.YELLOW;

        inMemoryStore.store(inputString, expectedColour);

        Colour actualColour = inMemoryStore.get("00");

        assertEquals(expectedColour, actualColour);
    }

    @Test
    void GivenLongSequenceColoursCanBeStoredAndRetrieved() {
        InMemoryStore inMemoryStore = new InMemoryStore();
        String inputString = "00-05";
        Colour expectedColour = Colour.YELLOW;

        inMemoryStore.store(inputString, expectedColour);
        Colour actualColour1 = inMemoryStore.get("00");
        Colour actualColour3 = inMemoryStore.get("00");
        Colour actualColour5 = inMemoryStore.get("00");

        assertEquals(expectedColour, actualColour1);
        assertEquals(expectedColour, actualColour3);
        assertEquals(expectedColour, actualColour5);
    }

    @Test
    void RetrievingUnsetIndexesReturnsGrey() {
        InMemoryStore inMemoryStore = new InMemoryStore();
        String inputString = "00-05";
        Colour expectedColour = Colour.GREY;

        inMemoryStore.store(inputString, Colour.YELLOW);
        Colour actualColour = inMemoryStore.get("06");

        assertEquals(expectedColour, actualColour);
    }

    @Test
    void OverlappingSequenceReturnsHigherPriorityColour() {
        InMemoryStore inMemoryStore = new InMemoryStore();
        String lowPriorityColourRange = "00-05";
        Colour lowPriorityColour = Colour.BLUE;

        String highPriorityColourRange = "01-06";
        Colour highPriorityColour = Colour.YELLOW;


        inMemoryStore.store(lowPriorityColourRange, lowPriorityColour);
        inMemoryStore.store(highPriorityColourRange, highPriorityColour);


        assertEquals(lowPriorityColour, inMemoryStore.get("00"));
        assertEquals(highPriorityColour, inMemoryStore.get("01"));
        assertEquals(highPriorityColour, inMemoryStore.get("05"));
        assertEquals(highPriorityColour, inMemoryStore.get("06"));
    }

    @Test
    void InternallyOverlappingSequenceReturnsHigherPriorityColour() {
        InMemoryStore inMemoryStore = new InMemoryStore();
        String lowPriorityColourRange = "00-10";
        Colour lowPriorityColour = Colour.BLUE;

        String highPriorityColourRange = "01-05";
        Colour highPriorityColour = Colour.YELLOW;


        inMemoryStore.store(lowPriorityColourRange, lowPriorityColour);
        inMemoryStore.store(highPriorityColourRange, highPriorityColour);


        assertEquals(lowPriorityColour, inMemoryStore.get("00"));
        assertEquals(highPriorityColour, inMemoryStore.get("01"));
        assertEquals(highPriorityColour, inMemoryStore.get("05"));
        assertEquals(lowPriorityColour, inMemoryStore.get("06"));
        assertEquals(lowPriorityColour, inMemoryStore.get("10"));
    }

    //endregion
    //region Tests for failure/exception conditions
    @Test
    void StoreWithSingleIndexRangeThrows() {
        InMemoryStore inMemoryStore = new InMemoryStore();
        String invalidRange = "32";

        assertThrows(IllegalArgumentException.class, () -> inMemoryStore.store(invalidRange, Colour.YELLOW));
    }

    @Test
    void StoreWithNonIntegerStringThrows() {
        InMemoryStore inMemoryStore = new InMemoryStore();
        String invalidRange = "05-ten";

        assertThrows(NumberFormatException.class, () -> inMemoryStore.store(invalidRange, Colour.YELLOW));
    }

    @Test
    void StoreWithHigherStartIndexThrows() {
        InMemoryStore inMemoryStore = new InMemoryStore();
        String invalidRange = "10-01";

        assertThrows(IllegalArgumentException.class, () -> inMemoryStore.store(invalidRange, Colour.YELLOW));
    }

    @Test
    void StoreWithOutOfBoundsHighIndexThrows() {
        InMemoryStore inMemoryStore = new InMemoryStore();
        // with a size of 100, highest index is 99
        String invalidRange = "00-100";

        assertThrows(IndexOutOfBoundsException.class, () -> inMemoryStore.store(invalidRange, Colour.YELLOW));
    }

    @Test
    void StoreWithNegativeIndexThrows() {
        InMemoryStore inMemoryStore = new InMemoryStore();
        String invalidRange = "-01-100";

        assertThrows(IllegalArgumentException.class, () -> inMemoryStore.store(invalidRange, Colour.YELLOW));
    }

    //endregion

    /**
     * As a final affirmation of valid implementation, I've taken the "Further Examples" section of the challenge and
     * converted them into assertions/actions.
     */
    @Test
    void CodingChallengeExamplesProof() {
        InMemoryStore inMemoryStore = new InMemoryStore();
        /*
        store("34-78", RED)
        store("31-41", YELLOW)
        store("64-98", GREEN)

        get("31") => YELLOW
        get("39") => YELLOW
        get("50") => RED
        get("68") => RED
        get("91") => GREEN
        get("99") => GREY

        store("90-99", BLUE)

        get("91") => GREEN
        get("99") => BLUE
         */

        inMemoryStore.store("34-78", Colour.RED);
        inMemoryStore.store("31-41", Colour.YELLOW);
        inMemoryStore.store("64-98", Colour.GREEN);

        assertEquals(Colour.YELLOW, inMemoryStore.get("31"));
        assertEquals(Colour.YELLOW, inMemoryStore.get("39"));
        assertEquals(Colour.RED, inMemoryStore.get("50"));
        assertEquals(Colour.RED, inMemoryStore.get("68"));
        assertEquals(Colour.GREEN, inMemoryStore.get("91"));
        assertEquals(Colour.GREY, inMemoryStore.get("99"));

        inMemoryStore.store("90-99", Colour.BLUE);

        assertEquals(Colour.GREEN, inMemoryStore.get("91"));
        assertEquals(Colour.BLUE, inMemoryStore.get("99"));
    }
}
