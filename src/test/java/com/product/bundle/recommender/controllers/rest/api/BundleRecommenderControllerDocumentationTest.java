package com.product.bundle.recommender.controllers.rest.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.product.bundle.recommender.Application;
import com.product.bundle.recommender.domain.Answer;
import com.product.bundle.recommender.domain.Bundle;
import com.product.bundle.recommender.domain.Product;
import com.product.bundle.recommender.domain.wrappers.BundleAndAnswerWrapper;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class BundleRecommenderControllerDocumentationTest {

    @Rule
    public final JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("target/generated-snippets");

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
                .apply(documentationConfiguration(this.restDocumentation)).build();
    }

    @Test
    public void getQuestions() throws Exception {
        this.mockMvc.perform(get("/api/questions")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("age[0]", is("0-17")))
                .andExpect(jsonPath("age[1]", is("18-64")))
                .andExpect(jsonPath("age[2]", is("65+")))
                .andExpect(jsonPath("student[0]", is("Yes")))
                .andExpect(jsonPath("student[1]", is("No")))
                .andExpect(jsonPath("income[0]", is("0")))
                .andExpect(jsonPath("income[1]", is("1-12000")))
                .andExpect(jsonPath("income[2]", is("12001-40000")))
                .andExpect(jsonPath("income[3]", is("40001+")))
                .andDo(print())
                .andDo(document("get-questions",
                        responseFields(
                                fieldWithPath("age").description("All valid values."),
                                fieldWithPath("student").description("All valid values."),
                                fieldWithPath("income").description("All valid values."))
                ));
    }

    @Test
    public void postAnswer() throws Exception {
        final Map<String, String> answers = new HashMap<String, String>() {{
            put("age", "18-64");
            put("student", "No");
            put("income", "12001-40000");
        }};

        this.mockMvc.perform(post("/api/answer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(answers))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("bundle", is("Classic Plus")))
                .andExpect(jsonPath("products[0].product", is("Current Account")))
                .andExpect(jsonPath("products[1].product", is("Debit Card")))
                .andExpect(jsonPath("products[2].product", is("Credit Card")))
                .andExpect(jsonPath("value", is(2)))
                .andDo(print())
                .andDo(document("post-answer",
                        requestFields(
                                fieldWithPath("age").description("Selected value."),
                                fieldWithPath("student").description("Selected value."),
                                fieldWithPath("income").description("Selected value.")),
                        responseFields(
                                fieldWithPath("bundle").description("Recommended bundle."),
                                fieldWithPath("products").description("Included products."),
                                fieldWithPath("value").description("Bundle value."))
                ));
    }

    @Test
    public void postValidate() throws Exception {
        final Bundle bundle = new Bundle("Gold", new ArrayList<Product>(Arrays.asList(
                new Product("Current Account Plus"), new Product("Debit Card"), new Product("Gold Credit Card"))), 3);
        final Answer answer = new Answer("18-64", "No", "40001+");

        this.mockMvc.perform(post("/api/validate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(new BundleAndAnswerWrapper(answer, bundle)))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("valid", is("true")))
                .andDo(print())
                .andDo(document("post-validate",
                        requestFields(
                                fieldWithPath("answer").description("Answer object."),
                                fieldWithPath("answer.age").description("Selected age."),
                                fieldWithPath("answer.student").description("Selected value."),
                                fieldWithPath("answer.income").description("Selected value."),
                                fieldWithPath("bundle").description("Bundle object."),
                                fieldWithPath("bundle.bundle").description("Bundle name."),
                                fieldWithPath("bundle.products").description("List of selected products."),
                                fieldWithPath("bundle.products[0].product").description("Selected product."),
                                fieldWithPath("bundle.value").description("Bundle value.")),
                        responseFields(
                                fieldWithPath("valid").description("Should be equal to true."))
                ));
    }

    @Test
    public void testPostInvalidAnswerThrowsValidationException() throws Exception {
        final Map<String, String> answers = new HashMap<String, String>() {{
            put("age", "246");
            put("student", "sdghsrth");
            put("income", "56787");
        }};

        this.mockMvc.perform(post("/api/answer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(answers))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status", is("400")))
                .andExpect(jsonPath("error", is("Bad Request")))
                .andExpect(jsonPath("exception", is("org.springframework.web.bind.MethodArgumentNotValidException")))
                .andExpect(jsonPath("message", is("Invalid request arguments")))
                .andExpect(jsonPath("path", is("http://localhost:8080/api/answer")))
                .andExpect(jsonPath("errors[0]", is("age, 246, age.value.not.valid")))
                .andExpect(jsonPath("errors[1]", is("student, sdghsrth, student.value.not.valid")))
                .andExpect(jsonPath("errors[2]", is("income, 56787, income.value.not.valid")))
                .andDo(print())
                .andDo(document("validation-error-example",
                        responseFields(
                                fieldWithPath("error").description("The HTTP error that occurred, e.g. `Bad Request`"),
                                fieldWithPath("message").description("A description of the cause of the error"),
                                fieldWithPath("path").description("The path to which the request was made"),
                                fieldWithPath("status").description("The HTTP status code, e.g. `400`"),
                                fieldWithPath("exception").description("Exception name"),
                                fieldWithPath("timestamp").description("The time, in milliseconds, at which the error occurred"),
                                fieldWithPath("errors").description("Validation errors, optional"))));
    }
}
