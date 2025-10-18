package ru.itis.example.training.repository;

import ru.itis.example.models.TrainingSession;
import ru.itis.example.models.UserCardProgress;
import ru.itis.example.models.UserCardProgressWithSeconds;

import java.util.List;
import java.util.Optional;

public interface TrainingRepository {
    void deleteOldTrainingSession(Long userId, Long cardGroupId);
    List<UserCardProgressWithSeconds> getProgressesByUserAndCardGroupId(Long userId, Long cardGroupId);
    void addTrainingSession(TrainingSession trainingSession);
    void addTrainingSessionCard(String trainingSessionId, Long trainingCardId, Integer cardOrder);
    Optional<TrainingSession> getByTrainingSessionId(String trainingSessionId);
    List<Long> getTrainingCardIdsByTrainingSessionId(String trainingSessionId);
    boolean existsUserCardProgressByUserAndCardId(Long userId, Long cardId);
    void addUserCardProgress(UserCardProgress userCardProgress);
    void updateUserCardProgress(UserCardProgress userCardProgress);
    void updateIndexOfTrainingSession(Integer index, String trainingSessionId);
}
