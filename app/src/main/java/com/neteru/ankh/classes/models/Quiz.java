package com.neteru.ankh.classes.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@SuppressWarnings("unused")
@DatabaseTable(tableName = "quiz")
public class Quiz {
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField(columnName = "answer", canBeNull = false)
    private String answer;
    @DatabaseField(columnName = "question", canBeNull = false)
    private String question;
    @DatabaseField(columnName = "firstProposal", canBeNull = false)
    private String firstProposal;
    @DatabaseField(columnName = "secondProposal", canBeNull = false)
    private String secondProposal;
    @DatabaseField(columnName = "thirdProposal", canBeNull = false)
    private String thirdProposal;
    @DatabaseField(columnName = "fourthProposal", canBeNull = false)
    private String fourthProposal;

    public Quiz(){}

    public Quiz(String q, String a, String p1, String p2, String p3, String p4){
        answer = a;
        question = q;
        firstProposal = p1;
        secondProposal = p2;
        thirdProposal = p3;
        fourthProposal = p4;
    }

    public int getId() {
        return id;
    }

    public String getAnswer() {
        return answer;
    }

    public String getQuestion() {
        return question;
    }

    public String getFirstProposal() {
        return firstProposal;
    }

    public String getSecondProposal() {
        return secondProposal;
    }

    public String getThirdProposal() {
        return thirdProposal;
    }

    public String getFourthProposal() {
        return fourthProposal;
    }
}
