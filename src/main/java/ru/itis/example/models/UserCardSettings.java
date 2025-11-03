package ru.itis.example.models;

public class UserCardSettings {
    private Long id;
    private Long userId;
    private Integer veryEasySeconds;
    private Integer easySeconds;
    private Integer mediumSeconds;
    private Integer hardSeconds;

    public UserCardSettings(Long id, Long userId, Integer veryEasySeconds, Integer easySeconds, Integer mediumSeconds, Integer hardSeconds) {
        this.id = id;
        this.userId = userId;
        this.veryEasySeconds = veryEasySeconds;
        this.easySeconds = easySeconds;
        this.mediumSeconds = mediumSeconds;
        this.hardSeconds = hardSeconds;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getVeryEasySeconds() {
        return veryEasySeconds;
    }

    public void setVeryEasySeconds(Integer veryEasySeconds) {
        this.veryEasySeconds = veryEasySeconds;
    }

    public Integer getEasySeconds() {
        return easySeconds;
    }

    public void setEasySeconds(Integer easySeconds) {
        this.easySeconds = easySeconds;
    }

    public Integer getMediumSeconds() {
        return mediumSeconds;
    }

    public void setMediumSeconds(Integer mediumSeconds) {
        this.mediumSeconds = mediumSeconds;
    }

    public Integer getHardSeconds() {
        return hardSeconds;
    }

    public void setHardSeconds(Integer hardSeconds) {
        this.hardSeconds = hardSeconds;
    }
}
