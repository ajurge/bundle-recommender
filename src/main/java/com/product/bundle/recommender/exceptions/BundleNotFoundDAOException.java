package com.product.bundle.recommender.exceptions;

public class BundleNotFoundDAOException extends Exception {
    public BundleNotFoundDAOException() {
        super();
    }

    public BundleNotFoundDAOException(String message) {
        super(message);
    }

    public BundleNotFoundDAOException(String message,
                                      Throwable cause) {
        super(message, cause);
    }

    public BundleNotFoundDAOException(Throwable cause) {
        super(cause);
    }

    protected BundleNotFoundDAOException(String message,
                                         Throwable cause,
                                         boolean enableSuppression,
                                         boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
