package com.product.bundle.recommender.services;

import com.product.bundle.recommender.domain.Answer;
import com.product.bundle.recommender.domain.Bundle;
import com.product.bundle.recommender.errors.ErrorMessage;
import com.product.bundle.recommender.exceptions.BundleRecommendationNotFoundException;

/**
 * Service responsible for finding recommended bundle and validating bundles.
 */
public interface BundleRecommenderService {
    /**
     * Find recommended {@link Bundle} based on the passed in {@link Answer}.
     *
     * @param answer reference to {@link Answer} to be used for recommending a {@link Bundle}.
     * @return a reference to the recommended {@link Bundle}.
     * @throws BundleRecommendationNotFoundException if bundle recommendation could not be found.
     */
    Bundle findRecommendedBundle(Answer answer) throws BundleRecommendationNotFoundException;


    /**
     * Validate the passed in {@link Bundle} using available business rules and specified {@link Answer}.
     *
     * @param answer       reference to the {@link Answer} to be used to fetch all possible valid bundles.
     * @param bundle       reference to the {@link Bundle} to be validated.
     * @param errorMessage reference to the {@link ErrorMessage} to add validation errors.
     * @return tre if the {@link Bundle} is valid, false otherwise.
     * @throws BundleRecommendationNotFoundException if bundle recommendation could not be found.
     */
    boolean validateBundle(Answer answer,
                           Bundle bundle,
                           ErrorMessage errorMessage) throws BundleRecommendationNotFoundException;
}
