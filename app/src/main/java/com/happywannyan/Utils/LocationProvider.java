package com.happywannyan.Utils;

import android.content.Context;
import android.location.LocationManager;

/**
 * Created by su on 6/20/17.
 */

public class LocationProvider {
    public static boolean GPS(Context context)
    {
      LocationManager  lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return lm.isProviderEnabled(LocationManager.GPS_PROVIDER) ||lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }
}
