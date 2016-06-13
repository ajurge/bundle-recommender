package com.product.bundle.recommender.dao;


import com.product.bundle.recommender.Application;
import com.product.bundle.recommender.domain.Bundle;
import com.product.bundle.recommender.domain.Product;
import com.product.bundle.recommender.exceptions.BundleNotFoundDAOException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Arrays;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class BundlesDAOTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    @Autowired
    BundlesDAO bundlesDAO;

    @Test
    public void testGetBundleByName() throws BundleNotFoundDAOException {
        assertThat(bundlesDAO.getBundleByName("Junior Saver"),
                is(new Bundle("Junior Saver", new ArrayList<Product>(Arrays.asList(
                        new Product("Junior Saver Account"))),
                        0)));
        assertThat(bundlesDAO.getBundleByName("Student"),
                is(new Bundle("Student", new ArrayList<Product>(Arrays.asList(
                        new Product("Student Account"), new Product("Debit Card"), new Product("Credit Card"))),
                        0)));
        assertThat(bundlesDAO.getBundleByName("Classic"),
                is(new Bundle("Classic", new ArrayList<Product>(Arrays.asList(
                        new Product("Current Account"), new Product("Debit Card"))),
                        1)));
        assertThat(bundlesDAO.getBundleByName("Classic Plus"),
                is(new Bundle("Classic Plus", new ArrayList<Product>(Arrays.asList(
                        new Product("Current Account"), new Product("Debit Card"), new Product("Credit Card"))),
                        2)));
        assertThat(bundlesDAO.getBundleByName("Gold"),
                is(new Bundle("Gold", new ArrayList<Product>(Arrays.asList(
                        new Product("Current Account Plus"), new Product("Debit Card"), new Product("Gold Credit Card"))),
                        3)));
    }

    @Test
    public void testGetBundleByNameThrowsException() throws BundleNotFoundDAOException {
        String bundleName = "Does not exist";
        expectedException.expect(BundleNotFoundDAOException.class);
        expectedException.expectMessage(containsString(String.format("Bundle with name %s does not exist!", bundleName)));
        bundlesDAO.getBundleByName(bundleName);

    }
}
