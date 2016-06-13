package com.product.bundle.recommender.services;

import com.product.bundle.recommender.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class QuestionsServiceImplTest {

    @Autowired
    private QuestionsService questionsService;
    
    @Test
    public void testGetQuestions(){
        final List<String> expectedAge =
                Collections.unmodifiableList(new ArrayList<String>(Arrays.asList("0-17", "18-64", "65+")));
        final List<String> expectedStudent =
                Collections.unmodifiableList(new ArrayList<String>(Arrays.asList("Yes", "No")));
        final List<String> expectedIncome =
                Collections.unmodifiableList(new ArrayList<String>(Arrays.asList("0", "1-12000", "12001-40000", "40001+")));
        assertThat("All age values must be equal!", 
                this.questionsService.getQuestions().getAge(), is(expectedAge));
        assertThat("All student values must be equal!", 
                this.questionsService.getQuestions().getStudent(), is(expectedStudent));
        assertThat("All income values must be equal!", 
                this.questionsService.getQuestions().getIncome(), is(expectedIncome));
    }
}
