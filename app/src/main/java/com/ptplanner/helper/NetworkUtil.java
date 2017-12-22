package com.ptplanner.helper;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by su on 12/13/17.
 */
public class NetworkUtil {
    private static NetworkUtil ourInstance = null;

    public static NetworkUtil getInstance() {
        if (ourInstance == null)
            ourInstance = new NetworkUtil();
        return ourInstance;
    }

    private NetworkUtil() {
    }

    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager)context. getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
