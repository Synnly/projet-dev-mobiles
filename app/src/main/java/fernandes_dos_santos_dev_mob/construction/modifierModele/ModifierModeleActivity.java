package fernandes_dos_santos_dev_mob.construction.modifierModele;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.fernandes_dos_santos_dev_mob.R;
import com.fasterxml.jackson.databind.ObjectMapper;
import fernandes_dos_santos_dev_mob.construction.Utils.FilesUtils;
import fernandes_dos_santos_dev_mob.construction.camera.CameraActivity;
import fernandes_dos_santos_dev_mob.construction.modifierAcces.ModifierAccesActivity;
import fernandes_dos_santos_dev_mob.donnees.FabriqueIDs;
import fernandes_dos_santos_dev_mob.donnees.Modele;
import fernandes_dos_santos_dev_mob.donnees.Mur;
import fernandes_dos_santos_dev_mob.donnees.Piece;

import java.io.*;

public class ModifierModeleActivity extends AppCompatActivity {

    private Modele modele, modeleEnModification;
    private int indicePiecePhoto, orientationPhoto;
    private String path;
    private final static int INTENT_PRENDRE_PHOTO = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifier_modele);

        // Récupération du modèle
        path = getIntent().getStringExtra("path");
        if (path != null) {
            try {
                modele = FilesUtils.chargerModele(this, path);
            } catch (IOException e) {
                Toast.makeText(this, "Erreur lors de la lecture du modèle", Toast.LENGTH_SHORT).show();
            }
            if(modele==null){
                modele = new Modele();
            }
        }
        else {
            modele = new Modele();
            path = modele.getNomModele()+".json";
        }

        modeleEnModification = new Modele(modele);

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

        creerRecyclerView();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case INTENT_PRENDRE_PHOTO:
                if(resultCode == RESULT_OK) { // Si une photo a été prise
                    ouvrirPhoto();
                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        if(requestCode == 100){
            if(grantResults.length > 0 && grantResults[0] == getPackageManager().PERMISSION_GRANTED){
                Intent intentPhoto = new Intent(ModifierModeleActivity.this, CameraActivity.class);
                startActivityForResult(intentPhoto, 1);
            }
            else{
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
     * Ajoute une nouvelle pièce au modèle
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

            // Fermeture du fichier
            try {
                fis.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            // Mise à jour du RecyclerView
            creerRecyclerView();
        }
    }

    /**
     * Lance l'intent de prise de photo pour le mur
     * @param indice L'indice de la piece dans laquelle se trouve le mur
     * @param orientation L'orientation du mur
     */
    public void prendrePhotoMur(int indice, int orientation){
        // Demande des permissions
        if(ContextCompat.checkSelfPermission(ModifierModeleActivity.this, android.Manifest.permission.CAMERA) != getPackageManager().PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(ModifierModeleActivity.this, new String[]{android.Manifest.permission.CAMERA}, 100);
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
     * Valide les modifications du modèle et retourne à l'activité précédente
     * @param view la vue
     */
    public void valider(View view){
        modele = modeleEnModification;
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

    public void changerActiviteModifierAccesActivity(int idPiece, int orientation){
        Intent intent = new Intent(this, ModifierAccesActivity.class);
        intent.putExtra("idPiece", idPiece);
        intent.putExtra("orientation", orientation);
        intent.putExtra("path", path);
        startActivityForResult(intent, 1);
    }
}