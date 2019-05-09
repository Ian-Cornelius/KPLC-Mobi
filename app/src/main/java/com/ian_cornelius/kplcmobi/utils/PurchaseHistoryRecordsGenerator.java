package com.ian_cornelius.kplcmobi.utils;

/*
Class to generate our purchase history records

Then, pass the array list to the adapter, on completion of network request for data

A singleton, like all generators we have.

But should these generators be singletons? I don't think so. Only doing read accesses.
Not stringent therefore. Being a singleton taking a lot of heap, since instance maintained for
entire lifetime once created, as I've noticed. Maybe am wrong.

I'm not. Static reference makes it last for the lifetime, once the classloader loads its class.

So, kill all generator singletons. Not necessary

Only enforce singletons in Request Processors, that do actual write to db.

Save for accountsList generator which seems to be a bit unique. Frequently accessed.

Work with singletons for now
 */

import com.ian_cornelius.kplcmobi.models.PurchaseHistoryRecord;
import com.ian_cornelius.kplcmobi.adapters.PurchaseHistoryRecyclerViewAdapter;

import java.util.ArrayList;

public class PurchaseHistoryRecordsGenerator {

    /*
    ArrayLists to hold our purchase histories.

    Ordered in terms of account ordering, in-app

    Instance of array-list only created on data request for that account-number

    **Set up above not memory efficient
    *
    * Why not work with a single array list. On account number change, change to string and cache the record, then
    * populate new data
     */

    private ArrayList<PurchaseHistoryRecord> purchaseHistoryRecords = new ArrayList<>();

    /*
    Hold the account number of the currently populated list. If requesting for same account, tell user that "You're
    already seeing the history for this account". Else, getPurchaseHistoryList() caches current list, generates new
    one if not previously cached.

    invalidateListeners() called in each generator to kill all firebase listeners instances, which never go
    away and bloat all the java heap and stack space we have!!
     */

    /*
    Our only instance
     */
    private static final PurchaseHistoryRecordsGenerator purchaseHistoryGenerator = new PurchaseHistoryRecordsGenerator();

    /*
    Adapter's reference
     */
    private PurchaseHistoryRecyclerViewAdapter adapter;

    /*
    get our instance
     */
    public static PurchaseHistoryRecordsGenerator getInstance(){

        return purchaseHistoryGenerator;
    }

    /*
    Method to get our history list

    Invoked to invoke firebase DB listeners to get data. Takes account number as argument

    DB listeners callback to a method here that callback to the adapter's method to pass this list

    To avoid redundant loading on db data change, keep a record of changed nodes, to inform us what data to specifically
    add to array list
     */
    public void getPurchaseHistoryList(String accountNumber){

        /*
        set up firebase listeners. Listener invoke populateHistoryData method, passing datasnapshot

        The latter method calls getData in adapter
         */

        /*
        generate dummy data
         */
        PurchaseHistoryRecord record = new PurchaseHistoryRecord();
        record.year = "2019";
        record.date = "12/2/2019";
        record.tokens = "90";
        record.cash = "1500";
        record.tokenNumber = "4512-3412-4512-4578-6543";
        purchaseHistoryRecords.add(record);

        record = new PurchaseHistoryRecord();
        record.year = "2019";
        record.date = "12/3/2019";
        record.tokens = "70";
        record.cash = "1100";
        record.tokenNumber = "3121-5612-5411-4566-9812";
        purchaseHistoryRecords.add(record);

        /*
        Update the adapter
         */
        adapter.updateAdapterData(purchaseHistoryRecords);

    }

    /*
    Get adapter's reference
     */
    public void getAdapterReference(PurchaseHistoryRecyclerViewAdapter adapter){

        this.adapter = adapter;
    }

    public void invalidateFirebaseListeners(){

        /*
        Kill all firebase listeners. Called on parent fragment onDetach method
         */
    }

}
