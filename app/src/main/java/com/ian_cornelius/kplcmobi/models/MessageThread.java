package com.ian_cornelius.kplcmobi.models;

/*
Model for each message thread. Used for KPLC responses and inbox

Inbox data generator uses a singleton, cause we only have one thread for that one. Should be that way.
 */
import java.util.ArrayList;

public class MessageThread {

    /*
    Member variables
     */

    //Name of the thread/header
    public String threadName;

    //Reference to the location of the thread's messages (reference all messages here)
    public String threadMessagesRef;

    //Tell us whether this thread has a new message or not. Message lookup node tells us which node has this new message
    //No need to order. Can use firebase functions to do just that
    public boolean hasNewMessage;

    //Tell us the number of new messages - since we are ordering, use this to limit query, if updated local store
    //is available, to get only new messages, instead of a payload of all. Only request for all, if
    //no local message store is available
    public int newMessagesNum;

    //Hold object that holds these messages?? Meaning threads and messages loaded at same time? Don't think its good. Let's load on request, using ref

    //How do we show last message? Need to store it in thread, and updated on new message arrival

}
