package ru.star.bank.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.star.bank.repository.RecommendationRepository;
import ru.star.bank.service.RecommendationService;

import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private static final Pattern PATTERN =
            Pattern.compile("/recommend\\s+(\\w+\\.\\w+)");

    private final RecommendationRepository recommendationRepository;
    private final RecommendationService recommendationService;
    private final TelegramBot telegramBot;

    private final Logger logger =
            LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    public TelegramBotUpdatesListener(
            RecommendationRepository recommendationRepository,
            RecommendationService recommendationService,
            TelegramBot telegramBot
    ) {
        this.recommendationRepository = recommendationRepository;
        this.recommendationService = recommendationService;
        this.telegramBot = telegramBot;
    }

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        for (Update update : updates) {
            if (!hasTextMessage(update)) {
                continue;
            }

            long chatId = update.message().chat().id();
            String text = update.message().text().trim();

            if (isStartCommand(text)) {
                sendStartMessage(chatId);
                continue;
            }

            handleRecommendationCommand(chatId, text);
        }

        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    private boolean hasTextMessage(Update update) {
        return update.message() != null && update.message().text() != null;
    }

    private boolean isStartCommand(String text) {
        return "/start".equalsIgnoreCase(text);
    }

    private void sendStartMessage(long chatId) {
        telegramBot.execute(
                new SendMessage(
                        chatId,
                        "Привет! Используйте команду /recommend <username> для получения рекомендаций."
                )
        );
    }

    private void handleRecommendationCommand(long chatId, String text) {
        Matcher matcher = PATTERN.matcher(text);
        if (!matcher.matches()) {
            return;
        }

        String username = matcher.group(1);

        try {
            UserData userData = loadUserData(username);
            if (userData == null) {
                sendUserNotFound(chatId);
                return;
            }

            String recommendations = recommendationService
                    .getRecommendations(userData.userId())
                    .getRecommendations()
                    .stream()
                    .map(r -> r.getProductName() + ": " + r.getProductText())
                    .reduce((a, b) -> a + "\n" + b)
                    .orElse("Новых продуктов нет");

            telegramBot.execute(
                    new SendMessage(
                            chatId,
                            "Здравствуйте, " + userData.name() + " " + userData.surname()
                                    + ".\nНовые продукты для вас:\n"
                                    + recommendations
                    )
            );

        } catch (Exception e) {
            logger.error("Ошибка при обработке username: {}", username, e);
            sendUserNotFound(chatId);
        }
    }

    private UserData loadUserData(String username) {
        UUID userId = recommendationRepository.getId(username);
        String name = recommendationRepository.getName(username);
        String surname = recommendationRepository.getSurname(username);

        if (userId == null || name == null || surname == null) {
            return null;
        }

        return new UserData(userId, name, surname);
    }

    private void sendUserNotFound(long chatId) {
        telegramBot.execute(
                new SendMessage(chatId, "Пользователь не найден")
        );
    }

    private record UserData(UUID userId, String name, String surname) {}
}
