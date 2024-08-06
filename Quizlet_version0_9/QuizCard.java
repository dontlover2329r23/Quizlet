package Quizlet_version0_9;

import java.io.Serializable;

public class QuizCard implements Serializable {
    private String question;
    private String answer;

    public QuizCard(String question, String answer) {
        this.question = question;
        this.answer = answer;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }
}
