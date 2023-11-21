package fernandes_dos_santos_dev_mob.construction.modifierAcces;

import android.graphics.drawable.BitmapDrawable;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.fernandes_dos_santos_dev_mob.R;
import com.fasterxml.jackson.databind.ObjectMapper;
import fernandes_dos_santos_dev_mob.donnees.Modele;
import fernandes_dos_santos_dev_mob.donnees.Mur;
import fernandes_dos_santos_dev_mob.donnees.Piece;
import fernandes_dos_santos_dev_mob.donnees.Porte;

import java.io.*;
import java.util.ArrayList;

public class ModifierAccesActivity extends AppCompatActivity {

    private Modele modele;
    private Mur mur;
    private ArrayList<Porte> listePortes;
    private RecyclerView recyclerView;
    private String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifier_acces);
        path = getIntent().getStringExtra("path");
        chargerModele();
        int idPiece = getIntent().getIntExtra("idPiece", 0);
        int orientation = getIntent().getIntExtra("orientation", 0);

        // Recuperation du mur et de la liste de portes
        for (Piece p : modele.getListePieces()) {
            if (p.getIdPiece() == idPiece) {
                for (Mur m : p.getListeMurs()) {
                    if (m.getOrientation() == orientation) {
                        mur = m;
                        break;
                    }
                }
                break;
            }
        }
        listePortes = mur.getListePortes();

        BitmapDrawable image = new BitmapDrawable(getResources(), mur.getImageBitmap());
        ((ImageView) findViewById(R.id.imageMur)).setImageDrawable(image);

        creerRecyclerView();
    }

    public void creerRecyclerView() {
        recyclerView = findViewById(R.id.recyclerViewAcces);
        RecyclerView.Adapter<AccesAdapter.AccesViewHolder> accesAdapter = new AccesAdapter(listePortes, modele.getListePieces());
        recyclerView.setAdapter(accesAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(ModifierAccesActivity.this));
    }

    /**
     * Charge le modele de chemin path et le stocke dans la variable modele.<br> Affiche un toast si le modele est introuvable ou si une erreur survient lors de la lecture du fichier
     */
    public void chargerModele(){
        try {
            // Ouverture du fichier
            InputStream is = this.openFileInput(path);

            if (is != null) {
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);
                String buffer = "";
                StringBuilder builder = new StringBuilder();

                // Lecture du fichier
                while ((buffer = br.readLine()) != null) {
                    builder.append(buffer);
                }

                is.close();
                isr.close();

                // Creation du modele
                modele = new ObjectMapper().readValue(builder.toString(), Modele.class);
            }
        }
        catch (FileNotFoundException e) {
            Toast.makeText(this, "Le modele est introuvable", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(this, "Erreur lors de la lecture du modèle", Toast.LENGTH_SHORT).show();
        }
    }
}