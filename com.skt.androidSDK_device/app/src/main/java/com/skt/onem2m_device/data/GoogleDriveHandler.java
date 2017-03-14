package com.skt.onem2m_device.data;

import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.Metadata;
import com.google.android.gms.drive.MetadataChangeSet;
import com.google.android.gms.drive.query.Filters;
import com.google.android.gms.drive.query.Query;
import com.google.android.gms.drive.query.SearchableField;
import com.skt.onem2m_device.Const;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Iterator;

/**
 * Google drive processing handler
 *
 * Copyright (C) 2017. SK Telecom, All Rights Reserved.
 * Written 2017, by SK Telecom
 */
public class GoogleDriveHandler implements ConnectionCallbacks, OnConnectionFailedListener {
    private static final String     TAG = GoogleDriveHandler.class.getSimpleName();

    private static final int    CONNECTION_FAILED_POPUP = 1000;

    private AppCompatActivity   activity;           // activity for showing notification popup
    private CommandListener     commandListener;    // command listener for processing result
    private GoogleApiClient     googleApiClient;    // google api client

    /**
     * command listener
     */
    public interface CommandListener {
        /**
         * called when picture saved
         * @param result        result
         * @param resourceId    resource ID of saved picture
         */
        void onSavedPicture(boolean result, String resourceId);

        /**
         * called when picture downloaded
         * @param result       result
         * @param imageData    downloaded file
         */
        void onLoadedPicture(boolean result, byte[] imageData);
    }

    /**
     * constructor
     * @param activity           activity for showing account selection popup
     * @param commandListener    handler listener
     */
    public GoogleDriveHandler(AppCompatActivity activity, CommandListener commandListener) {
        this.activity = activity;
        this.commandListener = commandListener;
    }

    public void connect() {
//        googleAutoLogin();
        connectGoogleDrive();
    }

    public void disconnect() {
        googleApiClient.disconnect();
    }

    /**
     * save file to the Google drive
     * @param bytes    data for saving
     * @return calling result
     */
    public boolean savePicture(final byte[] bytes) {
        if (!googleApiClient.isConnected()) { return false; }

        Drive.DriveApi.newDriveContents(googleApiClient).setResultCallback(new ResultCallback<DriveApi.DriveContentsResult>() {
            @Override
            public void onResult(@NonNull DriveApi.DriveContentsResult driveContentsResult) {
                if (!driveContentsResult.getStatus().isSuccess()) {
                    commandListener.onSavedPicture(false, null);
                    return;
                }

                try {
                    OutputStream outputStream = driveContentsResult.getDriveContents().getOutputStream();
                    outputStream.write(bytes);
                    outputStream.close();
                } catch (IOException e1) {
                    commandListener.onSavedPicture(false, null);
                    return;
                }

                Calendar cal = Calendar.getInstance();
                final String fileName = String.format("%04d%02d%02d_%02d%02d%02d_%03d.jpg"
                        , cal.get(Calendar.YEAR)
                        , cal.get(Calendar.MONTH) + 1
                        , cal.get(Calendar.DAY_OF_MONTH)
                        , cal.get(Calendar.HOUR_OF_DAY)
                        , cal.get(Calendar.MINUTE)
                        , cal.get(Calendar.SECOND)
                        , cal.get(Calendar.MILLISECOND));

                // Create a file in the root folder
                MetadataChangeSet changeSet = new MetadataChangeSet.Builder().setTitle(fileName).setMimeType("image/jpg").build();
                Drive.DriveApi.getRootFolder(googleApiClient)
                        .createFile(googleApiClient, changeSet, driveContentsResult.getDriveContents())
                        .setResultCallback(new ResultCallback<DriveFolder.DriveFileResult>() {
                            @Override
                            public void onResult(@NonNull DriveFolder.DriveFileResult driveFileResult) {
                                if (!driveFileResult.getStatus().isSuccess()) {
                                    commandListener.onSavedPicture(false, null);
                                    return;
                                }
                                
                                // request sync to Google drive
                                Drive.DriveApi.requestSync(googleApiClient).setResultCallback(new ResultCallback<Status>() {
                                    @Override
                                    public void onResult(@NonNull Status status) {
                                        commandListener.onSavedPicture(status.isSuccess(), fileName);
                                    }
                                });
                            }
                        });
            }
        });
        return true;
    }

    /**
     * load file from the Google drive
     * @param fileName    file name to be saved in Google drive
     * @return calling result
     */
    public boolean loadPicture(final String fileName) {
        if (!googleApiClient.isConnected()) { return false; }
        Log.i(TAG, "loadPicture():" + fileName);

        // request sync to Google drive
        Drive.DriveApi.requestSync(googleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                if (!status.isSuccess()) {
                    commandListener.onLoadedPicture(false, null);
                    return;
                }

                // search file
                Query query = new Query.Builder().addFilter(Filters.eq(SearchableField.TITLE, fileName)).build();
                Drive.DriveApi.getRootFolder(googleApiClient)
                        .queryChildren(googleApiClient, query)
                        .setResultCallback(new ResultCallback<DriveApi.MetadataBufferResult>() {
                            @Override
                            public void onResult(@NonNull DriveApi.MetadataBufferResult metadataBufferResult) {
                                if (!metadataBufferResult.getStatus().isSuccess()) {
                                    commandListener.onLoadedPicture(false, null);
                                    return;
                                }

                                // load file
                                Iterator<Metadata> iterator = metadataBufferResult.getMetadataBuffer().iterator();
                                if (iterator.hasNext()) {
                                    Metadata metadata = iterator.next();
                                    metadata.getDriveId().asDriveFile().open(googleApiClient, DriveFile.MODE_READ_ONLY, null)
                                            .setResultCallback(new ResultCallback<DriveApi.DriveContentsResult>() {
                                                @Override
                                                public void onResult(@NonNull DriveApi.DriveContentsResult driveContentsResult) {
                                                    if (!driveContentsResult.getStatus().isSuccess()) {
                                                        commandListener.onLoadedPicture(false, null);
                                                        return;
                                                    }

                                                    // notify result
                                                    InputStream inputStream = driveContentsResult.getDriveContents().getInputStream();
                                                    try {
                                                        byte[] readFile = new byte[inputStream.available()];
                                                        inputStream.read(readFile);
                                                        inputStream.close();
                                                        commandListener.onLoadedPicture(true, readFile);
                                                    } catch (IOException e) {
                                                        e.printStackTrace();
                                                        commandListener.onLoadedPicture(false, null);
                                                    }
                                                }
                                            });
                                }
                            }
                        });
            }
        });

        return true;
    }

    /**
     * login to the Google : currently not used
     */
//    private void googleAutoLogin() {
//        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestProfile()
//                .requestScopes(new Scope(Scopes.PROFILE))
//                .requestEmail()
//                .requestIdToken(Const.GOOGLE_SERVER_CLIENT_ID)
//                .requestServerAuthCode(Const.GOOGLE_SERVER_CLIENT_ID)
//                .setAccountName(Const.GOOGLE_ACCOUNT)
//                .build();
//
//        googleApiClient = new GoogleApiClient.Builder(activity)
//                .enableAutoManage(activity, null)
//                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
//                .build();
//
//        googleApiClient.connect();
////        GoogleSignInResult signInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(Auth.GoogleSignInApi.getSignInIntent(googleApiClient));
////        Log.d(TAG, "googleSignInResult = " + signInResult.isSuccess());
////        activity.startActivity(Auth.GoogleSignInApi.getSignInIntent(googleApiClient));
//        OptionalPendingResult<GoogleSignInResult> pendingResult = Auth.GoogleSignInApi.silentSignIn(googleApiClient);
//        if (pendingResult != null) {
//            if (pendingResult.isDone()) {
//                // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
//                // and the GoogleSignInResult will be available instantly.
//                Log.d(TAG, " ----------------  CACHED SIGN-IN ------------");
//                Log.d(TAG, "pendingResult is done = ");
//                GoogleSignInResult signInResult = pendingResult.get();
//                connectGoogleDrive();
//            } else {
//                Log.d(TAG, "Setting result callback");
//                // If the user has not previously signed in on this device or the sign-in has expired,
//                // this asynchronous branch will attempt to sign in the user silently.  Cross-device
//                // single sign-on will occur in this branch.
//                pendingResult.setResultCallback(new ResultCallback<GoogleSignInResult>() {
//                    @Override
//                    public void onResult(GoogleSignInResult googleSignInResult) {
//                        Log.d(TAG, "googleSignInResult = " + googleSignInResult.isSuccess());
//                        GoogleSignInAccount signInAccount = googleSignInResult.getSignInAccount();
//                        if (signInAccount != null) {
//                            String emailAddress = signInAccount.getEmail();
//                            String token = signInAccount.getIdToken();
//                            Log.d(TAG, "token = " + token);
//                            Log.d(TAG, "emailAddress = " + emailAddress);
//                        }
//                        connectGoogleDrive();
//                    }
//                });
//            }
//        }
//        else {
//        }
//    }

    /**
     * connect to the Google drive
     */
    private void connectGoogleDrive() {
        googleApiClient = new GoogleApiClient.Builder(activity)
                .addApi(Drive.API)
                .addScope(Drive.SCOPE_FILE)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
//                .setAccountName(Const.GOOGLE_ACCOUNT)
                .build();

        googleApiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i(TAG, "API client connected.");
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "GoogleApiClient connection suspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // Called whenever the API client fails to connect.
        Log.i(TAG, "GoogleApiClient connection failed: " + connectionResult.toString());
        if (!connectionResult.hasResolution()) {
            // show the localized error dialog.
            GoogleApiAvailability.getInstance().getErrorDialog(activity, connectionResult.getErrorCode(), 0).show();
            return;
        }
        // The failure has a resolution. Resolve it.
        // Called typically when the app is not yet authorized, and an
        // authorization
        // dialog is displayed to the user.
        try {
            connectionResult.startResolutionForResult(activity, CONNECTION_FAILED_POPUP);
        } catch (IntentSender.SendIntentException e) {
            Log.e(TAG, "Exception while starting resolution activity", e);
        }
    }

    /**
     * handling of connection fail popup
     * @param requestCode    popup request code
     * @param resultCode     popup result code
     * @param data           popup data
     * @return handling result
     */
    public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CONNECTION_FAILED_POPUP) {
            if (resultCode == AppCompatActivity.RESULT_OK) {
                connect();
            }
            return true;
        }
        return false;
    }
}
