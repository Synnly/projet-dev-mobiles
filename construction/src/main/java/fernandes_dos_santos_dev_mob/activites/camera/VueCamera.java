package fernandes_dos_santos_dev_mob.activites.camera;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import androidx.annotation.NonNull;

import java.io.IOException;

public class VueCamera extends SurfaceView implements SurfaceHolder.Callback {

    private Camera camera;
    private SurfaceHolder holder;

    public VueCamera(Context context, Camera camera) {
        super(context);
        this.camera = camera;
        holder = getHolder();
        holder.addCallback(this);
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        Camera.Parameters parametres = camera.getParameters();
        if(this.getResources().getConfiguration().orientation != Configuration.ORIENTATION_PORTRAIT) {
            parametres.set("orientation", "portrait");
        }

        parametres.setPictureFormat(ImageFormat.JPEG);
        parametres.setPictureSize(1600, 1200);
        parametres.setPreviewSize(640, 480);
        parametres.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        camera.setDisplayOrientation(90);
        parametres.setRotation(90);

        camera.setParameters(parametres);
        try {
            camera.setPreviewDisplay(holder);
            camera.startPreview();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
        camera.stopPreview();
        camera.release();
    }
}
