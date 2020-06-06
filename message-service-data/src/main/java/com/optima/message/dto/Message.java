package com.optima.message.dto;


import java.util.ArrayList;
import java.util.List;


public class Message {

    private List<String> toEmails;

    private MessageInfo messageInfo;

    public Message(List<String> toEmails, String messageBody) {
        this.toEmails = new ArrayList<>(toEmails);
        this.messageInfo = new MessageInfo(messageBody);
    }

    public List<String> getToEmails() {
        return toEmails;
    }

    public MessageInfo getMessageInfo() {
        return messageInfo;
    }

    public void setToEmails(List<String> toEmails) {
        this.toEmails = toEmails;
    }

    public void setMessageInfo(MessageInfo messageInfo) {
        this.messageInfo = messageInfo;
    }
}
