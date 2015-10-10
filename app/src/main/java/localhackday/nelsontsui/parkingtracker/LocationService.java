package localhackday.nelsontsui.parkingtracker;

import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import android.content.Context;
import android.os.Bundle;

/**
 * Location service
 * Responsible for connecting to Google Play API and registering location listeners
 */
public class LocationService implements ConnectionCallbacks {
    private LocationRequest locationRequest;
    private LocationListener listener;
    private GoogleApiClient client;

    public LocationService(Context context) {
        client = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();
    }

    public LocationService(Context context, LocationListener listener, long interval,
                           long fastestInterval, int priority) {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(interval);
        locationRequest.setFastestInterval(fastestInterval);
        locationRequest.setPriority(priority);

        client = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();

        this.listener = listener;
    }

    /**
     * Return an instance of the Google API client
     */
    public GoogleApiClient getClient() {
        return client;
    }

    /**
     * Executed on connection to Play Services API
     *
     * @param connectionHint
     */
    public void onConnected(Bundle connectionHint) {
        if (listener != null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(client, locationRequest, listener);
        }
    }

    /**
     * Executed when connection to Google Play Services is suspended
     */
    public void onConnectionSuspended(int cause) {
        // TODO: CHECK IF THIS IS REQUIRED
        if (listener != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(client, listener);
        }
    }
}
