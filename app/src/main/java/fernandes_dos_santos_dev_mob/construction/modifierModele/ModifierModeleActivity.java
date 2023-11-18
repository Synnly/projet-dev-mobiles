package fernandes_dos_santos_dev_mob.construction.modifierModele;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.DocumentsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fernandes_dos_santos_dev_mob.construction.camera.CameraActivity;
import fernandes_dos_santos_dev_mob.donnees.Modele;
import fernandes_dos_santos_dev_mob.donnees.Mur;
import fernandes_dos_santos_dev_mob.donnees.Piece;

import java.io.*;

import static com.google.android.material.internal.ContextUtils.getActivity;

public class ModifierModeleActivity extends AppCompatActivity {

    private Modele modele, modeleEnModification;
    private int indicePiecePhoto, orientationPhoto;
    private final static int INTENT_PRENDRE_PHOTO = 1;
    private final static int INTENT_ENREGISTRER_JSON = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifier_modele);

        // TODO : Reset la fabrique d'ids de pieces au nombre de pieces --> saut d'id (0 -> 5 -> 8 -> 13 au lieu de 0 -> 1 -> 2 ...) qd nouvelle piece cree a cause de de/serialisation du modele en json

        String modeleJSON = getIntent().getStringExtra("modele");
        try {
            modele = new ObjectMapper().readValue(modeleJSON, Modele.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        modeleEnModification = new Modele(modele);

        // Nom du modele
        EditText nomModele = findViewById(R.id.texteNomModele);
        nomModele.setText(modeleEnModification.getNomModele());
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

    /**
     * Ajoute une nouvelle pièce au modèle
     * @param view la vue
     */
    public void nouvellePiece(View view){
        Piece piece = new Piece(modeleEnModification);
        creerRecyclerView();
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
        String JSON = modele.toJSON();
        String path = Environment.getExternalStorageDirectory().getPath()+"/Modeles/"+modele.getNomModele()+".json";

        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/json");
        intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, path);
        intent.putExtra(Intent.EXTRA_TITLE, modele.getNomModele()+".json");

        startActivityForResult(intent, INTENT_ENREGISTRER_JSON);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case INTENT_PRENDRE_PHOTO:
                if(resultCode == RESULT_OK) { // Si une photo a été prise
                    ouvrirPhoto();
                }
                break;

            case INTENT_ENREGISTRER_JSON:
                if(resultCode == RESULT_OK){
                    ecrireJSON(data.getData(), modele.toJSON());
                    Intent intent = new Intent();
                    intent.putExtra("path", data.getData());
                    terminerActivite(RESULT_OK, intent);
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
     * Crée le RecyclerView des pièces
     */
    public void creerRecyclerView(){
        RecyclerView recyclerView = findViewById(R.id.recyclerViewPieces);
        RecyclerView.Adapter<PieceAdapter.PieceViewHolder> PiecesAdapter = new PieceAdapter(modeleEnModification.getListePieces());
        recyclerView.setAdapter(PiecesAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(ModifierModeleActivity.this));
    }

    /**
     * Ouvre la photo dans le fichier bitmap.data et l'ajoute au mur d'orientation orientationPhoto de la pièce d'indice indicePiecePhoto
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
     * Ecrit le JSON dans le fichier à l'URI
     * @param uri L'URI du fichier
     * @param json Le JSON à écrire
     */
    public void ecrireJSON(Uri uri, String json){
        Log.i("info", json);
        try {
            ParcelFileDescriptor pdf = getActivity(this).getContentResolver().openFileDescriptor(uri, "w");
            FileOutputStream fos = new FileOutputStream(pdf.getFileDescriptor());
            fos.write(json.getBytes());
            fos.close();
            pdf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void terminerActivite(int resultCode, Intent data){
        if(data == null){
            setResult(resultCode);
        }
        else {
            setResult(resultCode, data);
        }
        finish();
    }
}