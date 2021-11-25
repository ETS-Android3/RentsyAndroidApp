
package com.menlopark.rentsyuser.model.Help_QuestionAnswer;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Detail {

    @Expose
    @SerializedName("answer")
    private String answer;
    @Expose
    @SerializedName("id")
    private int id;
    @Expose
    @SerializedName("question")
    private String question;

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

}
