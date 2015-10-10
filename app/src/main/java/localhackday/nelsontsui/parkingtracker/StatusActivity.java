package localhackday.nelsontsui.parkingtracker;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;

public class StatusActivity extends AppCompatActivity {
    TextView distanceBox;
    SurfaceView arrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        createButtons();
    }

    private void createButtons(){
        arrow = (SurfaceView)findViewById(R.id.arrowBox);
        distanceBox = (TextView)findViewById(R.id.distanceBox);
        Bitmap arrowPicture = BitmapFactory.decodeResource(getResources(), R.drawable.arrow);
        Canvas c = new Canvas();
        c.drawBitmap(arrowPicture, arrow.getLeft(), arrow.getTop(), null);
        arrow.draw(c);
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
