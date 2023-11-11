package fernandes_dos_santos_dev_mob.construction;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.fernandes_dos_santos_dev_mob.R;

public class CameraActivity extends AppCompatActivity {
    private SensorManager managerCapteurs;
    private Sensor accelerometre, magnetometre;
    private SensorEventListener ecouteurAccelerometre, ecouteurMagnetometre;
    private boolean cameraActif;
    private float [] vecteurAcceleration, vecteurMagnetisme, matriceRotationI, matriceRotationR, vecteurOrientation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        cameraActif = true;
        managerCapteurs = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        // Accelerometre
        accelerometre = managerCapteurs.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        vecteurAcceleration = new float[3];

        // Magnetometre
        magnetometre = managerCapteurs.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        vecteurMagnetisme = new float[3];
        matriceRotationI = new float[9];
        matriceRotationR = new float[9];
        vecteurOrientation = new float[3];

        ecouteurAccelerometre = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                vecteurAcceleration = sensorEvent.values;
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {}
        };

        ecouteurMagnetometre = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                vecteurMagnetisme = sensorEvent.values;
                SensorManager.getRotationMatrix(matriceRotationR, matriceRotationI, vecteurAcceleration, vecteurMagnetisme);
                // Rotation de la matrice de rotation pour que l'axe Y traverse l'écran perpendiculairement
                float matriceRotationRetournee[] = new float[9];
                SensorManager.remapCoordinateSystem(matriceRotationR, SensorManager.AXIS_X, SensorManager.AXIS_Z, matriceRotationRetournee);
                SensorManager.getOrientation(matriceRotationRetournee, vecteurOrientation);
                // Vecteur orientation
                float vecteurOrientationX = (float) (Math.sin(vecteurOrientation[2]+Math.PI/2)/Math.PI);
                float vecteurOrientationY = (float) (Math.cos(vecteurOrientation[2]+Math.PI/2)/Math.PI);
                vecteurOrientation[0] = vecteurOrientationX;
                vecteurOrientation[1] = vecteurOrientationY;
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {}
        };
        managerCapteurs.registerListener(ecouteurAccelerometre, accelerometre, 16666);
        managerCapteurs.registerListener(ecouteurMagnetometre, magnetometre, 16666);
        ((InclinaisonView) findViewById(R.id.inclinaisonView)).setActiviteCamera(this);
        ((InclinaisonView) findViewById(R.id.inclinaisonView)).dessiner();
    }

    /**
     * Retourne l'état de la caméra
     */
    public boolean isCameraActif() {
        return cameraActif;
    }

    /**
     * Retourne le vecteur d'orientation. [0] = X, [1] = Y
     */
    public float[] getVecteurOrientation() {
        return vecteurOrientation;
    }

    /**
     * Termine l'activité et désactive les capteurs
     * @param view la vue
     */
    public void terminerActivite(View view){
        cameraActif = false;
        managerCapteurs.unregisterListener(ecouteurAccelerometre);
        managerCapteurs.unregisterListener(ecouteurMagnetometre);
        finish();
    }
}