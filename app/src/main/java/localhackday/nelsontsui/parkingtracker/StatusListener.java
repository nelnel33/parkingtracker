package localhackday.nelsontsui.parkingtracker;

import java.util.ArrayList;
import android.location.Location;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import android.content.Context;
import android.support.v4.app.NotificationCompat;
import android.content.Intent;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.app.NotificationManager;

/**
 * Main location listener
 *
 * Runs in the background and determines whether user is parked/driving
 * Initializes RangeListener once user is parked
 */
public class StatusListener {
    private static final int UPDATE_INTERVAL = 10000; // Prefer to receive updates every 10s
    private static final int FASTEST_INTERVAL = 5000; // Receive updates a minimum of every 5s
    private static final int PRIORITY = LocationRequest.PRIORITY_HIGH_ACCURACY; // Request high accuracy data

    private static final int PARK_PERIODS = 15; // Number of successive periods w/ reduced velocity before status changed
    private static final double DRIVING_THRESHOLD = 8.9408; // Minimum velocity for driving (20mph)
    private static final double THRESHOLD_DISTANCE = 30.48; // Within this range (100ft), the user is shown a notification

    private ArrayList<Location> locations; // Stack to hold locations from previous updates
    private ArrayList<Double> velocities; // Stores velocities from previous updates
    private Context context;

    /**
     * Initialize stack and connect to Google Play Services API
     */
    public StatusListener(Context callingContext) {
        locations = new ArrayList<Location>();
        velocities = new ArrayList<Double>();
        context = callingContext;

        LocationListener listener = this.new StatusLocationListener();
        new LocationService(context, listener, UPDATE_INTERVAL, FASTEST_INTERVAL, PRIORITY);
    }

    public void maybeNotifyUser(ParkingTracker appContext, Location currentLocation) {
        if(currentLocation.distanceTo(appContext.getParkingLocation()) <= THRESHOLD_DISTANCE) {
            NotificationCompat.Builder withinDistance = new NotificationCompat.Builder(context)
            .setSmallIcon(R.drawable.ic_stat_name)
            .setContentTitle("Click to find car")
            .setContentText("You're within "+ currentLocation.distanceTo(appContext.getParkingLocation()) + "m");

            Intent resultIntent = new Intent(context, StatusActivity.class);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            stackBuilder.addParentStack(StatusActivity.class);
            stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0 , PendingIntent.FLAG_UPDATE_CURRENT);

            withinDistance.setContentIntent(resultPendingIntent);

            NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(1, withinDistance.build());
        }
    }

    /**
     * Listen for location changes and updates parking status if necessary
     */
    public class StatusLocationListener implements LocationListener {
        public void onLocationChanged(Location location) {
            // Add location to stack
            locations.add(location);

            if (locations.size() == 1)
                return; // Wait until we have at least two locations

            // Calculate average velocity since last update
            Location lastLocation = locations.get(locations.size() - 1);
            long timeElapsed = location.getTime() - lastLocation.getTime();
            double newVelocity = location.distanceTo(lastLocation) / timeElapsed;

            velocities.add(newVelocity); // Push velocity to velocity list

            // If velocity over last PARK_PERIODS was less than the driving threshold, status <- parked
            if (velocities.size() < PARK_PERIODS)
                return;

            double totalVelocity = 0;

            for (int i = velocities.size(); i >= velocities.size() - PARK_PERIODS - 1; i--)
                totalVelocity += velocities.get(i);

            double avgVelocity = totalVelocity / PARK_PERIODS;
            ParkingTracker appContext = (ParkingTracker) context.getApplicationContext();

            if (avgVelocity >= DRIVING_THRESHOLD) {
                appContext.setIsParked(false);
            } else if (appContext.isParked()) {
                // Notify user if they are within range of car
                maybeNotifyUser(appContext, location);
            } else {
                appContext.setIsParked(true);

                // Parking location <- parking location PARK_PERIODS ago
                Location parkingLocation = locations.get(locations.size() - PARK_PERIODS - 1);
                appContext.setParkingLocation(parkingLocation);
            }
        }
    }
}