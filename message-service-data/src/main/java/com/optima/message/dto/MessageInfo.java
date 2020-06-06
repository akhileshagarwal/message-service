package com.optima.message.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Date;

public class MessageInfo {

    private String messageBody;

    @JsonIgnore
    private Boolean fetched;

    @JsonIgnore
    private Date date;

    public MessageInfo(String messageBody, Boolean fetched, Date date) {
        this.messageBody = messageBody;
        this.fetched = false;
        this.date = new Date();
    }

    public MessageInfo(String messageBody) {
        this.messageBody = messageBody;
        this.fetched = false;
        this.date = new Date();
    }

    public Boolean isFetched() {
        return fetched;
    }

    public void setFetched(Boolean fetched) {
        this.fetched = fetched;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public Date getDate() {
        return date;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }

    @Override
    public String toString() {
        return "MessageInfo{" +
                "messageBody='" + messageBody + '\'' +
                ", fetched=" + fetched +
                ", date=" + date +
                '}';
    }
}
