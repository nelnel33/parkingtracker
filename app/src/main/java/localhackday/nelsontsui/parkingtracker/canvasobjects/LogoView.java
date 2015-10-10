package localhackday.nelsontsui.parkingtracker.canvasobjects;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.SurfaceView;

/**
 * Created by Nelnel33 on 10/10/15.
 */
public class LogoView extends SurfaceView {
    private Bitmap logo;

    public LogoView(Context context) {
        super(context);
    }

    @Override
    public void onDraw(Canvas canvas){
        canvas.drawBitmap(logo, this.getLeft(), this.getTop(),null);
    }
}
