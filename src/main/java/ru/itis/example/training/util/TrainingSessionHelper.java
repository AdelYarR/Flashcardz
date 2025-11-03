package ru.itis.example.training.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import ru.itis.example.models.TrainingSession;
import ru.itis.example.training.repository.TrainingRepository;
import ru.itis.example.training.service.TrainingService;

public class TrainingSessionHelper {

    private static TrainingRepository trainingRepository;
    private static TrainingService trainingService;

    public static boolean hasActiveTrainingSession(HttpServletRequest request, Long cardGroupId) {
        trainingRepository = (TrainingRepository) request.getServletContext().getAttribute("training_repository");
        trainingService = new TrainingService(trainingRepository);

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("training_session_id".equals(cookie.getName())) {
                    String sessionId = cookie.getValue();
                    TrainingSession session = trainingService.getByTrainingSessionId(sessionId);
                    return cardGroupId.equals(session.getCardGroupId());
                }
            }
        }
        return false;
    }
}
