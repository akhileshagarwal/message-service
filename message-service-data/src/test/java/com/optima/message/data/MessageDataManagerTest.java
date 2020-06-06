package com.optima.message.data;

import com.optima.message.dto.Message;
import com.optima.message.dto.MessageInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class MessageDataManagerTest {

    @Autowired
    private MessageDataManager messageDataManager;


    private String testEmail = "@email.com";
    private String testMessageBody = "Test";
    private String testRangeEmail = "testRange@mail.com";

    @BeforeEach
    void setUp() {
        IntStream.range(0, 10).forEach(value -> {
            List<String> emails = new ArrayList<>();
            emails.add(value + testEmail);
            emails.add(testRangeEmail);
            Message message = new Message(emails, testMessageBody);
            messageDataManager.deliverMessageToMail(message);
        });
    }

    @Test
    void testNotFetchedMessages() {

        //test
        List<String> messages = messageDataManager.getUnFetchedMessages(testRangeEmail);

        //result
        assertThat(messages.size() == 10);

    }

    @Test
    void testFetchMessagesRange() {
        //test
        List<String> messageInfos = messageDataManager.getMessagesByRange(testRangeEmail, "7-9");

        //result
        assertThat(messageInfos.size() == 3);

    }

    @SpringBootApplication
    static class TestMessageDataManager {
    }
}
