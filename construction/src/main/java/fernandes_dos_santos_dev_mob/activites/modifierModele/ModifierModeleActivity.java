package fernandes_dos_santos_dev_mob.activites.modifierModele;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.fernandes_dos_santos_dev_mob.R;
import fernandes_dos_santos_dev_mob.activites.Utils.FilesUtils;
import fernandes_dos_santos_dev_mob.activites.camera.CameraActivity;
import fernandes_dos_santos_dev_mob.activites.modifierAcces.ModifierAccesActivity;
import fernandes_dos_santos_dev_mob.donnees.FabriqueIDs;
import fernandes_dos_santos_dev_mob.donnees.Modele;
import fernandes_dos_santos_dev_mob.donnees.Mur;
import fernandes_dos_santos_dev_mob.donnees.Piece;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ModifierModeleActivity extends AppCompatActivity {

    private Modele modele, modeleEnModification;
    private int indicePiecePhoto, orientationPhoto;
    private String modelePath, modeleTempPath;
    private final static int INTENT_PRENDRE_PHOTO = 1;
    private final static int INTENT_MODIFIER_ACCES = 2;
    private final static int REQUEST_CODE_PERMISSIONS = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifier_modele);

        // Récupération du modèle
        modelePath = getIntent().getStringExtra("path");
        modeleTempPath = "temp.json";
        if (modelePath != null) { // Le modèle existe déjà
            try {
                modele = FilesUtils.chargerModele(this, modelePath);
            } catch (IOException e) {
                Toast.makeText(this, "Erreur lors de la lecture du modèle", Toast.LENGTH_SHORT).show();
                annuler(null);
            }
            if(modele==null){
                modele = new Modele();
            }
        }
        else { // Création d'un nouveau modèle
            modele = new Modele(getIntent().getStringExtra("nom"));
            modelePath = modele.getNomModele()+".json";
        }

        modeleEnModification = new Modele(modele); // Copie du modèle pour les modifications

        // Nom du modele
        EditText nomModele = findViewById(R.id.texteNomModele);
        nomModele.setText(modele.getNomModele());
        nomModele.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                modeleEnModification.setNomModele(s.toString());
            }
        });
        try {
            FilesUtils.ecrireTexte(this, modeleEnModification.toJSON(), modeleTempPath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        creerRecyclerView();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {
            switch (requestCode) {
                case INTENT_PRENDRE_PHOTO:
                    ouvrirPhoto();
                    break;

                case INTENT_MODIFIER_ACCES:
                    try {
                        Modele modeleTemp = FilesUtils.chargerModele(this, modeleTempPath);

                        if (modeleTemp != null) {
                            modeleEnModification = new Modele(modeleTemp);
                        }
                    } catch (IOException e) {
                        Toast.makeText(this, "Erreur lors de la lecture du modèle", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (grantResults.length > 0 && grantResults[0] == getPackageManager().PERMISSION_GRANTED) { // Permission accordée donc lancement de l'activité
                Intent intentPhoto = new Intent(ModifierModeleActivity.this, CameraActivity.class);
                startActivityForResult(intentPhoto, 1);
            } else {
                Toast.makeText(ModifierModeleActivity.this, "Autorisation d'accéder à la caméra nécessaire", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Crée le RecyclerView des pièces. Reinitalise le compteur d'ids de pièces
     */
    public void creerRecyclerView(){
        RecyclerView recyclerView = findViewById(R.id.recyclerViewPieces);
        RecyclerView.Adapter<PieceAdapter.PieceViewHolder> PiecesAdapter = new PieceAdapter(modeleEnModification.getListePieces());
        recyclerView.setAdapter(PiecesAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(ModifierModeleActivity.this));

        FabriqueIDs.getinstance().initIDPiece(modeleEnModification.getListePieces().size());
    }

    /**
     * Ajoute une nouvelle pièce au modèle. Met à jour le RecyclerView
     * @param view la vue
     */
    public void nouvellePiece(View view){
        Piece piece = new Piece(modeleEnModification);
        creerRecyclerView();
    }

    /**
     * Ouvre la photo dans le fichier bitmap.data et l'ajoute au mur d'orientation orientationPhoto de la pièce d'indice indicePiecePhoto. Met à jour le RecyclerView
     */
    public void ouvrirPhoto() {
        // Ouverture du fichier
        FileInputStream fis;
        try {
            fis = openFileInput("bitmap.data");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        if (fis != null) {
            // Conversion en bitmap
            Bitmap photo = BitmapFactory.decodeStream(fis);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.PNG, 100, stream);

            // Ajout de la photo au mur
            byte[] binaireImage = stream.toByteArray();
            Mur mur = new Mur(orientationPhoto);
            mur.setBinaireImage(binaireImage);
            modeleEnModification.getListePieces().get(indicePiecePhoto).ajouterMur(mur);
            try {
                FilesUtils.ecrireTexte(this, modeleEnModification.toJSON(), modeleTempPath);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            // Fermeture du fichier
            try {
                fis.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            creerRecyclerView();
        }
    }

    /**
     * Lance l'intent de prise de photo pour le mur. Demande la permission si nécessaire
     * @param indice L'indice de la piece dans laquelle se trouve le mur
     * @param orientation L'orientation du mur
     */
    public void prendrePhotoMur(int indice, int orientation){
        // Demande des permissions
        if(ContextCompat.checkSelfPermission(ModifierModeleActivity.this, android.Manifest.permission.CAMERA) != getPackageManager().PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(ModifierModeleActivity.this, new String[]{android.Manifest.permission.CAMERA}, REQUEST_CODE_PERMISSIONS);
        }
        else{
            indicePiecePhoto = indice;
            orientationPhoto = orientation;

            Intent intentPhoto = new Intent(ModifierModeleActivity.this, CameraActivity.class);
            startActivityForResult(intentPhoto, INTENT_PRENDRE_PHOTO);
        }
    }

    /**
     * Annule les modifications du modèle et retourne à l'activité précédente
     */
    public void annuler(View view){
        setResult(RESULT_CANCELED);
        finish();
    }

    /**
     * Valide les modifications du modèle et retourne à l'activité précédente en fournissant le chemin du modèle
     * @param view la vue
     */
    public void valider(View view){
        modele = modeleEnModification;
        try {
            FilesUtils.ecrireTexte(this, modele.toJSON(), modelePath);
        } catch (IOException e) {
            Toast.makeText(this, "Erreur lors de la sauvegarde du modèle", Toast.LENGTH_SHORT).show();
        }
        Intent intent = new Intent();
        intent.putExtra("path", modelePath);
        terminerActivite(RESULT_OK, intent);
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

    /**
     * Change d'activité pour aller à l'activité de modification d'accès. Envoie un intent avec l'URI du modèle à modifier
     * @param idPiece L'id de la pièce
     * @param orientation L'orientation du mur
     */
    public void changerActiviteModifierAccesActivity(int idPiece, int orientation){
        Intent intent = new Intent(this, ModifierAccesActivity.class);
        if(modeleEnModification.getListePieces().get(idPiece).getMur(orientation) != null) {
            intent.putExtra("idPiece", idPiece);
            intent.putExtra("orientation", orientation);
            intent.putExtra("path", modeleTempPath);
            startActivityForResult(intent, INTENT_MODIFIER_ACCES);
        }
        else{
            Toast.makeText(this, "Veuillez prendre une photo du mur avant.", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Supprime la pièce de l'indice donné. Met à jour le RecyclerView
     * @param indice l'indice de la pièce
     */
    public void supprimerPiece(int indice){
        modeleEnModification.getListePieces().remove(indice);
        creerRecyclerView();
    }
}