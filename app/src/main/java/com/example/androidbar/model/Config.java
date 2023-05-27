package com.example.androidbar.model;

import android.content.Context;
import android.content.SharedPreferences;

public class Config {
    private static final String PREF_NAME = "AppConfig";
    private static final String KEY_IP_ADDRESS = "ipAddress";
    private static final String KEY_PORT = "port";

    // Valores por defecto
    private static final String DEFAULT_IP_ADDRESS = "192.168.1.145";
    private static final int DEFAULT_PORT = 7676;

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static void saveIpAddress(Context context, String ipAddress) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(KEY_IP_ADDRESS, ipAddress);
        editor.apply();
    }

    public static String loadIpAddress(Context context) {
        String ipAddress = getSharedPreferences(context).getString(KEY_IP_ADDRESS, null);
        return (ipAddress != null && !ipAddress.isEmpty()) ? ipAddress : DEFAULT_IP_ADDRESS;
    }

    public static void savePort(Context context, int port) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putInt(KEY_PORT, port);
        editor.apply();
    }

    public static int loadPort(Context context) {
        int port = getSharedPreferences(context).getInt(KEY_PORT, 0);
        return (port > 0 && port <= 65535) ? port : DEFAULT_PORT;
    }
}