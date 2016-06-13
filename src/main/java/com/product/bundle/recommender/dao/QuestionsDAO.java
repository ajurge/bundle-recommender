package com.product.bundle.recommender.dao;

import com.product.bundle.recommender.repository.Questions;
import com.product.bundle.recommender.repository.RepositoryMock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * This class retrieves available questions and valid answers from mocked {@link RepositoryMock}.
 * Alternatively it could also retrieve data from a database.
 */
@Component
public class QuestionsDAO {
    //This could a data repository instead.
    @Autowired
    RepositoryMock repositoryMock;

    public QuestionsDAO() {

    }
    
    public Questions getQuestions(){
        return repositoryMock.getQuestions();        
    }
}
