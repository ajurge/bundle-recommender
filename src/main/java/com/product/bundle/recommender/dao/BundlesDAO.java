package com.product.bundle.recommender.dao;

import com.product.bundle.recommender.domain.Bundle;
import com.product.bundle.recommender.exceptions.BundleNotFoundDAOException;
import com.product.bundle.recommender.exceptions.BundleRecommendationNotFoundException;
import com.product.bundle.recommender.repository.RepositoryMock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 * This class retrieves available bundles and products from mocked {@link RepositoryMock}.
 * Alternatively it could also retrieve data from a database.
 */
@Component
public class BundlesDAO {
    //This could a data repository instead.
    @Autowired
    RepositoryMock repositoryMock;

    public BundlesDAO() {

    }

    public List<Bundle> getBundles() {
        return repositoryMock.getBundles();
    }

    /**
     * Find bundle by the specified name.
     * @param name bundle name to find.
     * @return reference to {@link Bundle} if found.
     * @throws BundleNotFoundDAOException if bundle is not found.
     */
    public Bundle getBundleByName(String name) throws BundleNotFoundDAOException {
        for (Bundle bundle : repositoryMock.getBundles()){
            if (bundle != null && bundle.getBundle().equals(name)) return bundle;
        }
        throw new BundleNotFoundDAOException(String.format("Bundle with name %s does not exist!", name));
        
    }
}
