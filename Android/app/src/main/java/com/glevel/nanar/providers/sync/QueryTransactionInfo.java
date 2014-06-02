//package com.glevel.nanar.providers.sync;
//
//import android.content.SharedPreferences;
//import android.util.Log;
//
//public class QueryTransactionInfo {
//
//    private static String TAG = "QueryTransactionInfo";
//
//    private static final String RESTFUL_PREFS = "nanar_prefs";
//    private static final String PREFS_DOWNLOAD_DATE = "dl_date";
//
//    private static final int TRANSACTION_COMPLETED = 100;
//    private static final int TRANSACTION_PENDING = 200;
//    private static final int TRANSACTION_RETRY = 300;
//    private static final int TRANSACTION_IN_PROGRESS = 500;
//
//    private static final int MAX_REQUEST_ATTEMPTS = 3;
//
//    private static final QueryTransactionInfo instance = new QueryTransactionInfo();
//
//    private int transacting = TRANSACTION_COMPLETED;
//    private int tryCount = 0;
//    private int result = 0;
//
//    private QueryTransactionInfo() {
//    }
//
//    public static QueryTransactionInfo getInstance() {
//        return instance;
//    }
//
//    /**
//     * Mark the query request as completed.
//     *
//     * @param httpResult The HttpStatus code.
//     */
//    public synchronized void markCompleted(int httpResult) {
//        transacting = TRANSACTION_COMPLETED;
//        tryCount = 0;
//        result = httpResult;
//    }
//
//    /**
//     * Mark the query request as pending.
//     */
//    public void markPending() {
//        if (transacting == TRANSACTION_PENDING) {
//            return;
//        }
//
//        synchronized (this) {
//            transacting = TRANSACTION_PENDING;
//            tryCount = 0;
//            result = 0;
//        }
//    }
//
//    /**
//     * If the call to the REST API fails, the query request
//     * will be attempted during five sync operations before
//     * it is marked as completed.
//     *
//     * @param httpResult The HttpStatus code.
//     * @return True if query should be retried.
//     */
//    public synchronized boolean markRetry(int httpResult) {
//        boolean markedRetry;
//
//        tryCount++;
//
//        Log.i(TAG, "retrieve.retry.httpResult[" + httpResult + "]");
//        Log.i(TAG, "retrieve.retry.tryCount[" + tryCount + "]");
//
//        if (tryCount < MAX_REQUEST_ATTEMPTS) {
//            Log.i(TAG, "retrieve.retry.RETRY");
//            transacting = TRANSACTION_RETRY;
//            markedRetry = true;
//        } else {
//            Log.i(TAG, "retrieve.retry.COMPLETE");
//            transacting = TRANSACTION_COMPLETED;
//            tryCount = 0;
//            markedRetry = false;
//        }
//        result = httpResult;
//
//        return markedRetry;
//    }
//
//    /**
//     * Mark the query request as in-progress.
//     */
//    public synchronized void markInProgress() {
//        transacting = TRANSACTION_IN_PROGRESS;
//    }
//
//    /**
//     * A refresh is outstanding if:
//     * - transacting = TRANSACTION_PENDING
//     * - transacting = TRANSACTION_RETRY
//     * - autoSync is true and data has not been refreshed in the last hour
//     *
//     * @param autoSync Automatically request a sync
//     *                 from the REST API if data has not been refreshed
//     *                 in the last hour.
//     * @return True is a sync operation should be requested.
//     */
//    public synchronized boolean isRefreshOutstanding(boolean autoSync) {
//        boolean refresh;
//
//        if (transacting == TRANSACTION_PENDING ||
//                transacting == TRANSACTION_RETRY) {
//            refresh = true;
//        } else if (transacting == TRANSACTION_IN_PROGRESS) {
//            refresh = false;
//        } else {
//
//            if (!autoSync) {
//                refresh = false;
//            } else {
//                // If transaction is in COMPLETED state
//                // and data has not been refresh for
//                // one hour a refresh will be requested
//                // automatically.
//
//                SharedPreferences prefs = RestfulApplication.getAppContext().getSharedPreferences(
//                        RESTFUL_PREFS, 0);
//                long dlMillis = prefs.getLong(PREFS_DOWNLOAD_DATE, 0);
//
//                if (dlMillis > 3600 * 1000) {
//                    refresh = true;
//                    transacting = TRANSACTION_PENDING;
//                } else {
//                    refresh = false;
//                }
//            }
//        }
//
//        return refresh;
//    }
//
//    @Override
//    public String toString() {
//        return "QueryTransactionInfo [transacting=" + transacting + ", tryCount=" + tryCount + ", result=" + result + "]";
//    }
//
//}
