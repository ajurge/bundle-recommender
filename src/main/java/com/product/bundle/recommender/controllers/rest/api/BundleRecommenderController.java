package com.product.bundle.recommender.controllers.rest.api;

import com.product.bundle.recommender.domain.Answer;
import com.product.bundle.recommender.domain.Bundle;
import com.product.bundle.recommender.domain.wrappers.BundleAndAnswerWrapper;
import com.product.bundle.recommender.errors.ErrorMessage;
import com.product.bundle.recommender.exceptions.BundleRecommendationNotFoundException;
import com.product.bundle.recommender.repository.Questions;
import com.product.bundle.recommender.services.BundleRecommenderService;
import com.product.bundle.recommender.services.QuestionsService;
import com.product.bundle.recommender.validators.AnswerValidator;
import com.product.bundle.recommender.validators.BundleAndAnswerWrapperValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api")
public class BundleRecommenderController {

    @Autowired
    AnswerValidator answerValidator;

    @Autowired
    BundleAndAnswerWrapperValidator bundleAndAnswerWrapperValidator;

    @Autowired
    private QuestionsService questionsService;

    @Autowired
    private BundleRecommenderService bundleRecommenderService;

    @InitBinder("answer")
    protected void initAnswerBinder(final WebDataBinder binder) {
        binder.addValidators(this.answerValidator);
    }

    @InitBinder("bundleAndAnswerWrapper")
    protected void initBundleBinder(final WebDataBinder binder) {
        binder.addValidators(this.bundleAndAnswerWrapperValidator);
    }

    @RequestMapping(value = "/questions", method = RequestMethod.GET)
    public Questions questions() {
        return this.questionsService.getQuestions();
    }

    @RequestMapping(value = "/answer", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Bundle answer(@Valid @RequestBody Answer answer)
            throws BundleRecommendationNotFoundException {

        return this.bundleRecommenderService.findRecommendedBundle(answer);
    }

    @RequestMapping(value = "/validate", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> validate(
            @Valid @RequestBody BundleAndAnswerWrapper bundleAndAnswerWrapper,
            HttpServletRequest request,
            HttpServletResponse response) throws BundleRecommendationNotFoundException {
        ErrorMessage errorMessage = new ErrorMessage(
                HttpStatus.BAD_REQUEST.toString(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                String.format("Validation failed for bundle %s and answer %s.",
                        bundleAndAnswerWrapper.getBundle().getBundle(), bundleAndAnswerWrapper.getAnswer()),
                null,
                request.getRequestURL().toString(),
                new ArrayList<String>());

        if (!this.bundleRecommenderService.validateBundle(
                bundleAndAnswerWrapper.getAnswer(), bundleAndAnswerWrapper.getBundle(), errorMessage)) {
            return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("{\"valid\":\"true\"}", HttpStatus.OK);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    ErrorMessage handleMethodArgumentNotValidException(HttpServletRequest req, MethodArgumentNotValidException ex) {
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        List<ObjectError> globalErrors = ex.getBindingResult().getGlobalErrors();
        List<String> errors = new ArrayList<>(fieldErrors.size() + globalErrors.size());
        String error;
        for (FieldError fieldError : fieldErrors) {
            error = fieldError.getField() + ", " + fieldError.getRejectedValue() + ", " + fieldError.getCode();
            errors.add(error);
        }
        for (ObjectError objectError : globalErrors) {
            error = objectError.getObjectName() + ", " + objectError.getDefaultMessage() + ", " + objectError.getCode();
            errors.add(error);
        }
        return new ErrorMessage(
                HttpStatus.BAD_REQUEST.toString(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "Invalid request arguments",
                MethodArgumentNotValidException.class.getCanonicalName(),
                req.getRequestURL().toString(), errors);
    }

}
