package fernandes_dos_santos_dev_mob.construction.listeModele;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.fernandes_dos_santos_dev_mob.R;
import com.fasterxml.jackson.databind.ObjectMapper;
import fernandes_dos_santos_dev_mob.construction.modifierModele.ModifierModeleActivity;
import fernandes_dos_santos_dev_mob.donnees.Modele;
import java.io.*;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Modele modele;
    private ArrayList<Modele> listeModeles;
    private RecyclerView recyclerView;
    private int indiceModele;
    private ArrayList<Uri> cheminsModeles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        listeModeles = new ArrayList<>();
        cheminsModeles = new ArrayList<>();

        // RecyclerView des modeles
        creerRecyclerView();
    }

    /**
     * Changer d'activité pour aller à l'activité de modification de modèle
     * @param indice L'indice du modèle à modifier
     */
    public void changerActiviteModifierModeleActivity(int indice){
        indiceModele = indice;
        Intent intent = new Intent(this, ModifierModeleActivity.class);
        intent.putExtra("modele", listeModeles.get(indice).toJSON());
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK) {
            Uri path = data.getParcelableExtra("path");
            cheminsModeles.set(indiceModele, path);
            enregistrerChemins();
            chargerChemins();
            chargerModeles();
            creerRecyclerView();
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

    /**
     * Crée une nouvelle pièce
     * @param view La vue
     */
    public void nouvellePiece(View view){
        this.listeModeles.add(new Modele());
        this.cheminsModeles.add(null);
        creerRecyclerView();
    }

    public void enregistrerChemins(){
        try {
            OutputStreamWriter osw = new OutputStreamWriter(this.openFileOutput("chemins.txt", Context.MODE_PRIVATE));
            osw.write(cheminsString());
            osw.close();
        }
        catch (IOException ignored){}
    }

    public void chargerChemins() {
        ArrayList<Uri> chemins = new ArrayList<>();
        try {
            InputStream is = this.openFileInput("chemins.txt");

            if (is != null) {
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);
                String buffer = "";

                while ((buffer = br.readLine()) != null) {
                    chemins.add(Uri.parse(buffer));
                }

                is.close();
                cheminsModeles = chemins;
            }
        }
        catch (FileNotFoundException e) {
            Toast.makeText(this, "Certains modèles sont introuvables", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(this, "Erreur lors de la lecture des modèles", Toast.LENGTH_SHORT).show();
        }
    }

    public void chargerModeles(){
        ArrayList<Modele> modeles = new ArrayList<>();
        for(Uri uri : cheminsModeles){
            try {
                InputStream is = getContentResolver().openInputStream(uri);

                if (is != null) {
                    InputStreamReader isr = new InputStreamReader(is);
                    BufferedReader br = new BufferedReader(isr);
                    String buffer = "";
                    StringBuilder builder = new StringBuilder();

                    while ((buffer = br.readLine()) != null) {
                        builder.append(buffer);
                    }

                    is.close();
                    modeles.add(new ObjectMapper().readValue(builder.toString(), Modele.class));
                }
            }
            catch (FileNotFoundException e) {
                Toast.makeText(this, "Certains modèles sont introuvables", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                Toast.makeText(this, "Erreur lors de la lecture des modèles", Toast.LENGTH_SHORT).show();
            }

            listeModeles = modeles;
        }
    }

    public String cheminsString(){
        StringBuilder chemins = new StringBuilder();
        for(Uri chemin : cheminsModeles){
            chemins.append(chemin.toString());
            chemins.append("\n");
        }
        return chemins.toString();
    }

}