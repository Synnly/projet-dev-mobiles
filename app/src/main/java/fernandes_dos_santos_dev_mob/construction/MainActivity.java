package fernandes_dos_santos_dev_mob.construction;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.fernandes_dos_santos_dev_mob.R;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fernandes_dos_santos_dev_mob.donnees.Modele;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Modele modele;
    private ArrayList<Modele> listeModeles;
    private RecyclerView recyclerView;
    private int indiceModele;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listeModeles = new ArrayList<>();

        // RecyclerView des modeles
        creerRecyclerView();
    }

    /**
     * Changer d'activité pour aller à l'activité de modification de modèle
     * @param view la vue
     */
    public void changerActiviteModifierModeleActivity(int position){
        indiceModele = position;
        Intent intent = new Intent(this, ModifierModeleActivity.class);
        intent.putExtra("modele", listeModeles.get(position).toJSON());
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            String modeleJSON = data.getStringExtra("modele");
            try {
                Log.i("System.out", modeleJSON);

                modele = new ObjectMapper().readValue(modeleJSON, Modele.class); // Conversion du JSON en objet Modele
                // Modification du modèle
                listeModeles.remove(indiceModele);
                listeModeles.add(indiceModele, modele);

                creerRecyclerView();
            }
            catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Créer le RecyclerView des modèles
     */
    public void creerRecyclerView(){
        recyclerView = findViewById(R.id.recyclerViewModeles);
        RecyclerView.Adapter<ModeleAdapter.ModeleViewHolder> ModelesAdapter = new ModeleAdapter(listeModeles);
        recyclerView.setAdapter(ModelesAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
    }

    public void nouvellePiece(View view){
        this.listeModeles.add(new Modele());
        creerRecyclerView();
    }

}