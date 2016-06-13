package com.product.bundle.recommender.dao;

import com.product.bundle.recommender.domain.Product;
import com.product.bundle.recommender.exceptions.ProductNotFoundDAOException;
import com.product.bundle.recommender.repository.RepositoryMock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;


/**
 * This class retrieves available products from mocked {@link RepositoryMock}.
 * Alternatively it could also retrieve data from a database.
 */
@Component
public class ProductsDAO {
    //This could a data repository instead.
    @Autowired
    RepositoryMock repositoryMock;

    public ProductsDAO() {

    }

    /**
     * Find product by the specified name.
     * @param name product name to find.
     * @return reference to {@link Product} if found.
     * @throws ProductNotFoundDAOException if bundle is not found.
     */
    public Product getProductByName(String name) throws ProductNotFoundDAOException {
        for (Map.Entry<RepositoryMock.PRODUCT_TYPE, List<Product>> productType: repositoryMock.getProducts().entrySet()){
            for (Product product : productType.getValue()){
                if (product != null && product.getProduct().equals(name)) return product;
            }
            
        }
        throw new ProductNotFoundDAOException(String.format("Product with name %s does not exist!", name));

    }
    
    public List<Product> getAccounts(){
        return repositoryMock.getProducts().get(RepositoryMock.PRODUCT_TYPE.ACCOUNT);
    }

    public List<Product> getCards(){
        return repositoryMock.getProducts().get(RepositoryMock.PRODUCT_TYPE.CARD);
    }
}
