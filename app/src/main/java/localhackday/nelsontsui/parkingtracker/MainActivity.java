package localhackday.nelsontsui.parkingtracker;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.BroadcastReceiver;
import android.support.v4.content.LocalBroadcastManager;
import android.content.IntentFilter;

public class MainActivity extends AppCompatActivity {
    Button set_getParkedLocation;
    TextView statusBox;
    ImageView logo;
    BroadcastReceiver receiver;

    static String parkedString = "Parked";
    static String drivingString = "Not Parked";
    static String buttonParkedString = "Find Car";
    static String buttonDrivingString = "Park Here";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        createButtons();
        setListeners();

        // Create broadcast receiver
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String status = intent.getStringExtra("Status");
                if (status == "true") {
                    ((MainActivity) context).statusBox.setText(parkedString.toCharArray(), 0, parkedString.length());
                    ((MainActivity) context).set_getParkedLocation.setText(buttonParkedString.toCharArray(), 0, buttonParkedString.length());
                } else {
                    ((MainActivity) context).statusBox.setText(drivingString.toCharArray(), 0, drivingString.length());
                    ((MainActivity) context).set_getParkedLocation.setText(buttonDrivingString.toCharArray(), 0, buttonDrivingString.length());
                }
            }
        };
    }

    public void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver((receiver),
                new IntentFilter("ParkingTrackerStatusChanged")
        );
       new StatusListener(this);
    }

    public void onStop() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
        super.onStop();
    }

    private void createButtons(){
        set_getParkedLocation = (Button)findViewById(R.id.get_setParkedLocation);
        statusBox = (TextView)findViewById(R.id.statusBox);

        logo = (ImageView)findViewById(R.id.logoBox);
        logo.setImageResource(R.mipmap.logo);
    }

    private void setListeners(){
        set_getParkedLocation.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent((Context)MainActivity.this, StatusActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
