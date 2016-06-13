package com.product.bundle.recommender.repository;

import com.product.bundle.recommender.domain.Bundle;
import com.product.bundle.recommender.domain.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;

/**
 * Mocked data.
 */
@Repository
public class RepositoryMock {
    /**
     * This attribute stores available bundles and their products.
     * Alternatively it could also retrieve data from a database.
     */
    private final List<Bundle> bundles;
    /**
     * This attribute stores available products.
     * Alternatively it could also retrieve data from a database.
     */
    private final Map<PRODUCT_TYPE, List<Product>> products;
    @Autowired
    private Questions questions;

    public RepositoryMock() {
        //Products
        final Product currentAccount = new Product("Current Account");
        final Product currentAccountPlus = new Product("Current Account Plus");
        final Product juniorSaverAccount = new Product("Junior Saver Account");
        final Product studentAccount = new Product("Student Account");
        final Product pensionerAccount = new Product("Pensioner Account");
        final Product debitCard = new Product("Debit Card");
        final Product creditCard = new Product("Credit Card");
        final Product goldCreditCard = new Product("Gold Credit Card");

        //Bundles
        final Bundle juniorSaver = new Bundle("Junior Saver",
                new ArrayList<Product>(Arrays.asList(juniorSaverAccount)), 0);
        final Bundle student = new Bundle("Student",
                new ArrayList<Product>(Arrays.asList(studentAccount, debitCard, creditCard)), 0);
        final Bundle classic = new Bundle("Classic",
                new ArrayList<Product>(Arrays.asList(currentAccount, debitCard)), 1);
        final Bundle classicPlus = new Bundle("Classic Plus",
                new ArrayList<Product>(Arrays.asList(currentAccount, debitCard, creditCard)), 2);
        final Bundle gold = new Bundle("Gold",
                new ArrayList<Product>(Arrays.asList(currentAccountPlus, debitCard, goldCreditCard)), 3);

        final List<Product> accounts = Collections.unmodifiableList(new ArrayList<>(Arrays.asList(
                currentAccount,
                currentAccountPlus,
                juniorSaverAccount,
                studentAccount,
                pensionerAccount
        )));

        final List<Product> cards = Collections.unmodifiableList(new ArrayList<>(Arrays.asList(
                debitCard,
                creditCard,
                goldCreditCard
        )));

        this.products = Collections.unmodifiableMap(new HashMap<PRODUCT_TYPE, List<Product>>() {{
            put(PRODUCT_TYPE.ACCOUNT, accounts);
            put(PRODUCT_TYPE.CARD, cards);
        }});

        this.bundles = Collections.unmodifiableList(new ArrayList<>(Arrays.asList(
                juniorSaver,
                student,
                classic,
                classicPlus,
                gold
        )));

    }

    public Questions getQuestions() {
        return questions;
    }

    public List<Bundle> getBundles() {
        return bundles;
    }

    public Map<PRODUCT_TYPE, List<Product>> getProducts() {
        return products;
    }

    /**
     * Marker for different product types.
     */
    public enum PRODUCT_TYPE {
        ACCOUNT, CARD;
    }
}
