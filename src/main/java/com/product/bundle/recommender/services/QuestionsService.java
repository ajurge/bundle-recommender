package com.product.bundle.recommender.services;

import com.product.bundle.recommender.repository.Questions;

/**
 * Service for retrieving all possible {@link Questions}.
 */
public interface QuestionsService {
    /**
     * Get all possible questions that exist.
     *
     * @return reference to {@link Questions}
     */
    Questions getQuestions();
}
