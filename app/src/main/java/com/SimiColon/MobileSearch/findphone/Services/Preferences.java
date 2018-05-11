package com.SimiColon.MobileSearch.findphone.Services;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Delta on 28/02/2018.
 */

public class Preferences {

    Context context;

    public Preferences(Context context) {
        this.context = context;
    }

    public void CreateSharedPref(String id)
    {
        SharedPreferences pref = context.getSharedPreferences("user_id",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("id",id);
        updateSession("login");
        editor.apply();
    }
    public void ClearSharedPref()
    {
        SharedPreferences pref = context.getSharedPreferences("user_id",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        updateSession("logout");
        editor.clear();
        editor.apply();
    }

    private void updateSession(String session)
    {
        SharedPreferences pref = context.getSharedPreferences("user_id",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("session",session);
        editor.apply();

    }

    public String getSession()
    {
        SharedPreferences pref = context.getSharedPreferences("user_id",Context.MODE_PRIVATE);
        String session = pref.getString("session","");
        return session;
    }

    public String getId()
    {
        SharedPreferences pref = context.getSharedPreferences("user_id",Context.MODE_PRIVATE);
        String id = pref.getString("id","");
        return id;
    }
}
