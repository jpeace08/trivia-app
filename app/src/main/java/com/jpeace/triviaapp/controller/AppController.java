package com.jpeace.triviaapp.controller;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;


public class AppController {
    public static final String REQ_TAG = "request_volley";
    public static final String CURRENT_INDEX="current_index";
    public static final String CURRENT_SCORE="current_score";
    public static final String PREFS_NAME="my_prefs";

    private static AppController mInstance;
    private static SharedPreferences mSharedPreferences;
    private RequestQueue mRequestQueue;
    private Gson mGSon;

    private AppController(){
        mSharedPreferences=MyApp.getInstance().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        mRequestQueue= Volley.newRequestQueue(MyApp.getInstance().getApplicationContext());
        mGSon=MyApp.getInstance().getGSon();
    }

    public RequestQueue getRequestQueue(){
        return mRequestQueue;
    }

    public <T> void addRequest(Request<T> request){
        if (request.getTag()==null || request.getTag() == ""){
            request.setTag(REQ_TAG);
        }
        getRequestQueue().add(request);
    }

    public void clearRequest(String tag){
        mRequestQueue.cancelAll(tag);
    }

    public static AppController getInstance() {
        if (mInstance == null) {
            synchronized (AppController.class) {
                mInstance = new AppController();
            }
        }
        return mInstance;
    }

    public <T> T get(String key, Class<T> anonymousClass,T defaultValue){
        if (anonymousClass==String.class){
            return (T) String.valueOf(mSharedPreferences.getString(key, (String) defaultValue));
        }
        else if (anonymousClass==Integer.class){
            return (T) Integer.valueOf(mSharedPreferences.getInt(key, (Integer) defaultValue));
        }
        else if (anonymousClass==Boolean.class){
            return (T) Boolean.valueOf(mSharedPreferences.getBoolean(key, (Boolean) defaultValue));
        }
        else if (anonymousClass==Float.class){
            return (T) Float.valueOf(mSharedPreferences.getFloat(key, (Float) defaultValue));
        }
        else if (anonymousClass==Long.class){
            return (T) Long.valueOf(mSharedPreferences.getLong(key, (Long) defaultValue));
        }
        else return (T) MyApp.getInstance().getGSon().fromJson(mSharedPreferences.getString(key, ""), anonymousClass);
    }

    public  <T> void put(String key, T data){
        SharedPreferences.Editor editor=mSharedPreferences.edit();

        if (data instanceof String){
            editor.putString(key, (String) data);
        }
        else if (data instanceof Integer){
            editor.putInt(key, (Integer) data);
        }
        else if (data instanceof Boolean){
            editor.putBoolean(key, (Boolean) data);
        }
        else if(data instanceof Float){
            editor.putFloat(key, (Float) data);
        }
        else if(data instanceof Long){
            editor.putLong(key, (Long) data);
        }
        else editor.putString(key, MyApp.getInstance().getGSon().toJson(data));

        editor.apply();
    }
}
