package com.product.bundle.recommender.utils;


import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.HashMap;
import java.util.Map;

import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class NumberRangeParserTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void testIllegalArgumentExceptionIsThrownForInvalidNumbers() {
        String invalidNumber = "2345gh";
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage(containsString(String.format("Range value %s is not valid!", invalidNumber)));
        NumberRangeParser.validateNumberRange(invalidNumber);
    }


    @Test
    public void testValidRanges() {
        String[] validRanges = new String[]{"546235", "342345-3465734", "2352345+"};
        for (String validRange : validRanges) {
            assertTrue(String.format("Range %s must be valid", validRange), NumberRangeParser.validateNumberRange(validRange));
        }
    }

    @Test
    public void testGetUpperBound() {
        Map<String, Integer> rangesAndExpectedUpperBounds = new HashMap<String, Integer>() {{
            put("0", 0);
            put("0-20", 20);
            put("500+", Integer.MAX_VALUE);
        }};
        for (Map.Entry<String, Integer> rangeAndExpectedUpperBound : rangesAndExpectedUpperBounds.entrySet()) {
            Integer expectedUpperBound = rangeAndExpectedUpperBound.getValue();
            assertThat(String.format("Upper bound must be %s!", expectedUpperBound),
                    NumberRangeParser.getUpperBound(rangeAndExpectedUpperBound.getKey()),
                    is(expectedUpperBound));
        }
    }

    @Test
    public void testGetLowerBound() {
        Map<String, Integer> rangesAndExpectedLowerBounds = new HashMap<String, Integer>() {{
            put("0", 0);
            put("10-34", 10);
            put("356+", 356);
        }};
        for (Map.Entry<String, Integer> rangeAndExpectedLowerBound : rangesAndExpectedLowerBounds.entrySet()) {
            Integer expectedLowerBound = rangeAndExpectedLowerBound.getValue();
            assertThat(String.format("Lower bound must be %s!", expectedLowerBound),
                    NumberRangeParser.getLowerBound(rangeAndExpectedLowerBound.getKey()),
                    is(expectedLowerBound));
        }
    }
}
