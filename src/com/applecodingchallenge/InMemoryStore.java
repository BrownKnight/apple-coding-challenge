package com.applecodingchallenge;

import java.util.Arrays;

public class InMemoryStore {
    private static final int MEMORY_STORE_SIZE = 100;

    /**
     * In memory array that stores a colour for a given index
     *
     * @implNote The challenge gives us an assumption that the possible indexes range from 00-99,
     * thus we make the assumption that only an array of length 100 is required.
     * If this assumption were not true, we would instead want to implement dynamic resizing
     * of the array when given an index higher than the current length of the array.
     */
    private Colour[] colourArray = new Colour[100];

    InMemoryStore() {
        Arrays.fill(colourArray, Colour.GREY);
    }

    /**
     * Stores the given colour in the given range. If a colour is already stored in the range, the higher priority
     * colour takes precedence
     *
     * @param range  String of the format "00-00" describing the start and end indexes to store the colour for.
     *               Is inclusive.
     * @param colour The Colour to store in the given range
     */
    public void store(String range, Colour colour) {
        // Validate the arguments for a bit
        if (range == null || range.isEmpty()) {
            System.out.println("Range cannot be null/empty");
            throw new IllegalArgumentException("Range cannot be null/empty");
        }

        // range will be a string like "00-05", so get the two indexes by splitting it by the dash
        String[] indexes = range.split("-");

        if (indexes.length < 2) {
            System.out.printf("Range must contain a start and end index, with a single dash (-) in between: Supplied range: %s %n", range);
            throw new IllegalArgumentException("Range must contain a start and end index, with a single dash (-) in between");
        }

        int startIndex;
        int endIndex;

        try {
            startIndex = Integer.parseInt(indexes[0], 10);
            endIndex = Integer.parseInt(indexes[1], 10);
        } catch (NumberFormatException e) {
            System.out.printf("The supplied start or end index in the range is invalid. Range: %s %n", range);
            throw e;
        }

        if (startIndex > endIndex) {
            System.out.printf("The supplied start index is higher than the end index. Range: %s %n", range);
            throw new IllegalArgumentException("The supplied start index is higher than the end index");
        }

        // Don't need to check endIndex < 0 due to the above startIndex > endIndex check
        if (startIndex < 0 || startIndex >= MEMORY_STORE_SIZE || endIndex >= MEMORY_STORE_SIZE) {
            System.out.printf("The supplied start or end index in the range is out of bounds. Range: %s %n", range);
            throw new IndexOutOfBoundsException(String.format("Start or End Index is out of bounds. Max index: %d", MEMORY_STORE_SIZE - 1));
        }

        // All the argument validation is done, now just store the colour

        // As its a fairly small array, not using some "magic" memory manipulation/array fill to avoid iterating over
        // the array here won't be too big of a deal. This also allows us to easily ensure colour priority
        for (int i = startIndex; i <= endIndex; i++) {
            // Compare the enum values, to ensure we only overwrite the value if it has a higher priority
            if (colourArray[i].compareTo(colour) > 0) {
                colourArray[i] = colour;
            }
        }
    }

    /**
     * Returns the colour for the given index.
     * @param indexString Index for fetch the colour for, given as a 2 character string
     * @return  Colour stored against the specified index. Colour.GREY if no colour has been stored
     */
    public Colour get(String indexString) {
        // Validate the arguments for a bit
        if (indexString == null || indexString.isEmpty()) {
            System.out.println("index cannot be null/empty");
            throw new IllegalArgumentException("index cannot be null/empty");
        }

        int index;
        try {
            index = Integer.parseInt(indexString, 10);
        } catch (NumberFormatException e) {
            System.out.printf("The supplied index in is invalid. Index: %s %n", indexString);
            throw e;
        }

        if (index < 0 || index >= MEMORY_STORE_SIZE) {
            throw new IndexOutOfBoundsException(String.format("Index is out of bounds. Max index: %d", MEMORY_STORE_SIZE - 1));
        }

        // Index arg is validated, now simply return the colour
        return colourArray[index];
    }
}
