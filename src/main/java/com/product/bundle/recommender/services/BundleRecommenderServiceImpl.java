package com.product.bundle.recommender.services;


import com.product.bundle.recommender.comparator.BundleValueComparator;
import com.product.bundle.recommender.dao.BundlesDAO;
import com.product.bundle.recommender.dao.ProductsDAO;
import com.product.bundle.recommender.domain.Answer;
import com.product.bundle.recommender.domain.Bundle;
import com.product.bundle.recommender.domain.Product;
import com.product.bundle.recommender.errors.ErrorMessage;
import com.product.bundle.recommender.exceptions.BundleNotFoundDAOException;
import com.product.bundle.recommender.exceptions.BundleRecommendationNotFoundException;
import com.product.bundle.recommender.exceptions.ProductNotFoundDAOException;
import com.product.bundle.recommender.utils.NumberRangeParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class BundleRecommenderServiceImpl implements BundleRecommenderService {

    @Autowired
    private ProductsDAO productsDAO;

    @Autowired
    private BundlesDAO bundlesDAO;

    @Override
    public Bundle findRecommendedBundle(Answer answer) throws BundleRecommendationNotFoundException {
        List<Bundle> eligibleBundles = null;
        try {
            eligibleBundles = getAllBundles(answer);
        } catch (BundleNotFoundDAOException e) {
            throw new BundleRecommendationNotFoundException(e.getMessage());
        } catch (ProductNotFoundDAOException e) {
            throw new BundleRecommendationNotFoundException(e.getMessage());
        }
        if (eligibleBundles == null)
            throw new BundleRecommendationNotFoundException("Customer is not eligible for any of the available bundles");
        //Sort by value and return the one with the highest value!
        Collections.sort(eligibleBundles, new BundleValueComparator());
        return eligibleBundles.get(eligibleBundles.size() - 1);
    }

    @Override
    public boolean validateBundle(Answer answer,
                                  Bundle bundle,
                                  ErrorMessage errorMessage) throws BundleRecommendationNotFoundException {
        List<Bundle> eligibleBundles = null;
        try {
            eligibleBundles = getAllBundles(answer);
        } catch (BundleNotFoundDAOException e) {
            throw new BundleRecommendationNotFoundException(e.getMessage());
        } catch (ProductNotFoundDAOException e) {
            throw new BundleRecommendationNotFoundException(e.getMessage());
        }
        if (eligibleBundles == null || eligibleBundles.isEmpty()) {
            errorMessage.getErrors().add(String.format("For answer %s no eligible bundles exist!", answer));
            return false;
        }
        //Check if passed in bundle is valid        
        return validateBundle(bundle, eligibleBundles, errorMessage);
    }

    /**
     * Get all the bundles that match the business rules based on the given answers in {@link Answer}.
     *
     * @param answer reference to {@link Answer} which will be used to interpret the rules.
     * @return list of bundles.
     * @throws BundleNotFoundDAOException  if bundle was not found.
     * @throws ProductNotFoundDAOException if required products cannot be fetched.
     */
    final List<Bundle> getAllBundles(Answer answer) throws BundleNotFoundDAOException, ProductNotFoundDAOException {
        List<Bundle> bundles = new ArrayList<>();

        if (NumberRangeParser.getUpperBound(answer.getAge()) < 18) {
            Bundle juniorBundle = this.bundlesDAO.getBundleByName("Junior Saver");
            if (isBundleAccountValid(answer, juniorBundle)) {
                bundles.add(filterBundleCards(answer, juniorBundle));
            }
        }
        if ((NumberRangeParser.getLowerBound(answer.getAge()) > 17)
                && (isStudent(answer))) {
            Bundle studentBundle = this.bundlesDAO.getBundleByName("Student");
            if (isBundleAccountValid(answer, studentBundle)) {
                bundles.add(filterBundleCards(answer, studentBundle));
            }
        }
        if ((NumberRangeParser.getLowerBound(answer.getAge()) > 17)
                && (NumberRangeParser.getLowerBound(answer.getIncome()) > 0)) {
            Bundle classicBundle = this.bundlesDAO.getBundleByName("Classic");
            if (isBundleAccountValid(answer, classicBundle)) {
                bundles.add(filterBundleCards(answer, classicBundle));
            }
        }
        if ((NumberRangeParser.getLowerBound(answer.getAge()) > 17)
                && (NumberRangeParser.getLowerBound(answer.getIncome()) > 12000)) {
            Bundle classicPlusBundle = this.bundlesDAO.getBundleByName("Classic Plus");
            if (isBundleAccountValid(answer, classicPlusBundle)) {
                bundles.add(filterBundleCards(answer, classicPlusBundle));
            }
        }
        if ((NumberRangeParser.getLowerBound(answer.getAge()) > 17)
                && (NumberRangeParser.getLowerBound(answer.getIncome()) > 40000)) {
            Bundle goldBundle = this.bundlesDAO.getBundleByName("Gold");
            if (isBundleAccountValid(answer, goldBundle)) {
                bundles.add(filterBundleCards(answer, goldBundle));
            }
        }
        if (bundles.isEmpty())
            throw new BundleNotFoundDAOException("Customer is not eligible for any of the available bundles");
        return bundles;
    }


    /**
     * Check if the account in the bundle is valid according to the business rules and the passed in {@link Answer}
     *
     * @param answer reference to {@link Answer} which will be used to interpret the rules.
     * @param bundle reference to {@link Bundle} to be checked.
     * @return true if all rules are matched and the account in the bundle is valid for the passed in answer,
     * false otherwise.
     * @throws ProductNotFoundDAOException if required products cannot be fetched.
     */
    final boolean isBundleAccountValid(Answer answer, Bundle bundle) throws ProductNotFoundDAOException {
        List<Product> eligibleAccounts = new ArrayList<>();
        if (NumberRangeParser.getUpperBound(answer.getAge()) < 18)
            eligibleAccounts.add(this.productsDAO.getProductByName("Junior Saver Account"));
        if ((NumberRangeParser.getLowerBound(answer.getAge()) > 17)
                && (isStudent(answer)))
            eligibleAccounts.add(this.productsDAO.getProductByName("Student Account"));
        if ((NumberRangeParser.getLowerBound(answer.getAge()) > 17)
                && (NumberRangeParser.getLowerBound(answer.getIncome()) > 0))
            eligibleAccounts.add(this.productsDAO.getProductByName("Current Account"));
        if ((NumberRangeParser.getLowerBound(answer.getAge()) > 17)
                && (NumberRangeParser.getLowerBound(answer.getIncome()) > 40000))
            eligibleAccounts.add(this.productsDAO.getProductByName("Current Account Plus"));

        if (eligibleAccounts.isEmpty())
            throw new ProductNotFoundDAOException("Customer is not eligible for any of the available accounts!");
        eligibleAccounts.retainAll(bundle.getProducts());
        return !eligibleAccounts.isEmpty();
    }


    /**
     * Remove cards that do not match the business rules from the passed in bundle {@link Bundle} .
     *
     * @param answer reference to {@link Answer} which will be used to interpret the rules.
     * @param bundle reference to {@link Bundle} to be filtered.
     * @return filtered {@link Bundle}
     * @throws ProductNotFoundDAOException if required products cannot be fetched.
     */
    final Bundle filterBundleCards(Answer answer, Bundle bundle) throws ProductNotFoundDAOException {
        List<Product> bundleCards = new ArrayList<>(bundle.getProducts());
        List<Product> eligibleCards = new ArrayList<>();
        List<Product> bundleProducts = new ArrayList<>(bundle.getProducts());

        List<Product> requiredAccountList = new ArrayList<>(Arrays.asList(
                this.productsDAO.getProductByName("Current Account"),
                this.productsDAO.getProductByName("Current Account Plus"),
                this.productsDAO.getProductByName("Student Account"),
                this.productsDAO.getProductByName("Pensioner Account")));

        for (Product requiredAccount : requiredAccountList) {
            if (bundle.getProducts().contains(requiredAccount)) {
                eligibleCards.add(this.productsDAO.getProductByName("Debit Card"));
                break;
            }
        }
        if ((NumberRangeParser.getLowerBound(answer.getAge()) > 17)
                && (NumberRangeParser.getLowerBound(answer.getIncome()) > 12000))
            eligibleCards.add(this.productsDAO.getProductByName("Credit Card"));
        if ((NumberRangeParser.getLowerBound(answer.getAge()) > 17)
                && (NumberRangeParser.getLowerBound(answer.getIncome()) > 40000))
            eligibleCards.add(productsDAO.getProductByName("Gold Credit Card"));

        //Keep only the one that the customer is eligible to
        bundleCards.retainAll(eligibleCards);
        //This will also remove the accounts, so these need to be added back
        //This ia bit of a hack, but it works;)
        //Get only bundle accounts.
        bundleProducts.retainAll(this.productsDAO.getAccounts());
        bundleProducts.addAll(bundleCards);
        //Now set the filtered products that the customer is eligible to.
        bundle.setProducts(bundleProducts);

        return bundle;
    }

    /**
     * Validate the passed in bundle against the list of eligible bundles and add errors to the {@link ErrorMessage}.
     *
     * @param bundleToValidate reference to the {@link Bundle} to be validated.
     * @param eligibleBundles  list of bundles against which the passed in bundle will be validated.
     * @param errorMessage     reference to the {@link ErrorMessage} to add validation errors.
     * @return true if bundle is valid, false otherwise.
     */
    final boolean validateBundle(Bundle bundleToValidate, List<Bundle> eligibleBundles, ErrorMessage errorMessage) {
        List<String> errors = new ArrayList<>();
        // Just in case if Spring's custom BundleAndAnswerWrapperValidator does not catch this 
        // use the bundleExists switch. 
        boolean bundleExists = false;
        for (Bundle eligibleBundle : eligibleBundles) {
            if (eligibleBundle.getBundle().equals(bundleToValidate.getBundle())) {
                bundleExists = true;
                if (!eligibleBundle.getValue().equals(bundleToValidate.getValue())) {
                    errors.add(String.format("bundle value=%s value.not.valid", bundleToValidate.getValue()));
                }
                for (Product productToValidate : bundleToValidate.getProducts()) {
                    if (!eligibleBundle.getProducts().contains(productToValidate)) {
                        errors.add(String.format("bundle product=%s product.not.valid", productToValidate.getProduct()));
                    }
                }
                break;
            }
        }
        if (!bundleExists){
            errors.add(String.format("bundle bundle=%s bundle.not.eligible for this answer.", 
                    bundleToValidate.getBundle()));            
        }
        errorMessage.getErrors().addAll(errors);
        return errors.isEmpty();

    }

    /**
     * Check if answer contains student set to true.
     *
     * @param answer reference to {@link Answer}.
     * @return true if {@link Answer} contains student set to "Yes", false otherwise.
     */
    final boolean isStudent(Answer answer) {
        return answer.getStudent().equals("Yes");
    }
}
