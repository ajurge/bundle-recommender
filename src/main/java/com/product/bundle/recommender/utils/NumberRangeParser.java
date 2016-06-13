package com.product.bundle.recommender.utils;

import java.util.regex.Pattern;

public class NumberRangeParser {
    
    enum BoundType{
        UPPER, LOWER;
    }

    enum RangeType{
        NUMBER("^[0-9]*$") {
            @Override
            public boolean isValid(String range) {
                return Pattern.compile(this.getRegex()).matcher(range).matches();
            }
        }, 
        RANGE("^\\d+\\-\\d+$") {
            @Override
            public boolean isValid(String range) {
                return Pattern.compile(this.getRegex()).matcher(range).matches();
            }
        }, 
        NUMBER_ABOVE("^\\d+\\+$") {
            @Override
            public boolean isValid(String range) {
                return Pattern.compile(this.getRegex()).matcher(range).matches();
            }
        };

        private final String regex;

        RangeType(String regex) {
            this.regex = regex;
        }

        public String getRegex() {
            return regex;
        }

        /**
         * Visitor that each value of enum constant in the {@link RangeType} must implement and delegate
         * execution to the appropriate implementation.
         * @param range reference to the String representation of the range.
         * @return true if range is valid, false otherwise.
         */
        public abstract boolean isValid(String range);
    }

    public static final Integer getLowerBound(String range) {        
        return getBound(range, BoundType.LOWER);
    }

    public static final Integer getUpperBound(String range) {
        return getBound(range, BoundType.UPPER);
    }


    /**
     * Get specified bound from the passed in range.
     * @param range  reference to the String representation of number range.
     * @param boundType specified  bound type from {@link BoundType} 
     * @return {@link Integer} value of the bound or null.
     */
    private static final Integer getBound(String range, BoundType boundType){
        validateNumberRange(range);
        
        Integer lowerBound = null;
        Integer upperBound = null;
        
        if (RangeType.NUMBER.isValid(range)){
            lowerBound = upperBound = Integer.parseInt(range); 
        } else if (RangeType.RANGE.isValid(range)) {
            String[] bounds = range.split("-");
            lowerBound = Integer.parseInt(bounds[0]);
            upperBound = Integer.parseInt(bounds[1]);
        } else if (RangeType.NUMBER_ABOVE.isValid(range)){
            range = range.replace("+", "");
            lowerBound = Integer.parseInt(range);
            upperBound = Integer.MAX_VALUE;
        }        
        switch (boundType){
            case UPPER:
                return upperBound;
            case LOWER:
                return lowerBound;
            default:
                throw new IllegalArgumentException("Illegal bound type!");
        }        
    }


    /**
     * Validates that the range contains only digits [0-9] and has the correct format.
     *
     * @param range reference to the String representation of the range.
     * @return true if and only if the passed in range is valid.
     * @throws IllegalArgumentException if the range is not valid.
     */
    static boolean validateNumberRange(String range) {
        if (RangeType.NUMBER.isValid(range)
                || RangeType.NUMBER_ABOVE.isValid(range)
                || RangeType.RANGE.isValid(range))
            return true;
        throw new IllegalArgumentException(String.format("Range value %s is not valid!", range));
    }
}
