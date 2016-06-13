package com.product.bundle.recommender.validators;

import com.product.bundle.recommender.dao.BundlesDAO;
import com.product.bundle.recommender.dao.QuestionsDAO;
import com.product.bundle.recommender.domain.Answer;
import com.product.bundle.recommender.domain.Bundle;
import com.product.bundle.recommender.domain.wrappers.BundleAndAnswerWrapper;
import com.product.bundle.recommender.exceptions.BundleNotFoundDAOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class BundleAndAnswerWrapperValidator implements Validator {

    @Autowired
    @Qualifier("basicValidator")
    private Validator basicValidator;

    @Autowired
    private BundlesDAO bundlesDAO;
    
    @Autowired
    AnswerValidator answerValidator;

    public boolean supports(Class clazz) {
        return BundleAndAnswerWrapper.class.equals(clazz);
    }

    public void validate(Object obj, Errors e) {
        BundleAndAnswerWrapper bundleAndAnswer = (BundleAndAnswerWrapper) obj;
        Bundle bundle = bundleAndAnswer.getBundle();
        Answer answer = bundleAndAnswer.getAnswer();
        //Perform basic validation
        basicValidator.validate(bundle, e);
        basicValidator.validate(answer, e);
        // eventually stop if any errors
        if (e.hasErrors()) { return; }       
        
        //Custom validation
        answerValidator.validate(answer, e);
        try {
            this.bundlesDAO.getBundleByName(bundle.getBundle());
        } catch (BundleNotFoundDAOException e1) {
            e.rejectValue("bundle", "bundle.value.not.valid");
        }       
    }
}
