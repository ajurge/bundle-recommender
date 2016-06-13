package com.product.bundle.recommender.services;

import com.product.bundle.recommender.dao.QuestionsDAO;
import com.product.bundle.recommender.repository.Questions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuestionsServiceImpl implements QuestionsService {

    @Autowired
    private QuestionsDAO questionsDAO;

    public Questions getQuestions() {
        return this.questionsDAO.getQuestions();
    }
}
