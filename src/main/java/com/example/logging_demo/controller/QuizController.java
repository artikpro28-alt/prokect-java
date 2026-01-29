package com.example.logging_demo.controller;

import com.example.logging_demo.model.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

@Controller
public class QuizController {

    private static final Logger log = LoggerFactory.getLogger(QuizController.class);
    private final List<Question> questions;

    public QuizController() {
        log.info("Инициализация QuizController");

        questions = List.of(

            new Question(
                "Тебе дали свободный день. Что ты делаешь?",
                List.of(
                    new AnswerOption("Залипаю в аниме / сериалы", Map.of(Person.ANSAR, 2)),
                    new AnswerOption("Думаю, как провести его с пользой", Map.of(Person.ALEM, 2)),
                    new AnswerOption("Сплю до обеда", Map.of(Person.ARTUR, 2)),
                    new AnswerOption("Иду в зал или чем-то заняться", Map.of(Person.AMIR, 2)),
                    new AnswerOption("Иду на работу🫩", Map.of(Person.ARUZHAN, 2))
                )
            ),

            new Question(
                "Твоя роль в групповых чатах:",
                List.of(
                    new AnswerOption("Кидаю мемы и приколы", Map.of(Person.ANSAR, 2)),
                    new AnswerOption("Пишу редко, но по делу", Map.of(Person.ARTUR, 2)),
                    new AnswerOption("Читаю всё, но отвечаю выборочно", Map.of(Person.ALEM, 2)),
                    new AnswerOption("Комментирую почти всё", Map.of(Person.AMIR, 2)),
                    new AnswerOption("АХАХАХАХАХАХАХАХ", Map.of(Person.ARUZHAN, 2))
                )
            ),

            new Question(
                "Как ты относишься к спорам?",
                List.of(
                    new AnswerOption("Если что — пошучу", Map.of(Person.ANSAR, 2)),
                    new AnswerOption("Выскажу своё мнение, даже если не спрашивали", Map.of(Person.AMIR, 2)),
                    new AnswerOption("Послушаю всех и сделаю вывод", Map.of(Person.ALEM, 2)),
                    new AnswerOption("Скажу коротко и по факту", Map.of(Person.ARTUR, 2)),
                    new AnswerOption("Споры — это весело!", Map.of(Person.ARUZHAN, 2))
                )
            ),

            new Question(
                "Что тебя больше всего раздражает?",
                List.of(
                    new AnswerOption("Когда не уважают интересы других", Map.of(Person.ANSAR, 2)),
                    new AnswerOption("Когда всё делают без плана", Map.of(Person.ALEM, 2)),
                    new AnswerOption("Когда говорят ерунду", Map.of(Person.ARTUR, 2)),
                    new AnswerOption("Когда говорят не по факту", Map.of(Person.AMIR, 2)),
                    new AnswerOption("Когда меня перебивают", Map.of(Person.ARUZHAN, 2))
                )
            ),

            new Question(
                "Если бы ты выбирал хобби:",
                List.of(
                    new AnswerOption("Коллекционировать что-нибудь", Map.of(Person.ANSAR, 2)),
                    new AnswerOption("Разбираться в сложных вещах", Map.of(Person.ALEM, 2)),
                    new AnswerOption("Что-то связанное с техникой", Map.of(Person.ARTUR, 2)),
                    new AnswerOption("Физическая активность", Map.of(Person.AMIR, 2)),
                    new AnswerOption("Творчество", Map.of(Person.ARUZHAN, 2))
                )
            ),

            new Question(
                "Твой стиль мышления:",
                List.of(
                    new AnswerOption("Интуитивный и лёгкий", Map.of(Person.ANSAR, 2)),
                    new AnswerOption("Логичный и расчётливый", Map.of(Person.ALEM, 2)),
                    new AnswerOption("Прямой и практичный", Map.of(Person.ARTUR, 2)),
                    new AnswerOption("Напористый", Map.of(Person.AMIR, 2)),
                    new AnswerOption("Креативный", Map.of(Person.ARUZHAN, 2))
                )
            ),

            new Question(
                "Любимая игра",
                List.of(
                    new AnswerOption("Sekiro:Shadow Die Twice", Map.of(Person.ANSAR, 2)),
                    new AnswerOption("Grand Theft Auto 5 Online", Map.of(Person.ALEM, 2)),
                    new AnswerOption("Forza Horizon", Map.of(Person.ARTUR, 2)),
                    new AnswerOption("Dota 2", Map.of(Person.AMIR, 2)),
                    new AnswerOption("Brawl Stars", Map.of(Person.ARUZHAN, 2))
                )
            ),

            new Question(
                "Если что-то пошло не по плану:",
                List.of(
                    new AnswerOption("Ну и ладно 😄", Map.of(Person.ANSAR, 2, Person.ARTUR, 2)),
                    new AnswerOption("Переделаю план", Map.of(Person.ALEM, 2)),
                    new AnswerOption("Скажу, как надо было делать", Map.of(Person.AMIR, 2)),
                    new AnswerOption("Придумаю что-то новое и интересное", Map.of(Person.ARUZHAN, 2))
                )
            )
        );

        log.info("Загружено вопросов: {}", questions.size());
    }

    @GetMapping("/")
    public String quiz(Model model, HttpSession session) {

        Integer current = (Integer) session.getAttribute("current");

        if (current == null) {
            log.info("Новая сессия пользователя, старт теста");

            current = 0;
            session.setAttribute("current", 0);

            Map<Person, Integer> score = new EnumMap<>(Person.class);
            for (Person p : Person.values()) score.put(p, 0);
            session.setAttribute("score", score);
        }

        if (current >= questions.size()) {
            log.info("Все вопросы пройдены, переход к результату");
            return "redirect:/result";
        }

        log.info("Отображается вопрос №{}", current + 1);

        model.addAttribute("question", questions.get(current));
        model.addAttribute("index", current + 1);
        model.addAttribute("total", questions.size());

        return "quiz";
    }

    @PostMapping("/answer")
    public String answer(@RequestParam int option, HttpSession session) {

        int current = (int) session.getAttribute("current");
        Map<Person, Integer> score =
                (Map<Person, Integer>) session.getAttribute("score");

        Question q = questions.get(current);
        AnswerOption chosen = q.getOptions().get(option);

        log.info("Ответ на вопрос №{}: {}", current + 1, chosen.getText());

        chosen.getPoints().forEach((person, points) -> {
            score.put(person, score.get(person) + points);
            log.info("Начислено {} баллов персонажу {}", points, person);
        });

        session.setAttribute("current", current + 1);
        session.setAttribute("score", score);

        return "redirect:/";
    }

    @GetMapping("/result")
    public String result(Model model, HttpSession session) {

        Map<Person, Integer> score =
                (Map<Person, Integer>) session.getAttribute("score");

        if (score == null || score.isEmpty()) {
            log.warn("Попытка открыть результат без прохождения теста");
            return "redirect:/";
        }

        Person winner = Collections.max(
                score.entrySet(),
                Map.Entry.comparingByValue()
        ).getKey();

        log.info("Итоговый результат теста: {}", winner);

        model.addAttribute("person", winner);

        session.invalidate();
        log.info("Сессия пользователя очищена");

        return "result";
    }
}
