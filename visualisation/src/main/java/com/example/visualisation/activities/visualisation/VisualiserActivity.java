package com.example.visualisation.activities.visualisation;

import android.graphics.*;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.myapplication.R;
import com.example.visualisation.donnees.Modele;
import com.example.visualisation.donnees.Mur;
import com.example.visualisation.donnees.Piece;
import com.example.visualisation.donnees.Porte;
import com.example.visualisation.filesUtils.FilesUtils;

import java.io.IOException;
import java.util.ArrayList;

public class VisualiserActivity extends AppCompatActivity {

    private Modele modele;
    private Piece piece;
    private int orientation;
    private ImageView imageView;
    private TextView textViewOrientation, textViewNomPiece;
    private SurfaceView surfaceView;
    private Paint peintureBleue, peintureNoire;
    private static final int WIDTH_SCREEN = 300;
    private static final int HEIGHT_SCREEN = 400;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualiser);
        String path = getIntent().getStringExtra("path");
        orientation = Mur.NORD;
        imageView = findViewById(R.id.imageViewPiece);
        textViewOrientation = findViewById(R.id.textViewOrientation);
        textViewNomPiece = findViewById(R.id.textViewNomPiece);
        surfaceView = findViewById(R.id.surfaceViewPiece);

        try {
            modele = FilesUtils.chargerModele(this, path);
            System.out.println(modele.toJSON());
            piece = modele.getListePieces().get(getIntent().getIntExtra("indice", 0)); // Piece de départ
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        creerPeinture();
        textViewNomPiece.setText(piece.getNomPiece());
        surfaceView.setZOrderOnTop(true);
        surfaceView.getHolder().setFormat(PixelFormat.TRANSPARENT);
    }

    public void chargerImage(int orientation){
        Bitmap b = piece.getMur(orientation).getImageBitmap();
        imageView.setImageBitmap(b);
        textViewOrientation.setText(getInitiale(orientation));
        dessinerAcces(piece.getMur(orientation).getListePortes(), surfaceView.getHolder());
    }

    /**
     * Effectue une rotation de 90° vers la droite de la vue
     */
    public void rotationDroite(View view){
        orientation = (orientation + 1) % 4;
        chargerImage(orientation);
    }

    /**
     * Effectue une rotation de 90° vers la gauche de la vue
     */
    public void rotationGauche(View view){
        orientation = (orientation + 3) % 4;
        chargerImage(orientation);
    }

    /**
     * Retourne l'initiale de l'orientation
     * @param orientation L'orientation
     * @return L'initiale de l'orientation si l'orientation est valide (Mur.NORD, Mur.EST, Mur.SUD ou Mur.OUEST), "" sinon
     */
    public String getInitiale(int orientation){
        switch(orientation){
            case Mur.NORD:
                return "N";
            case Mur.EST:
                return "E";
            case Mur.SUD:
                return "S";
            case Mur.OUEST:
                return "O";
            default:
                return "";
        }
    }

    public void creerPeinture(){
        peintureBleue = new Paint(Paint.ANTI_ALIAS_FLAG);
        peintureBleue.setColor(Color.argb(64, 0, 128, 255));
        peintureBleue.setStrokeWidth(5);
        peintureBleue.setStyle(Paint.Style.FILL_AND_STROKE);

        peintureNoire = new Paint(Paint.ANTI_ALIAS_FLAG);
        peintureNoire.setColor(Color.BLACK);
        peintureNoire.setStrokeWidth(5);
        peintureNoire.setStyle(Paint.Style.FILL_AND_STROKE);
        peintureNoire.setTextAlign(Paint.Align.CENTER);
        peintureNoire.setTextSize(50);
    }

    /**
     * Dessine tous les acces sur la surfaceView
     * @param listePortes La liste des acces à dessiner
     * @param surfaceHolderDessin Le surfaceHolder de la surfaceView
     */
    public void dessinerAcces(ArrayList<Porte> listePortes, SurfaceHolder surfaceHolderDessin){
        Canvas c = surfaceHolderDessin.lockCanvas();
        c.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        for(Porte porte : listePortes){
            int x1 = (int) ((porte.getLeft() / dpToPx(WIDTH_SCREEN)) * c.getWidth());
            int y1 = (int) ((porte.getTop() / dpToPx(HEIGHT_SCREEN)) * c.getHeight());
            int x2 = (int) ((porte.getRight() / dpToPx(WIDTH_SCREEN)) * c.getWidth());
            int y2 = (int) ((porte.getBottom() / dpToPx(HEIGHT_SCREEN)) * c.getHeight());
            c.drawRect(new Rect(x1, y1 ,x2, y2), peintureBleue);
            int xPos = ((x1 + x2) / 2);
            int yPos = (int) (((y1 + y2) / 2) - ((peintureBleue.descent() + peintureBleue.ascent()) / 2)) ;
            c.drawText(porte.getPieceArrivee() == null ? "null" : porte.getPieceArrivee().getNomPiece(), xPos, yPos, peintureNoire);
        }
        surfaceHolderDessin.unlockCanvasAndPost(c);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        chargerImage(orientation);
    }

    /**
     * Convertis des dp en px
     * @param dp La valeur en dp
     * @return La valeur en px
     */
    public double dpToPx(int dp){
        return dp * getResources().getDisplayMetrics().density;
    }

}