package com.jpeace.triviaapp.controller;

import android.app.Application;

import com.google.gson.Gson;

public class MyApp extends Application {

    private static MyApp mInstance;
    private Gson mGSon;

    public static MyApp getInstance(){
        return mInstance;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        mInstance=this;
        mGSon=new Gson();
    }

    public Gson getGSon(){
        return mGSon;
    }
}
