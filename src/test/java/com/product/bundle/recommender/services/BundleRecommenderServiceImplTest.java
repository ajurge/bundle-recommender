package com.product.bundle.recommender.services;

import com.product.bundle.recommender.Application;
import com.product.bundle.recommender.dao.BundlesDAO;
import com.product.bundle.recommender.domain.Answer;
import com.product.bundle.recommender.domain.Bundle;
import com.product.bundle.recommender.domain.Product;
import com.product.bundle.recommender.errors.ErrorMessage;
import com.product.bundle.recommender.exceptions.BundleNotFoundDAOException;
import com.product.bundle.recommender.exceptions.BundleRecommendationNotFoundException;
import com.product.bundle.recommender.exceptions.ProductNotFoundDAOException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class BundleRecommenderServiceImplTest {

    @Autowired
    BundleRecommenderService bundleRecommenderService;

    @Autowired
    BundlesDAO bundlesDAO;

    @Test
    public void testFilterBundleCards() throws ProductNotFoundDAOException, BundleNotFoundDAOException {
        final Answer answerStudent = new Answer("18-64", "Yes", "1-12000");
        final Answer answerGold = new Answer("18-64", "No", "40001+");
        Bundle actualBundleStudent = bundlesDAO.getBundleByName("Student");
        Bundle actualBundleGold = bundlesDAO.getBundleByName("Gold");

        final Bundle expectedBundleStudent = new Bundle(
                "Student",
                new ArrayList<>(Arrays.asList(
                        new Product("Student Account"),
                        new Product("Debit Card"))),
                0);
        final Bundle expectedBundleGold = new Bundle(
                "Gold",
                new ArrayList<>(Arrays.asList(
                        new Product("Current Account Plus"),
                        new Product("Debit Card"),
                        new Product("Gold Credit Card"))),
                3);


        ((BundleRecommenderServiceImpl) bundleRecommenderService).filterBundleCards(answerStudent, actualBundleStudent);
        assertThat("Bundles must be equal!", actualBundleStudent, is(expectedBundleStudent));
        ((BundleRecommenderServiceImpl) bundleRecommenderService).filterBundleCards(answerGold, actualBundleGold);
        assertThat("Bundles must be equal!", actualBundleGold, is(expectedBundleGold));
    }

    @Test
    public void testIsBundleAccountValid() throws BundleNotFoundDAOException, ProductNotFoundDAOException {
        final Answer answerStudent = new Answer("18-64", "Yes", "1-12000");
        Bundle actualBundleStudent = bundlesDAO.getBundleByName("Student");
        final Answer answerJunior = new Answer("0-17", "No", "0");
        Bundle actualBundleClassic = bundlesDAO.getBundleByName("Classic");
        assertTrue("Bundle account must be valid!",
                ((BundleRecommenderServiceImpl) bundleRecommenderService).isBundleAccountValid(
                        answerStudent, actualBundleStudent));
        assertFalse("Bundle account must be invalid!", 
                ((BundleRecommenderServiceImpl) bundleRecommenderService).isBundleAccountValid(
                        answerJunior, actualBundleClassic));
        ;
    }
    
    @Test
    public void testGetAllBundles() throws BundleNotFoundDAOException, ProductNotFoundDAOException {
        //Student Saver test
        final Answer answerStudent = new Answer("18-64", "Yes", "1-12000");
        final List<Bundle> expectedBundlesStudent = new ArrayList<>(Arrays.asList(
                bundlesDAO.getBundleByName("Student"), bundlesDAO.getBundleByName("Classic")));
        assertThat("Bundles must be equal!", 
                ((BundleRecommenderServiceImpl) bundleRecommenderService).getAllBundles(answerStudent), 
                is(expectedBundlesStudent));
        //Junior Saver test
        final Answer answerJunior = new Answer("0-17", "Yes", "1-12000");
        final List<Bundle> expectedBundlesJunior = new ArrayList<>(Arrays.asList(
                bundlesDAO.getBundleByName("Junior Saver")));
        assertThat("Bundles must be equal!",
                ((BundleRecommenderServiceImpl) bundleRecommenderService).getAllBundles(answerJunior),
                is(expectedBundlesJunior));
    }
    
    
    @Test
    public void testFindRecommendedBundle() throws BundleRecommendationNotFoundException, BundleNotFoundDAOException {
        //Expected Classic bundle
        final Answer answerStudentClassic = new Answer("18-64", "Yes", "1-12000");
        final Bundle expectedBundleClassic = bundlesDAO.getBundleByName("Classic");
        assertThat("Bundles must be equal!",
                bundleRecommenderService.findRecommendedBundle(answerStudentClassic),
                is(expectedBundleClassic));
        //Expected Gold bundle
        final Answer answerStudentGold = new Answer("18-64", "Yes", "40001+");
        final Bundle expectedBundleGold = bundlesDAO.getBundleByName("Gold");
        assertThat("Bundles must be equal!",
                bundleRecommenderService.findRecommendedBundle(answerStudentGold),
                is(expectedBundleGold));
    }
    
    @Test
    public void testValidateBundle() throws BundleRecommendationNotFoundException {
        //Invalid test
        final Answer answerGold = new Answer("18-64", "Yes", "40001+");
        final Bundle invalidBundleGold = new Bundle(
                "Gold",
                new ArrayList<>(Arrays.asList(
                        new Product("Student Account"),
                        new Product("Current Account Plus"),
                        new Product("Debit Card"),
                        new Product("Gold Credit Card"))),
                3);

        assertFalse("Bundle must be invalid!",
                bundleRecommenderService.validateBundle(answerGold, invalidBundleGold, new ErrorMessage(
                        "", "", "", "", "", new ArrayList<String>())));
        //Valid test
        final Answer answerStudent = new Answer("18-64", "Yes", "0");
        final Bundle validBundleStudent = new Bundle(
                "Student",
                new ArrayList<>(Arrays.asList(
                        new Product("Student Account"),
                        new Product("Debit Card"))),
                0);
        assertTrue("Bundle must be valid!",
                bundleRecommenderService.validateBundle(answerStudent, validBundleStudent, new ErrorMessage(
                        "", "", "", "", "", new ArrayList<String>())));
    }
    
    @Test
    public void testValidateBundleOverloaded(){
        //Invalid test
        final Bundle invalidBundleGold = new Bundle(
                "Gold",
                new ArrayList<>(Arrays.asList(
                        new Product("Student Account"),
                        new Product("Current Account Plus"),
                        new Product("Debit Card"),
                        new Product("Gold Credit Card"))),
                3);

        assertFalse("Bundle must be invalid!",
                ((BundleRecommenderServiceImpl) bundleRecommenderService).validateBundle(
                        invalidBundleGold, 
                        bundlesDAO.getBundles(), 
                        new ErrorMessage("", "", "", "", "", new ArrayList<String>())));
        
        
        //Valid test
        final Bundle validBundleJunior = new Bundle(
                "Junior Saver", 
                new ArrayList<>(Arrays.asList(new Product("Junior Saver Account"))),
                0);

        assertTrue("Bundle must be valid!",
                ((BundleRecommenderServiceImpl) bundleRecommenderService).validateBundle(
                        validBundleJunior,
                        bundlesDAO.getBundles(),
                        new ErrorMessage("", "", "", "", "", new ArrayList<String>())));        
    }
}
