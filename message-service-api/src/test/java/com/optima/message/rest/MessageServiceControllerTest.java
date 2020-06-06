package com.optima.message.rest;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.optima.message.dto.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest
@AutoConfigureMockMvc
public class MessageServiceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private String testMessageBody = "Test";

    private List<String> emails;
    private Message message1;
    private Message message2;

    @BeforeEach
    void setUp() {
        emails = new ArrayList<>(Arrays.asList("1@mail.com"));
        message1 = new Message(emails, testMessageBody);
    }

    @Test
    void testDeliver()
            throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/message")
                .content(asJsonString(message1))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @Test
    void testRetrieveUnFetchedMessages()
            throws Exception {

        IntStream.range(0, 5).forEach(value -> {
            try {
                emails = new ArrayList<>(Arrays.asList("2@mail.com"));
                message2 = new Message(emails, testMessageBody + value);
                mockMvc.perform(MockMvcRequestBuilders.post("/message")
                        .content(asJsonString(message2))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        mockMvc.perform(MockMvcRequestBuilders.get("/messages/2@mail.com")
                .param("indexes", "2-4")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        mockMvc.perform(MockMvcRequestBuilders.get("/messages/2@mail.com")
                .param("unFetched", "true")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("[\"Test0\",\"Test1\"]")));
    }

    @Test
    void testRetrieveMessagesByIndexes()
            throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/message")
                .content(asJsonString(message1))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        IntStream.range(0, 5).forEach(value -> {
            try {
                emails = new ArrayList<>(Arrays.asList("2@mail.com"));
                message2 = new Message(emails, testMessageBody + value);
                mockMvc.perform(MockMvcRequestBuilders.post("/message")
                        .content(asJsonString(message2))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        mockMvc.perform(MockMvcRequestBuilders.get("/messages/2@mail.com")
                .param("indexes", "2-4")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("[\"Test2\",\"Test3\",\"Test4\"]")));
    }

    @Test
    void testRetrieveMessagesByIncorrectIndexes()
            throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/messages/2@mail.com")
                .param("indexes", "2-")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(content().string(equalTo("Incorrect Indexes")));
    }

    @Test
    void testRetrieveMessagesByIncorrectEmail()
            throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/messages/unknown@mail.com")
                .param("indexes", "2-5")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("[]")));
    }

    @Test
    void testRetrieveMessagesByNullEmail()
            throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/messages/null")
                .param("indexes", "2-5")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("[]")));
    }

    @Test
    void testDeleteMessagesByIndexes()
            throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/message")
                .content(asJsonString(message1))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.delete("/messages/1@mail.com/0-0")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("[\"Test\"]")));
    }

    @Test
    void testDeleteMessagesByInCorrectIndexes()
            throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/message")
                .content(asJsonString(message1))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.delete("/messages/1@mail.com/0-9")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("[\"Test\",\"Test\"]")));
    }

    public static String asJsonString(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            final String jsonContent = mapper.writeValueAsString(obj);
            return jsonContent;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
