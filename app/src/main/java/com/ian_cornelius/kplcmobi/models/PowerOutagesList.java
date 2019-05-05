package com.ian_cornelius.kplcmobi.models;

/*
Model representing how PowerOutages will be structured.

Basically have:

1. Type:
        a. Weekly
        b. Monthly

2. Entry:
        Inner class. Represented by:
        a. Date
        b. descLink - link to its description


 */

public class PowerOutagesList {

    public enum OutageType{

        WEEKLY,
        MONTHLY
    }

    public OutageType type;
    public Entry entry;

    //Methods below are used when generating data

    /*
    Create an entry
     */
    public void createEntry(){
        this.entry = new Entry();
    }

    /*
    Set type
     */
    public void setType(OutageType type){
        this.type = type;
    }

    /*
        Inner class representing entry
         */
    public class Entry{

        private String date;
        private String descLink;
        private boolean willAffect;

        public void setDate(String date) {
            this.date = date;
        }

        public void setDescLink(String descLink){
            this.descLink = descLink;
        }

        public String getDate() {
            return date;
        }

        public String getDescLink() {
            return descLink;
        }

        public void setWillAffect(boolean willAffect) {
            this.willAffect = willAffect;
        }

        public boolean isWillAffect() {
            return willAffect;
        }
    }
}
