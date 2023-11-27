package fernandes_dos_santos_dev_mob.activites.camera;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import androidx.annotation.Nullable;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class InclinaisonView extends View {
    private CameraActivity activiteCamera;
    private LocalDateTime dateDeb;
    private LocalDateTime dateFin;
    private final static int NB_FPS = 60;
    private final static int EPAISSEUR_CONTOUR = 10;
    private Paint peintureGris, peintureJaune, peintureGrisFonce;
    private float[] vecteur;

    public InclinaisonView(Context context) {
        super(context);
        init(null);
    }

    public InclinaisonView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public InclinaisonView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public InclinaisonView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    public void init(@Nullable AttributeSet set){
        peintureGris = new Paint(Paint.ANTI_ALIAS_FLAG);
        peintureGris.setColor(Color.rgb(100, 100, 100));
        peintureGris.setStrokeWidth(EPAISSEUR_CONTOUR);
        peintureGris.setStyle(Paint.Style.STROKE);

        peintureJaune = new Paint(Paint.ANTI_ALIAS_FLAG);
        peintureJaune.setColor(Color.rgb(220, 220, 0));
        peintureJaune.setStrokeWidth((int)(EPAISSEUR_CONTOUR*0.8));

        peintureGrisFonce = new Paint(Paint.ANTI_ALIAS_FLAG);
        peintureGrisFonce.setColor(Color.rgb(50, 50, 50));
        peintureGrisFonce.setStrokeWidth((int)(EPAISSEUR_CONTOUR*0.8));
    }

    public void onDraw(Canvas canvas){
        vecteur = activiteCamera.getVecteurInclinaison();
        canvas.drawLine(4, (float) getHeight()/2, getWidth()-4, (float) getHeight()/2, peintureGrisFonce);
        // Horizon
        canvas.drawLine((float) getWidth() /2, (float) getHeight() /2, (getWidth() /2)+(getWidth()*vecteur[0]), (getWidth() /2)+(getHeight()*vecteur[1]), peintureJaune);
        canvas.drawLine((float) getWidth() /2, (float) getHeight() /2, (getWidth() /2)+(getWidth()*-vecteur[0]), (getWidth() /2)+(getHeight()*-vecteur[1]), peintureJaune);
        // Contour
        canvas.drawCircle((float) getWidth() / 2, (float) getHeight() / 2, Math.min((getHeight()/2)-(int)(EPAISSEUR_CONTOUR*0.8), (getWidth()/2)-(int)(EPAISSEUR_CONTOUR*0.8)), peintureGris);
    }

    /**
     * Dessine l'horizon sur le canvas sur un autre thread. Limité à NB_FPS images par seconde
     */
    public void dessiner(){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                long delai;
                while(activiteCamera.isCameraActif()) { //
                    // Calcul du delai entre la derniere frame et la fin de l'actuelle
                    dateDeb = LocalDateTime.now();
                    postInvalidate();
                    dateFin = LocalDateTime.now();

                    // Calcul du delai en millisecondes
                    delai = ((dateFin.getNano()-dateDeb.getNano()+(int) 1e+9)%(int) 1e+9) / ((int) 1e+6);

                    // Regulation du nombre d'images par seconde
                    if (delai < 1000 / NB_FPS) {
                        try {
                            TimeUnit.MILLISECONDS.sleep((1000 / NB_FPS)-delai);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        };
        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(runnable);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int minw = getPaddingLeft() + getPaddingRight() + getSuggestedMinimumWidth();
        int w = resolveSizeAndState(minw, widthMeasureSpec, 1);

        int minh = getPaddingTop() + getPaddingBottom() + getSuggestedMinimumHeight();
        int h = resolveSizeAndState(minh, heightMeasureSpec, 1);

        setMeasuredDimension(w, h);
    }

    public void setActiviteCamera(CameraActivity cameraActivity){
        this.activiteCamera = cameraActivity;
    }
}
