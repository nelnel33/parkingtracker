package localhackday.nelsontsui.parkingtracker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableContainer;
import android.location.Location;
import android.opengl.Matrix;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;

public class StatusActivity extends AppCompatActivity {
    TextView distanceBox;
    ImageView arrow;

    LocationService locationService;

    Location parkedLocation;

    int arrowAngle;

    float distance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

        locationService = new LocationService(this,new FindCarListener(), 10000, 5000,LocationRequest.PRIORITY_HIGH_ACCURACY);
        parkedLocation = ((ParkingTracker)this.getApplicationContext()).getParkingLocation();

        createButtons();
    }

    private void createButtons(){
        arrow = (ImageView)findViewById(R.id.arrowBox);
        distanceBox = (TextView)findViewById(R.id.distanceBox);
        arrow.setImageResource(R.drawable.arrow);
    }

    private void rotateImageView(){

    }

    private void updateDistance(float distance, int arrowAngle){
        distanceBox.setText(""+distance+", "+arrowAngle);
    }

    public class FindCarListener implements LocationListener {
        public void onLocationChanged(Location location) {
            distance = location.distanceTo(parkedLocation);
            //TODO: does angle calculations and put it into the arrow
            arrowAngle = getAngle(location, parkedLocation, findDistance(location,parkedLocation));
            updateDistance(distance, arrowAngle);
        }
    }

    /**
     * Finds the distance between current location and destination location in terms of latitude and longitude.
     * @param curr
     * @param dest
     * @return
     */
    private int findDistance(Location curr, Location dest){
        double clat = curr.getLatitude();
        double clong = curr.getLongitude();
        double dlat = dest.getLatitude();
        double dlong = dest.getLongitude();

        int difflong = (int)Math.abs(clong-dlong);
        int difflat = (int)Math.abs(clat-dlat);

        int latsq = difflat*difflat;
        int longsq = difflong*difflong;

        return (int)Math.sqrt(latsq+longsq);
    }

    private int getAngle(Location curr, Location dest, float distance){
        double clat = curr.getLatitude();
        double clong = curr.getLongitude();
        double dlat = dest.getLatitude();
        double dlong = dest.getLongitude();

        double diffY = dest.getLatitude() - curr.getLatitude();
        double diffX = clong - dlong;
        int arrowAngle = (int)Math.acos(Math.abs(diffY/distance));

        if(diffY>0 && diffX>0){//QUAD 1
            return arrowAngle;
        }
        if(diffX<0 && diffY<0){//QUAD 3
            return 180+arrowAngle;
        }
        if(diffY>0 && diffX<0){//QUAD 4
            return 90+arrowAngle;
        }
        if(diffY<0 && diffX>0){//QUAD 2
            return 270+arrowAngle;
        }

        return arrowAngle;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_status, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
