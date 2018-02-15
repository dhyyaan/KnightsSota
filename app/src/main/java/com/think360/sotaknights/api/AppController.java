package com.think360.sotaknights.api;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.think360.sotaknights.api.interfaces.AppComponent;
import com.think360.sotaknights.api.interfaces.DaggerAppComponent;
import com.think360.sotaknights.util.ConnectivityReceiver;

public class AppController extends Application {
    private static AppController instance;
    private static SharedPreferences sharedPreferencesShotaKnight;
    private AppComponent component;

  private RxBus bus;
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
       bus = new RxBus();
       component = DaggerAppComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .httpModule(new HttpModule())
                .build();

        sharedPreferencesShotaKnight = getSharedPreferences("ShotaKnightSharedPref", MODE_PRIVATE);




    }

  public RxBus bus() {
       return bus;
   }



    public AppComponent getComponent() {
        return component;
    }
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public static SharedPreferences getSharedPref() {
        return sharedPreferencesShotaKnight;
    }
    public static synchronized AppController getInstance() {
        return instance;
    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }
}

