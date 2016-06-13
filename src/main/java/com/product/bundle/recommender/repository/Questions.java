package com.product.bundle.recommender.repository;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


/**
 * This class stores available questions and valid answers.
 * Alternatively it could also retrieve data from a database.
 */
@Component
public class Questions {

    private final List<String> age =
            Collections.unmodifiableList(new ArrayList<String>(Arrays.asList("0-17", "18-64", "65+")));

    private final List<String> student =
            Collections.unmodifiableList(new ArrayList<String>(Arrays.asList("Yes", "No")));

    private final List<String> income =
            Collections.unmodifiableList(new ArrayList<String>(Arrays.asList("0", "1-12000", "12001-40000", "40001+")));


    public Questions() {

    }

    public List<String> getAge() {
        return age;
    }

    public List<String> getStudent() {
        return student;
    }

    public List<String> getIncome() {
        return income;
    }
}
