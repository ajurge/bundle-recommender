package com.product.bundle.recommender.exceptions;

public class BundleRecommendationNotFoundException extends Exception {
    public BundleRecommendationNotFoundException() {
        super();
    }

    public BundleRecommendationNotFoundException(String message) {
        super(message);
    }

    public BundleRecommendationNotFoundException(String message,
                                                 Throwable cause) {
        super(message, cause);
    }

    public BundleRecommendationNotFoundException(Throwable cause) {
        super(cause);
    }

    protected BundleRecommendationNotFoundException(String message,
                                                    Throwable cause,
                                                    boolean enableSuppression,
                                                    boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
