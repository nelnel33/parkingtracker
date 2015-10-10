package localhackday.nelsontsui.parkingtracker;

import android.app.Application;
import android.location.Location;
import android.location.LocationListener;

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
        this.isParked = isParked;
    }

    public Location getParkingLocation() {
        return parkingLocation;
    }

    public void setParkingLocation(Location parkingLocation) {
        this.parkingLocation = parkingLocation;
    }
}
