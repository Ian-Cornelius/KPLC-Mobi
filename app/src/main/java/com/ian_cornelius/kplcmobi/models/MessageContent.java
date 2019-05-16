package com.ian_cornelius.kplcmobi.models;

/*
Model holding actual content for a message. Basically, sender, date, time, content
 */

public class MessageContent {

    //Use it to tell sender.
    public boolean isClient;

    //Date the message was sent
    public String date;

    //Time the message was sent
    public String time;

    //Message content
    public String messageContent;
}
