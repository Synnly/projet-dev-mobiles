package fernandes_dos_santos_dev_mob.construction.modifierModele;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ModifierModeleActivity extends AppCompatActivity {

    private Modele modele, modeleEnModification;
    private int indicePiecePhoto, orientationPhoto;
    private ActivityResultLauncher<Intent> launcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifier_modele);
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>(){
            @Override
            public void onActivityResult(ActivityResult result) {
                Bundle extras = result.getData().getExtras();
                Bitmap bitmapPhoto = (Bitmap) extras.get("data");
            }
        });

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
        Intent intent = new Intent();
        intent.putExtra("modele", modele.toJSON());
        setResult(RESULT_OK, intent);
        finish();
    }

    public void prendrePhotoMur(int indice, int orientation){
        // Demande des permissions
        if(ContextCompat.checkSelfPermission(ModifierModeleActivity.this, android.Manifest.permission.CAMERA) != getPackageManager().PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(ModifierModeleActivity.this, new String[]{android.Manifest.permission.CAMERA}, 100);
        }
        else{
            indicePiecePhoto = indice;
            orientationPhoto = orientation;

            Intent intentPhoto = new Intent(ModifierModeleActivity.this, CameraActivity.class);
            startActivityForResult(intentPhoto, 1);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        FileInputStream fis;
        try {
            fis = openFileInput("bitmap.data");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        if(fis != null){
            Bitmap photo = BitmapFactory.decodeStream(fis);
            System.out.println(photo.getWidth()+ " " + photo.getHeight());
            Mur mur = new Mur(orientationPhoto);
            mur.setImage(photo);
            modeleEnModification.getListePieces().get(indicePiecePhoto).ajouterMur(mur);
            try {
                fis.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            creerRecyclerView();
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

    public void creerRecyclerView(){
        // RecyclerView des pieces
        RecyclerView recyclerView = findViewById(R.id.recyclerViewPieces);
        RecyclerView.Adapter<PieceAdapter.PieceViewHolder> PiecesAdapter = new PieceAdapter(modeleEnModification.getListePieces());
        recyclerView.setAdapter(PiecesAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(ModifierModeleActivity.this));
    }
}