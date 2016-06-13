package com.product.bundle.recommender.domain.wrappers;


import com.product.bundle.recommender.domain.Answer;
import com.product.bundle.recommender.domain.Bundle;

import javax.validation.constraints.NotNull;

public class BundleAndAnswerWrapper {
    
    @NotNull
    private Answer answer;
    
    @NotNull
    private Bundle bundle;

    public BundleAndAnswerWrapper(Answer answer, Bundle bundle) {
        this.answer = answer;
        this.bundle = bundle;
    }

    public BundleAndAnswerWrapper() {
    }

    public Answer getAnswer() {
        return answer;
    }

    public void setAnswer(Answer answer) {
        this.answer = answer;
    }

    public Bundle getBundle() {
        return bundle;
    }

    public void setBundle(Bundle bundle) {
        this.bundle = bundle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BundleAndAnswerWrapper that = (BundleAndAnswerWrapper) o;

        if (answer != null ? !answer.equals(that.answer) : that.answer != null) return false;
        return bundle != null ? bundle.equals(that.bundle) : that.bundle == null;

    }

    @Override
    public int hashCode() {
        int result = answer != null ? answer.hashCode() : 0;
        result = 31 * result + (bundle != null ? bundle.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "BundleAndAnswerWrapper{" +
                "answer=" + answer +
                ", bundle=" + bundle +
                '}';
    }
}
