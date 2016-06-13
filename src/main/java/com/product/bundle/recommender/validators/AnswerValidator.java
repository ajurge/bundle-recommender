package com.product.bundle.recommender.validators;

import com.product.bundle.recommender.dao.QuestionsDAO;
import com.product.bundle.recommender.domain.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class AnswerValidator implements Validator {

    @Autowired
    @Qualifier("basicValidator")
    private Validator basicValidator;

    @Autowired
    private QuestionsDAO questionsDAO;

    public boolean supports(Class clazz) {
        return Answer.class.equals(clazz);
    }

    public void validate(Object obj, Errors e) {
        Answer answer = (Answer) obj;

        basicValidator.validate(obj, e);
        // eventually stop if any errors
        if (e.hasErrors()) { return; }
        if (!this.questionsDAO.getQuestions().getAge().contains(answer.getAge()))
            e.rejectValue("age", "age.value.not.valid");
        if (!this.questionsDAO.getQuestions().getStudent().contains(answer.getStudent()))
            e.rejectValue("student", "student.value.not.valid");
        if (!this.questionsDAO.getQuestions().getIncome().contains(answer.getIncome()))
            e.rejectValue("income", "income.value.not.valid");
    }
}
