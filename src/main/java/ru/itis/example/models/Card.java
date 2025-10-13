package ru.itis.example.models;

public class Card {
    private Long id;
    private Long authorId;
    private Long cardGroupId;
    private String question;
    private String answer;

    public Card(Long id, Long authorId, Long cardGroupId, String question, String answer) {
        this.id = id;
        this.authorId = authorId;
        this.cardGroupId = cardGroupId;
        this.question = question;
        this.answer = answer;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public Long getCardGroupId() {
        return cardGroupId;
    }

    public void setCardGroupId(Long cardGroupId) {
        this.cardGroupId = cardGroupId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
