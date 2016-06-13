package com.product.bundle.recommender.dao;


import com.product.bundle.recommender.Application;
import com.product.bundle.recommender.domain.Product;
import com.product.bundle.recommender.exceptions.ProductNotFoundDAOException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class ProductsDAOTest {
    
    @Autowired
    ProductsDAO productsDAO;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    
    @Test
    public void testGetProductByName() throws ProductNotFoundDAOException {
        assertThat(productsDAO.getProductByName("Current Account"), is(new Product("Current Account")));
        assertThat(productsDAO.getProductByName("Current Account Plus"), is(new Product("Current Account Plus")));
        assertThat(productsDAO.getProductByName("Junior Saver Account"), is(new Product("Junior Saver Account")));
        assertThat(productsDAO.getProductByName("Student Account"), is(new Product("Student Account")));
        assertThat(productsDAO.getProductByName("Debit Card"), is(new Product("Debit Card")));
        assertThat(productsDAO.getProductByName("Credit Card"), is(new Product("Credit Card")));
        assertThat(productsDAO.getProductByName("Gold Credit Card"), is(new Product("Gold Credit Card")));        
    }

    @Test
    public void testGetProductByNameThrowsException() throws ProductNotFoundDAOException {
        String productName = "Does not exist";
        expectedException.expect(ProductNotFoundDAOException.class);
        expectedException.expectMessage(containsString(String.format("Product with name %s does not exist!", productName)));
        productsDAO.getProductByName(productName);

    }
}
