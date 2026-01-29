package com.example.logging_demo.controller;

import com.example.logging_demo.model.Person;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.mock.web.MockHttpSession;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class QuizControllerTest {

    @Autowired
    private MockMvc mockMvc;

    /**
     * Тест 1:
     * Проверка инициализации сессии при первом заходе
     */
    @Test
    void firstVisit_initializesSession() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("question"))
                .andExpect(model().attributeExists("index"))
                .andExpect(model().attributeExists("total"));
    }

    /**
     * Тест 2:
     * Проверка перехода к следующему вопросу после ответа
     */
    @Test
    void answer_incrementsQuestionIndex() throws Exception {
        MockHttpSession session = new MockHttpSession();

        // Первый заход — инициализация
        mockMvc.perform(get("/").session(session));

        // Ответ на первый вопрос
        mockMvc.perform(post("/answer")
                        .param("option", "0")
                        .session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        // Проверка второго вопроса
        mockMvc.perform(get("/").session(session))
                .andExpect(status().isOk())
                .andExpect(model().attribute("index", 2));
    }

    /**
     * Тест 3:
     * Проверка начисления баллов персонажу
     */
    @Test
    void answer_addsPointsToPerson() throws Exception {
        MockHttpSession session = new MockHttpSession();

        mockMvc.perform(get("/").session(session));

        mockMvc.perform(post("/answer")
                        .param("option", "0")
                        .session(session))
                .andExpect(status().is3xxRedirection());

        mockMvc.perform(get("/").session(session))
                .andExpect(request().sessionAttribute("score",
                        org.hamcrest.Matchers.hasKey(Person.ANSAR)));
    }

    /**
     * Тест 4:
     * Проверка перехода на страницу результата
     */
    @Test
    void lastQuestion_redirectsToResult() throws Exception {
        MockHttpSession session = new MockHttpSession();

        mockMvc.perform(get("/").session(session));

        // Проходим все вопросы
        for (int i = 0; i < 8; i++) {
            mockMvc.perform(post("/answer")
                            .param("option", "0")
                            .session(session));
        }

        mockMvc.perform(get("/").session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/result"));
    }

    /**
     * Тест 5:
     * Проверка определения победителя
     */
    @Test
    void resultPage_returnsWinner() throws Exception {
        MockHttpSession session = new MockHttpSession();

        mockMvc.perform(get("/").session(session));

        for (int i = 0; i < 8; i++) {
            mockMvc.perform(post("/answer")
                            .param("option", "0")
                            .session(session));
        }

        mockMvc.perform(get("/result").session(session))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("person"));
    }

    /**
     * Тест 6:
     * Проверка очистки сессии после результата
     */
    @Test
    void resultPage_invalidatesSession() throws Exception {
        MockHttpSession session = new MockHttpSession();

        mockMvc.perform(get("/").session(session));

        for (int i = 0; i < 8; i++) {
            mockMvc.perform(post("/answer")
                            .param("option", "0")
                            .session(session));
        }

        mockMvc.perform(get("/result").session(session))
                .andExpect(status().isOk());

        mockMvc.perform(get("/").session(session))
                .andExpect(model().attribute("index", 1));
    }

    /**
     * Тест 7:
     * Защита от прямого перехода на /result
     */
    @Test
    void directResultAccess_redirectsToQuiz() throws Exception {
        mockMvc.perform(get("/result"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }
}
