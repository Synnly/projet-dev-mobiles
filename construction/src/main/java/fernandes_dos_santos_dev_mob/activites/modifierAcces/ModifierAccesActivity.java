package fernandes_dos_santos_dev_mob.activites.modifierAcces;

import android.content.Intent;
import android.graphics.*;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.fernandes_dos_santos_dev_mob.R;
import fernandes_dos_santos_dev_mob.activites.Utils.FilesUtils;
import fernandes_dos_santos_dev_mob.donnees.Modele;
import fernandes_dos_santos_dev_mob.donnees.Mur;
import fernandes_dos_santos_dev_mob.donnees.Piece;
import fernandes_dos_santos_dev_mob.donnees.Porte;

import java.io.IOException;
import java.util.ArrayList;

public class ModifierAccesActivity extends AppCompatActivity {

    private Modele modele;
    private Mur murActuel;
    private ArrayList<Porte> listePortes;
    private RecyclerView recyclerView;
    private String path;
    private boolean appuieSurEcran = false;
    private int pointeur1X, pointeur1Y, pointeur2X, pointeur2Y, nbContacts;
    private Rect rectangle;
    private ImageView imageView;
    private Paint peintureBleue, peintureNoire;
    private static final int TAILLE_TEXTE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifier_acces);
        path = getIntent().getStringExtra("path");
        try {
            modele = FilesUtils.chargerModele(this, path);
        } catch (IOException e) {
            Toast.makeText(this, "Erreur lors de la lecture du modèle", Toast.LENGTH_SHORT).show();
        }
        int idPiece = getIntent().getIntExtra("idPiece", 0);
        int orientation = getIntent().getIntExtra("orientation", 0);

        // Recuperation du mur et de la liste de portes
        for (Piece p : modele.getListePieces()) {
            if (p.getIdPiece() == idPiece) {
                for (Mur m : p.getListeMurs()) {
                    if (m.getOrientation() == orientation) {
                        murActuel = m;
                        for(Porte p1 : m.getListePortes()){
                            System.out.println(p1.getPieceArrivee());
                        }
                        break;
                    }
                }
                break;
            }
        }
        listePortes = murActuel.getListePortes();

        BitmapDrawable image = new BitmapDrawable(getResources(), murActuel.getImageBitmap());
        ((ImageView) findViewById(R.id.imageMur)).setImageDrawable(image);

        // Initialisation des variables
        SurfaceView surfaceDessin = findViewById(R.id.rectangleSelection);
        SurfaceHolder surfaceHolderDessin = surfaceDessin.getHolder();

        // Preparation du holder
        surfaceDessin.setZOrderOnTop(true);
        surfaceDessin.getHolder().setFormat(PixelFormat.TRANSPARENT);

        creerPeintures();

        imageView = findViewById(R.id.imageMur);
        imageView.setOnTouchListener(creerEcouteur(surfaceDessin, surfaceHolderDessin, peintureBleue));

        creerRecyclerView();
    }

    /**
     * Cree le RecyclerView et l'initialise avec la liste de portes
     */
    public void creerRecyclerView() {
        recyclerView = findViewById(R.id.recyclerViewAcces);
        RecyclerView.Adapter<AccesAdapter.AccesViewHolder> accesAdapter = new AccesAdapter(listePortes, modele.getListePieces(), murActuel.getPiece());
        recyclerView.setAdapter(accesAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(ModifierAccesActivity.this));
    }

    /**
     * Crée un ecouteur pour le toucher de l'image. Dessine un rectangle en fonction des coordonnees des pointeurs. Les coordonnees sont limitees par la taille de la surface de dessin
     * @param surfaceDessin La surface de dessin
     * @param surfaceHolderDessin Le holder de la surface de dessin
     * @param peinture La peinture
     * @return
     */
    public View.OnTouchListener creerEcouteur(SurfaceView surfaceDessin, SurfaceHolder surfaceHolderDessin, Paint peinture){
        return new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                // Memorisation du type d'evenement
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    appuieSurEcran = true;
                }
                else if (event.getAction() == MotionEvent.ACTION_UP) {
                    appuieSurEcran = false;
                }

                if(appuieSurEcran) {
                    if (event.getPointerCount() == 2) {
                        nbContacts = event.getPointerCount();
                        rectangle = creerRectangle(event, surfaceDessin);
                        rectangle.sort();
                        clearCanvasAndDrawRectangle(rectangle, surfaceHolderDessin);
                    }
                }
                else {
                    clearCanvas(surfaceHolderDessin);

                    if (nbContacts == 2) {
                        if (pointeur1X - pointeur2X == 0 || pointeur1Y - pointeur2Y == 0) {
                            Toast.makeText(ModifierAccesActivity.this, "La zone d'acces est invalide.", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            // Creation du rectangle. Pointeurs forcément initialisés dans le bloc if parent
                            rectangle = new Rect(pointeur1X, pointeur1Y, pointeur2X, pointeur2Y);
                            rectangle.sort();

                            Porte porte = new Porte(murActuel, rectangle, null);
                            clearCanvas(surfaceHolderDessin);
                            creerRecyclerView();
                        }
                    }
                }
                return true;
            }
        };
    }

    /**
     * Cree un rectangle en fonction des coordonnees des pointeurs. Les coordonnees sont limitees par la taille de la surface de dessin
     * @param event L'evenement
     * @param surfaceDessin La surface de dessin
     * @return Le rectangle
     */
    public Rect creerRectangle(MotionEvent event, SurfaceView surfaceDessin){
        pointeur1X = Math.max(Math.min((int) event.getX(0), surfaceDessin.getWidth()), 0);
        pointeur1Y = Math.max(Math.min((int) event.getY(0), surfaceDessin.getHeight()), 0);
        pointeur2X = Math.max(Math.min((int) event.getX(1), surfaceDessin.getWidth()), 0);
        pointeur2Y = Math.max(Math.min((int) event.getY(1), surfaceDessin.getHeight()), 0);

        return new Rect(pointeur1X, pointeur1Y, pointeur2X, pointeur2Y);
    }

    /**
     * Nettoie le canvas de la surface de dessin
     * @param surfaceHolderDessin Le holder de la surface de dessin
     */
    public void clearCanvas(SurfaceHolder surfaceHolderDessin){
        Canvas c = surfaceHolderDessin.lockCanvas();
        c.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        dessinerAcces(c, listePortes);
        surfaceHolderDessin.unlockCanvasAndPost(c);
    }

    /**
     * Nettoie le canvas de la surface de dessin et dessine le rectangle
     * @param rectangle Le rectangle
     * @param surfaceHolderDessin Le holder de la surface de dessin
     */
    public void clearCanvasAndDrawRectangle(Rect rectangle, SurfaceHolder surfaceHolderDessin){
        Canvas c = surfaceHolderDessin.lockCanvas();
        c.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        dessinerAcces(c, listePortes);
        c.drawRect(rectangle, peintureBleue);
        surfaceHolderDessin.unlockCanvasAndPost(c);
    }

    /**
     * Dessine les zones d'acces sur la surface de dessin. <br>Nécessite que le surfaceHolderDessin soit verrouillé avant l'appel de la fonction et déverrouillé après.
     * @param listePortes La liste des portes
     * @param c Le canvas de la surface de dessin
     */
    public void dessinerAcces(Canvas c, ArrayList<Porte> listePortes){
        c.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        for(Porte porte : listePortes){
            c.drawRect(porte.getRectangle(), peintureBleue);
            int posX = (porte.getRectangle().width() / 2)+ porte.getLeft();
            int posY = (int) ((porte.getRectangle().height() / 2) - ((peintureNoire.descent() + peintureNoire.ascent()) / 2)) + porte.getTop();
            c.drawText(Integer.toString(porte.getIdPorte()), posX, posY, peintureNoire);
        }
    }

    /**
     * Valide les modifications du modèle et retourne à l'activité précédente
     * @param view la vue
     */
    public void valider(View view){
        try {
            FilesUtils.ecrireTexte(this, modele.toJSON(), path);
        } catch (IOException e) {
            Toast.makeText(this, "Erreur lors de la sauvegarde du modèle", Toast.LENGTH_SHORT).show();
        }
        Intent intent = new Intent();
        intent.putExtra("path", path);
        terminerActivite(RESULT_OK, intent);
    }

    /**
     * Annule les modifications du modèle et retourne à l'activité précédente
     * @param view la vue
     */
    public void annuler(View view){
        setResult(RESULT_CANCELED);
        finish();
    }


    /**
     * Termine l'activité en envoyant un intent avec le code de retour et les données
     * @param resultCode Le code de retour
     * @param data Les données. Peut être null
     */
    public void terminerActivite(int resultCode, Intent data){
        if(data == null){
            setResult(resultCode);
        }
        else {
            setResult(resultCode, data);
        }
        finish();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        clearCanvas(((SurfaceView)findViewById(R.id.rectangleSelection)).getHolder());
    }

    /**
     * Initialise les peintures bleu et noir.
     */
    public void creerPeintures(){
        peintureBleue = new Paint(Paint.ANTI_ALIAS_FLAG);
        peintureBleue.setColor(Color.argb(64, 0, 128, 255));
        peintureBleue.setStrokeWidth(5);
        peintureBleue.setStyle(Paint.Style.FILL_AND_STROKE);

        peintureNoire = new Paint(Paint.ANTI_ALIAS_FLAG);
        peintureNoire.setColor(Color.BLACK);
        peintureNoire.setTextSize(TAILLE_TEXTE);
        peintureNoire.setStyle(Paint.Style.FILL);
    }
}