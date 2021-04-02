package dev.stanuch.htmleditor.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PostControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldRedirectToIndex() throws Exception {
        this.mockMvc
            .perform(
                get("/")
            )
            .andExpect(status().is3xxRedirection());
    }

    @Test
    void shouldReturnForm() throws Exception {
        this.mockMvc
            .perform(
                get("/post")
            )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(
                    content()
                        .string(containsString("<form"))
                );
    }

    @Test
    void whenGetNotExistingPostIdShouldThrowError() throws Exception {
        this.mockMvc
            .perform(
                get("/post/123")
            )
                .andExpect(status().is4xxClientError());
    }
}
