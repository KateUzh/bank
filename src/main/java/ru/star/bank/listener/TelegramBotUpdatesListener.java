package ru.star.bank.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.star.bank.repository.RecommendationRepository;
import ru.star.bank.service.RecommendationService;

import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    public static Pattern PATTERN = Pattern.compile("(start) (\\w+\\.\\w+)");

    @Autowired
    RecommendationRepository recommendationRepository;
    private Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    @Autowired
    RecommendationService recommendationService;

    @Autowired
    private TelegramBot telegramBot;

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            logger.info("Processing update: {}", update);
            long chatId = update.message().chat().id();
            Matcher matcher = PATTERN.matcher(update.message().text());
            if (matcher.find()) {
                String username = matcher.group(2);
                UUID userID = recommendationRepository.getId(username);
                String name = recommendationRepository.getName(username);
                String surname = recommendationRepository.getSurname(username);
                String recommendations = recommendationService.getRecommendations(userID).toString();
                SendMessage message = new SendMessage(chatId, "Здравствуйте, " + name + " " + surname + ". \n" +
                        "Новые продукты для Вас: " + recommendations);
                SendResponse response = telegramBot.execute(message);
            }
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }
}
