package localhackday.nelsontsui.parkingtracker.canvasobjects;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.SurfaceView;

import localhackday.nelsontsui.parkingtracker.R;

/**
 * Created by Nelnel33 on 10/10/15.
 */
public class DirectionView extends SurfaceView {
    public Bitmap arrow;
    public int angle;

    public DirectionView(Context context) {
        super(context);
        arrow = BitmapFactory.decodeResource(getResources(), R.drawable.arrow);
    }

    public void onDraw(Canvas canvas){
        canvas.drawBitmap(arrow, this.getLeft(), this.getTop(), null);
        canvas.rotate(angle);
    }


}
