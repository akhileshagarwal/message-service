package com.optima.message.data;

import com.optima.message.dto.Message;
import com.optima.message.dto.MessageInfo;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class MessageDataManager {

    private static final Map<String, List<MessageInfo>> EMAIL_TO_MESSAGES_MAP = new HashMap<>();

    private static final Logger LOG = Logger.getLogger(MessageDataManager.class.getName());

    public MessageDataManager() {
    }

    public void deliverMessageToMail(Message message) {
        message.getToEmails().stream().forEach(email -> {
            EMAIL_TO_MESSAGES_MAP.computeIfAbsent(email, s -> new ArrayList<>())
                    .add(message.getMessageInfo());
        });
        LOG.info("Leaving deliverMessageToMail");
    }

    public List<String> getMessagesByRange(String email, String indexes) {
        if (!isEmailPresent(email)) {
            return Collections.EMPTY_LIST;
        }

        int startIndex = Integer.parseInt(indexes.split("-")[0]);
        int endIndex = Integer.parseInt(indexes.split("-")[1]);

        return EMAIL_TO_MESSAGES_MAP.get(email).stream()
                .skip(startIndex)
                .limit(endIndex - startIndex + 1)
                .sorted(Comparator.comparing(MessageInfo::getDate))
                .map(messageInfo -> {
                    messageInfo.setFetched(true);
                    return messageInfo.getMessageBody();
                })
                .collect(Collectors.toList());
    }

    public List<String> getUnFetchedMessages(String email) {
        if (!isEmailPresent(email)) {
            return Collections.EMPTY_LIST;
        }

        return EMAIL_TO_MESSAGES_MAP.get(email).stream()
                .filter(messageInfo -> !messageInfo.isFetched())
                .map(MessageInfo::getMessageBody)
                .collect(Collectors.toList());
    }

    public List<String> deleteMessages(String email, String indexes) {
        if (!isEmailPresent(email)) {
            return Collections.EMPTY_LIST;
        }

        List<String> deletedMessages = new ArrayList<>();
        int startIndex = Integer.parseInt(indexes.split("-")[0]);
        int endIndex = Integer.parseInt(indexes.split("-")[1]);

        List<MessageInfo> messageInfos = EMAIL_TO_MESSAGES_MAP.get(email);
        for (int index = endIndex; index >= startIndex; index--) {
            if (messageInfos != null && messageInfos.size() < index + 1) {
                continue;
            }
            deletedMessages.add(messageInfos.get(index).getMessageBody()); //Collect the deleted messages
            messageInfos.remove(index); // Removed from the Map
        }
        LOG.info("Leaving Delete");
        return deletedMessages;
    }

    private boolean isEmailPresent(String email) {
        return EMAIL_TO_MESSAGES_MAP.containsKey(email);
    }
}
