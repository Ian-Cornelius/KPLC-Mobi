package com.ian_cornelius.kplcmobi.models;

/*
Model for Records we hold for purchase histories

Discovered that I can get which item is first or last visible in a recycler view using its layout manager.

Using the scroll listener, can use this to set floating text view data. But I find the month record being redundant.
Useful, yes, but the expected mean number of purchases a month will be 1. So, I'll classify on an yearly basis

Also, need a way to verify that monthly data being given is accurate. i.e We do not provide erroneous time.

Thus need a good time server.

Top classification of these records is based on account. I find no need of keeping that record explicitly here,
except for list generator who will be updating the records based on account type request

For updated records, in all generators, find a way of caching previous record on account type switch, to reduce network
data consumption, unless the requesting frag is killed, in which case we invalidate all caches
 */

public class PurchaseHistoryRecord {

    /*
    No need for explicit constructor in my opinion
     */

    /*
    Member variables

    I see public access as not dangerous within code since, with private access, the getter and setter names
    would still be obvious. And, to change specific records, need access to actual instances whose arraylist is
    private. So, still secure if you think about it
     */
    public String year, date, tokens, cash, tokenNumber;


}
