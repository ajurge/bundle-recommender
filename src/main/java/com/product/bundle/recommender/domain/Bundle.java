package com.product.bundle.recommender.domain;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

public class Bundle {
    
    @NotNull
    private String bundle;
    
    private List<Product> products;
    
    @NotNull
    @Min(0)
    @Max(3)
    private Integer value;

    public Bundle(String bundle, List<Product> products, Integer value) {
        this.bundle = bundle;
        this.products = products;
        this.value = value;
    }

    public Bundle() {
    }

    public String getBundle() {
        return bundle;
    }

    public void setBundle(String bundle) {
        this.bundle = bundle;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Bundle bundle1 = (Bundle) o;

        if (bundle != null ? !bundle.equals(bundle1.bundle) : bundle1.bundle != null) return false;
        if (products != null ? !products.equals(bundle1.products) : bundle1.products != null) return false;
        return value != null ? value.equals(bundle1.value) : bundle1.value == null;

    }

    @Override
    public int hashCode() {
        int result = bundle != null ? bundle.hashCode() : 0;
        result = 31 * result + (products != null ? products.hashCode() : 0);
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Bundle{" +
                "bundle='" + bundle + '\'' +
                ", products=" + products +
                ", value=" + value +
                '}';
    }
}
