package localhackday.nelsontsui.parkingtracker;

import android.app.Application;
import android.location.Location;
import android.location.LocationListener;
import android.support.v4.content.LocalBroadcastManager;
import android.content.Intent;

/**
 * Created by Nelnel33 on 10/10/15.
 */
public class ParkingTracker extends Application {
    private boolean isParked;
    private Location parkingLocation;

    public ParkingTracker() {
        parkingLocation = new Location("");
        parkingLocation.setLatitude(40.91222222222222);
        parkingLocation.setLongitude(-37.12222222222222);
    }

    public boolean isParked() {
        return isParked;
    }

    public void setIsParked(boolean isParked) {
        if(this.isParked != isParked){
            LocalBroadcastManager broadcast = LocalBroadcastManager.getInstance(this);
            Intent intent = new Intent("ParkingTrackerStatusChanged");
            intent.putExtra("Status", isParked);
            broadcast.sendBroadcast(intent);
            this.isParked = isParked; // its fine
        }



    }

    public Location getParkingLocation() {
        return parkingLocation;
    }

    public void setParkingLocation(Location parkingLocation) {
        this.parkingLocation = parkingLocation;
    }
}
