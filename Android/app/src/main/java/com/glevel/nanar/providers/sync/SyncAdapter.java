//package com.glevel.nanar.providers.sync;
//
//import android.accounts.Account;
//import android.accounts.AccountManager;
//import android.content.AbstractThreadedSyncAdapter;
//import android.content.ContentProviderClient;
//import android.content.ContentResolver;
//import android.content.ContentUris;
//import android.content.ContentValues;
//import android.content.Context;
//import android.content.SyncResult;
//import android.database.Cursor;
//import android.net.Uri;
//import android.os.Bundle;
//import android.util.Log;
//
//import com.glevel.nanar.models.Tag;
//import com.glevel.nanar.providers.ContentProvider;
//import com.glevel.nanar.providers.rest.RestClient;
//
//import java.util.Date;
//
//public class SyncAdapter extends AbstractThreadedSyncAdapter {
//
//    private static final String TAG = "SyncAdapter";
//
//    private final AccountManager mAccountManager;
//    private final Context mContext;
//
//    private Date mLastUpdated;
//
//    public SyncAdapter(Context context, boolean autoInitialize) {
//        super(context, autoInitialize);
//        mContext = context;
//        mAccountManager = AccountManager.get(context);
//    }
//
//    /**
//     * Create a RESTCommand for each pending request.  When executed these command
//     * objects will make the call to the REST API.
//     */
//    public void onPerformSync(Account account, Bundle extras, String authority,
//                              ContentProviderClient provider, SyncResult syncResult) {
//        ContentResolver cr = getContext().getContentResolver();
//
//        Log.e(TAG, "Performing sync operation.");
//
//        Uri pendingUri;
//        int count;
//
//        Cursor cursor;
//        cursor = cr.query(ContentProvider.URI_TAGS, null, null, null, null);
//
//        final int colId = cursor.getColumnIndex(Tag._ID);
//        final int colLabel = cursor.getColumnIndex(Tag.COL_LABEL);
//
//        Tag tag;
//
//        while (cursor.moveToNext()) {
//
//            tag = Tag.fromCursor(cursor);
//
//            // If the row still exists and is still pending it will be
//            // updated to transacting in-progress.
//            pendingUri = ContentUris.withAppendedId(
//                    RestfulProvider.CONTENT_URI_SONGS_IN_PROGRESS, id);
//            count = cr.update(pendingUri, new ContentValues(), null, null);
//
//            if (count > 0) {
//                switch (methodEnum) {
//                    case POST:
//                        restCommand = new InsertCommand(id, title, artist);
//                        break;
//                    case PUT:
//                        restCommand = new UpdateCommand(id, songsId, title, artist);
//                        break;
//                    case DELETE:
//                        restCommand = new DeleteCommand(id, songsId);
//                        break;
//                    default:
//                        Log.e(TAG, "Invalid REST method: methodEnum[" + methodEnum + "]");
//                        syncResult.databaseError = true;
//                        continue;
//                }
//
//                if (!handleSync(restCommand, syncResult)) {
//                    break;
//                }
//            }
//
//        }
//
//        cursor.close();
//
//        QueryTransactionInfo queryTransInfo = QueryTransactionInfo.getInstance();
//        if (queryTransInfo.isRefreshOutstanding(true)) {
//            queryTransInfo.markInProgress();
//        }
//
//        Log.d(TAG, "leaving onPerformSync");
//    }
//
//    /**
//     * Sync the local database with the web service by executing the RESTCommand which
//     * will call the REST API.  Hard errors will cancel the sync operation.
//     * Soft errors will result in exponential back-off.
//     *
//     * @param restClient The rest client object to be executed.
//     * @param syncResult SyncAdapter-specific parameters. Here we use it to set
//     *                   soft and hard errors.
//     * @return True if the call to the REST API was successful.
//     * True if the call fails and request should be retried.
//     * False if the call fails and request should NOT be retried.
//     */
//    private boolean handleSync(RestClient restClient, SyncResult syncResult) {
//        try {
//            restClient.execute();
//        } catch (Exception e) {
//            Log.i(TAG, "Web service is not available.");
//            syncResult.stats.numIoExceptions = 1;
//            return true;
//        }
//        return false;
//    }
//
//}
