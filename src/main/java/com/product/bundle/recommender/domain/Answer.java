package com.product.bundle.recommender.domain;

import javax.validation.constraints.NotNull;

public class Answer {
    
    @NotNull
    private String age;
    
    @NotNull
    private String student;

    @NotNull
    private String income;

    public Answer(String age, String student, String income) {
        this.age = age;
        this.student = student;
        this.income = income;
    }

    public Answer() {
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getStudent() {
        return student;
    }

    public void setStudent(String student) {
        this.student = student;
    }

    public String getIncome() {
        return income;
    }

    public void setIncome(String income) {
        this.income = income;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Answer answer = (Answer) o;

        if (age != null ? !age.equals(answer.age) : answer.age != null) return false;
        if (student != null ? !student.equals(answer.student) : answer.student != null) return false;
        return income != null ? income.equals(answer.income) : answer.income == null;

    }

    @Override
    public int hashCode() {
        int result = age != null ? age.hashCode() : 0;
        result = 31 * result + (student != null ? student.hashCode() : 0);
        result = 31 * result + (income != null ? income.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Answer{" +
                "age='" + age + '\'' +
                ", student='" + student + '\'' +
                ", income='" + income + '\'' +
                '}';
    }
}
