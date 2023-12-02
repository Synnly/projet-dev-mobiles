package fernandes_dos_santos_dev_mob.activites.camera;

import android.content.Context;
import android.content.Intent;
import android.graphics.*;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.fernandes_dos_santos_dev_mob.R;

import java.io.*;

public class CameraActivity extends AppCompatActivity {
    private SensorManager managerCapteurs;
    private Sensor accelerometre, magnetometre;
    private SensorEventListener ecouteurAccelerometre, ecouteurMagnetometre;
    private boolean cameraActif;
    private float [] vecteurAcceleration, vecteurMagnetisme, matriceRotationI, matriceRotationR, vecteurInclinaison, vecteurInclinaisonPrecedent, vecteurOrientation;
    private float orientationPrecedent;
    private Camera camera;
    private FrameLayout frameLayout;
    private VueCamera vueCamera;
    private Camera.PictureCallback photoCallBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        initCapteurs();

        //Camera
        cameraActif = true;
        frameLayout = findViewById(R.id.frameCamera);
        camera = Camera.open();
        vueCamera = new VueCamera(this, camera);
        frameLayout.addView(vueCamera);

        // Callback de la prise de photo
        photoCallBack = new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                FileOutputStream fos;
                try {
                    // Sauvegarde de la photo
                    fos = openFileOutput("bitmap.data", MODE_PRIVATE);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 30, fos); // Compression de la photo (ici 30 mais franchement j'ai pas l'impression que le poids de l'image diminue significativement)
                    fos.flush();
                    fos.close();

                    terminerActivite(RESULT_OK); // Retour à l'activité précédente
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }

    /**
     * Retourne l'état de la caméra
     * @return true si la caméra est active, false sinon
     */
    public boolean isCameraActif() {
        return cameraActif;
    }

    /**
     * Retourne le vecteur d'inclinaison. [0] = X, [1] = Y
     */
    public float[] getVecteurInclinaison() {
        return vecteurInclinaison;
    }

    /**
     * Calcule le vecteur d'inclinaison et le mets dans vecteurInclinaison[]. Le resultat est "lissé" en retournant la moyenne entre l'angle precédent et la mesure actuelle.<br>
     * /!\ Le système de coordonnées est remappé -> l'axe Y devient l'axe Z
     */
    public void calculerVecteurInclinaison(){
        vecteurInclinaison = new float[3];
        float matriceRotationRetournee[] = new float[9];
        float[] temp = new float[2];

        // Remappage des axes
        SensorManager.remapCoordinateSystem(matriceRotationR, SensorManager.AXIS_X, SensorManager.AXIS_Z, matriceRotationRetournee);
        SensorManager.getOrientation(matriceRotationRetournee, vecteurInclinaison);

        // Calcul du vecteur d'inclinaison actuel
        temp[0] = (float) (Math.sin(vecteurInclinaison[2]+Math.PI/2)/Math.PI);
        temp[1] = (float) (Math.cos(vecteurInclinaison[2]+Math.PI/2)/Math.PI);

        // Lissage
        vecteurInclinaison[0] = (float) ((Math.sin(vecteurInclinaison[2]+Math.PI/2)/Math.PI) + vecteurInclinaisonPrecedent[0])/2;
        vecteurInclinaison[1] = (float) ((Math.cos(vecteurInclinaison[2]+Math.PI/2)/Math.PI) + vecteurInclinaisonPrecedent[1])/2;

        // Sauvegarde du vecteur d'inclinaison actuel pour le lissage suivant
        vecteurInclinaisonPrecedent[0] = temp[0];
        vecteurInclinaisonPrecedent[1] = temp[1];
    }

    /**
     * Calcule l'orientation en degrés. Le resultat est "lissé" en retournant la moyenne entre l'angle precédent et la mesure actuelle. <br/>
     * /!\ Le système de coordonnées est remappé pour celui de base
     * @return L'orientation
     */
    public float calculerOrientation(){
        vecteurOrientation = new float[3];
        float matriceRotationRetournee[] = new float[9];
        float temp;

        // Remappage des axes
        SensorManager.remapCoordinateSystem(matriceRotationR, SensorManager.AXIS_X, SensorManager.AXIS_Y, matriceRotationRetournee);
        SensorManager.getOrientation(matriceRotationRetournee, vecteurOrientation);

        temp = orientationPrecedent;
        orientationPrecedent = (float) Math.round(Math.toDegrees(vecteurOrientation[0])); // Calcul de l'orientation actuelle et sauvegarde pour le lissage suivant

        return (orientationPrecedent+temp)/2; // Lissage et retour de l'orientation
    }

    /**
     * Retourne le cardinal correspondant à l'angle.
     * @param angle L'angle en degrés
     * @return L'initiale du cardinal si compris entre 180° et -180°, N sinon
     */
    public String getCardinal(float angle){
        if(angle >= -45 && angle < 45){
            return "N";
        }
        else if(angle >= 45 && angle < 135){
            return "E";
        }
        else if(angle >= 135 || angle < -135){
            return "S";
        }
        else if(angle >= -135 && angle < -45){
            return "O";
        }
        else{
            return "N";
        }
    }

    /**
     * Initialise les capteurs d'inclinaison et d'orientation ainsi que leurs ecouteurs
     */
    public void initCapteurs(){
        orientationPrecedent = 0;
        vecteurInclinaisonPrecedent = new float[]{0, 0, 0};
        managerCapteurs = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        // Accelerometre
        accelerometre = managerCapteurs.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        vecteurAcceleration = new float[3];

        ecouteurAccelerometre = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                vecteurAcceleration = sensorEvent.values;
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {}
        };

        // Magnetometre
        magnetometre = managerCapteurs.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        vecteurMagnetisme = new float[3];
        matriceRotationI = new float[9];
        matriceRotationR = new float[9];
        vecteurInclinaison = new float[3];

        ecouteurMagnetometre = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                vecteurMagnetisme = sensorEvent.values;
                SensorManager.getRotationMatrix(matriceRotationR, matriceRotationI, vecteurAcceleration, vecteurMagnetisme);
                calculerVecteurInclinaison();
                ((TextView) findViewById(R.id.cardinal)).setText(getCardinal(calculerOrientation()));
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {}
        };
        // Demarrage des capteurs
        managerCapteurs.registerListener(ecouteurAccelerometre, accelerometre, 16666);
        managerCapteurs.registerListener(ecouteurMagnetometre, magnetometre, 16666);

        // Dessin de l'inclinaison
        ((InclinaisonView) findViewById(R.id.inclinaisonView)).setActiviteCamera(this);
        ((InclinaisonView) findViewById(R.id.inclinaisonView)).dessiner();
    }

    /**
     * Désactive les capteurs, termine l'activité, et retourne dans l'activité précédente
     */
    public void terminerActivite(int resultCode){
        cameraActif = false;
        managerCapteurs.unregisterListener(ecouteurAccelerometre);
        managerCapteurs.unregisterListener(ecouteurMagnetometre);
        Intent intent = new Intent();
        setResult(resultCode, intent);
        finish();
    }

    /**
     * Prend une photo
     * @param view La vue
     */
    public void prendrePhoto(View view){
        camera.takePicture(null, null, photoCallBack);
    }
}