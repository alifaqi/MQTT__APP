package rss.android.mqtt_app;

import android.content.pm.PackageManager;
import android.location.Location;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

/**
 * Created by AliFakih on 15-Dec-19.
 */

public class LocationClass {
    private FusedLocationProviderClient client;
    MainActivity activity;

    public LocationClass(MainActivity mainActivity){
        activity=mainActivity;
    }

    Location l;

    public Location getLocation() {
        requestPermission();

        client= LocationServices.getFusedLocationProviderClient(activity);

        if (android.support.v4.app.ActivityCompat.checkSelfPermission(activity, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return null;
        }
        client.getLastLocation().addOnSuccessListener(activity, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    l=location;

                }
            }
        });
        return l;
    }



    public void requestPermission(){
        android.support.v4.app.ActivityCompat.requestPermissions(activity,new String[]{ACCESS_FINE_LOCATION},1);
    }
}
