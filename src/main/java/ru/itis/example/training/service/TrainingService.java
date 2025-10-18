package ru.itis.example.training.service;

import ru.itis.example.logger.Logger;
import ru.itis.example.models.*;
import ru.itis.example.training.repository.TrainingRepository;

import java.util.*;
import java.util.stream.Collectors;

public class TrainingService {

    private final Logger logger = new Logger(this.getClass().getName());
    private final TrainingRepository trainingRepository;

    private static final String VERY_EASY = "very_easy";
    private static final String EASY = "easy";
    private static final String MEDIUM = "medium";
    private static final String HARD = "hard";

    public TrainingService(TrainingRepository trainingRepository) {
        this.trainingRepository = trainingRepository;
    }

    public void deleteOldTrainingSession(Long userId, Long cardGroupId) {
        trainingRepository.deleteOldTrainingSession(userId, cardGroupId);
    }

    public List<UserCardProgressWithSeconds> getUserCardProgresses(Long userId, Long cardGroupId) {
        return trainingRepository.getProgressesByUserAndCardGroupId(userId, cardGroupId);
    }

    public List<Long> getTrainingCardIdsByAllCardsAndProgresses(List<Card> allCards, List<UserCardProgressWithSeconds> userCardProgressWithSeconds) {
        List<Long> trainingCardIds = new ArrayList<>();
        Map<Long, Boolean> progressCardIds = userCardProgressWithSeconds.stream()
                .collect(Collectors.toMap(
                        UserCardProgressWithSeconds::getCardId,
                        UserCardProgressWithSeconds::isExpired)
                );

        for (Card card : allCards) {
            if (progressCardIds.containsKey(card.getId())) {
                if (!progressCardIds.get(card.getId())) {
                    trainingCardIds.add(card.getId());
                }
            } else {
                trainingCardIds.add(card.getId());
            }
        }

        Collections.shuffle(trainingCardIds);
        return trainingCardIds;
    }

    public void addTrainingSession(TrainingSession trainingSession) {
        trainingRepository.addTrainingSession(trainingSession);
    }

    public void addTrainingCards(String trainingSessionId, List<Long> trainingCardIds) {
        for (int i = 0; i < trainingCardIds.size(); i++) {
            long trainingCardId = trainingCardIds.get(i);
            trainingRepository.addTrainingSessionCard(trainingSessionId, trainingCardId, i);
        }
    }

    public TrainingSession getByTrainingSessionId(String trainingSessionId) {
        Optional<TrainingSession> optionalTrainingSession = trainingRepository.getByTrainingSessionId(trainingSessionId);
        if (optionalTrainingSession.isEmpty()) {
            throw new RuntimeException("failed to get training session by its id");
        }

        return optionalTrainingSession.get();
    }

    public List<Long> getTrainingCardIdsByTrainingSessionId(String trainingSessionId) {
        return trainingRepository.getTrainingCardIdsByTrainingSessionId(trainingSessionId);
    }

    public void addUserCardProgress(Long userId, Long cardId, String action) {
        long lastAccess = System.currentTimeMillis() / 1000;
        UserCardProgress userCardProgress = new UserCardProgress(
                null,
                userId,
                cardId,
                switch(action) {
                    case "very_easy" -> Difficulty.VERY_EASY;
                    case "easy" -> Difficulty.EASY;
                    case "medium" -> Difficulty.MEDIUM;
                    case "hard" -> Difficulty.HARD;
                    default -> throw new RuntimeException("unknow action while updating user card progress: " + action);
                },
                lastAccess
        );

        if (!trainingRepository.existsUserCardProgressByUserAndCardId(userId, cardId)) {
            trainingRepository.addUserCardProgress(userCardProgress);
        } else {
            trainingRepository.updateUserCardProgress(userCardProgress);
        }
    }

    public void updateIndexOfTrainingSession(TrainingSession trainingSession) {
        trainingRepository.updateIndexOfTrainingSession(trainingSession.getCurrentIndex() + 1, trainingSession.getSessionId());
    }
}
