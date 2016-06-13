package com.product.bundle.recommender.exceptions;

public class ProductNotFoundDAOException extends Exception {
    public ProductNotFoundDAOException() {
        super();
    }

    public ProductNotFoundDAOException(String message) {
        super(message);
    }

    public ProductNotFoundDAOException(String message,
                                       Throwable cause) {
        super(message, cause);
    }

    public ProductNotFoundDAOException(Throwable cause) {
        super(cause);
    }

    protected ProductNotFoundDAOException(String message,
                                          Throwable cause,
                                          boolean enableSuppression,
                                          boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
