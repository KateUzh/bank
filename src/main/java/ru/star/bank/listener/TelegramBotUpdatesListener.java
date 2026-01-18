package ru.star.bank.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
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

    private static final Pattern PATTERN = Pattern.compile("/recommend\\s+(\\w+\\.\\w+)");

    private final RecommendationRepository recommendationRepository;
    private final RecommendationService recommendationService;
    private final TelegramBot telegramBot;

    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

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
            if (update.message() == null || update.message().text() == null) continue;

            long chatId = update.message().chat().id();
            String text = update.message().text().trim();

            if (text.equalsIgnoreCase("/start")) {
                telegramBot.execute(new SendMessage(chatId,
                        "Привет! Используйте команду /recommend <username> для получения рекомендаций."));
                continue;
            }

            Matcher matcher = PATTERN.matcher(text);
            if (matcher.matches()) {
                String username = matcher.group(1);

                try {
                    UUID userId = recommendationRepository.getId(username);
                    String name = recommendationRepository.getName(username);
                    String surname = recommendationRepository.getSurname(username);

                    if (userId == null || name == null || surname == null) {
                        telegramBot.execute(new SendMessage(chatId, "Пользователь не найден"));
                        continue;
                    }

                    String recommendations = recommendationService.getRecommendations(userId)
                            .getRecommendations()
                            .stream()
                            .map(r -> r.getProductName() + ": " + r.getProductText())
                            .reduce((a, b) -> a + "\n" + b)
                            .orElse("Новых продуктов нет");

                    telegramBot.execute(new SendMessage(chatId,
                            "Здравствуйте, " + name + " " + surname + ".\nНовые продукты для вас:\n" + recommendations));

                } catch (Exception e) {
                    logger.error("Ошибка при обработке username: " + username, e);
                    telegramBot.execute(new SendMessage(chatId, "Пользователь не найден"));
                }
            }
        }
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }
}
