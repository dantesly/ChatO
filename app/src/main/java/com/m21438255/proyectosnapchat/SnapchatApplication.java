package com.m21438255.proyectosnapchat;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseUser;


/**
 * Created by 21438255 on 16/12/2015.
 */
public class SnapchatApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // [Optional] Power your app with Local Datastore. For more info, go to
        // https://parse.com/docs/android/guide#local-datastore
        Parse.enableLocalDatastore(this);
        Parse.initialize(this);
        ParseUser user = new ParseUser();

    }

}
