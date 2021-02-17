package space.tsig.quizgame;

import java.util.ArrayList;

public class QLevel2 {
    public String question;
    public String answers;
    public Integer answerKey;

    public QLevel2(){

    }
    public QLevel2( Integer answerKey, String answers, String question) {
        this.answerKey = answerKey;
        this.answers = answers;
        this.question = question;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswers() {
        return answers;
    }

    public void setAnswers(String answers) {
        this.answers = answers;
    }

    public Integer getAnswersKey() {
        return answerKey;
    }

    public void setAnswersKey(Integer answerKey) {
        this.answerKey = answerKey;
    }
}
