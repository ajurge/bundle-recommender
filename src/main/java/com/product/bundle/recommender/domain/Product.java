package com.product.bundle.recommender.domain;

public class Product {
    
    private String product;

    public Product(String product) {
        this.product = product;
    }

    public Product() {
        
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Product product1 = (Product) o;

        return product != null ? product.equals(product1.product) : product1.product == null;

    }

    @Override
    public int hashCode() {
        return product != null ? product.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Product{" +
                "product='" + product + '\'' +
                '}';
    }
}
