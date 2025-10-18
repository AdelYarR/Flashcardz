package ru.itis.example.models;

public class TrainingSessionCard {
    private Long id;
    private String session_id;
    private Long card_id;
    private Integer card_order;

    public TrainingSessionCard(Long id, String session_id, Long card_id, Integer card_order) {
        this.id = id;
        this.session_id = session_id;
        this.card_id = card_id;
        this.card_order = card_order;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSession_id() {
        return session_id;
    }

    public void setSession_id(String session_id) {
        this.session_id = session_id;
    }

    public Long getCard_id() {
        return card_id;
    }

    public void setCard_id(Long card_id) {
        this.card_id = card_id;
    }

    public Integer getCard_order() {
        return card_order;
    }

    public void setCard_order(Integer card_order) {
        this.card_order = card_order;
    }
}
