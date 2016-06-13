package com.product.bundle.recommender.comparator;

import com.product.bundle.recommender.domain.Bundle;

import java.util.Comparator;

public class BundleValueComparator implements Comparator<Bundle> {
    @Override
    public int compare(Bundle b1, Bundle b2) {
        return b1.getValue().compareTo(b2.getValue());
    }
}
