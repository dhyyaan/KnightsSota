package com.think360.sotaknights.services.notification;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.think360.sotaknights.api.AppController;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
  //  private static final String TAG = MyFirebaseInstanceIDService.class.getSimpleName();

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        // Saving reg id to shared preferences
        storeRegIdInPref(refreshedToken);
    }
    private void storeRegIdInPref(String token) {
        AppController.getSharedPref().edit().putString("firebase_reg_token",token).apply();
    }
}

