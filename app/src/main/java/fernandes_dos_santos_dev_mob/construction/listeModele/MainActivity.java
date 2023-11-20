package fernandes_dos_santos_dev_mob.construction.listeModele;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.fernandes_dos_santos_dev_mob.R;
import com.fasterxml.jackson.databind.ObjectMapper;
import fernandes_dos_santos_dev_mob.construction.modifierModele.ModifierModeleActivity;
import fernandes_dos_santos_dev_mob.donnees.FabriqueIDs;
import fernandes_dos_santos_dev_mob.donnees.Modele;
import java.io.*;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Modele> listeModeles;
    private RecyclerView recyclerView;
    private int indiceModele;
    private ArrayList<String> cheminsModeles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        listeModeles = new ArrayList<>();
        cheminsModeles = new ArrayList<>();
        if(new File(this.getFilesDir(), "chemins.txt").exists()){
            chargerChemins();
            chargerModeles();
        }

        resetFabriqueIdModeles();
        creerRecyclerView();
    }

    /**
     * Change d'activité pour aller à l'activité de modification de modèle. Envoie un intent avec l'URI du modèle à modifier
     * @param indice L'indice du modèle à modifier
     */
    public void changerActiviteModifierModeleActivity(int indice){
        indiceModele = indice;
        System.out.println(indice);
        Intent intent = new Intent(this, ModifierModeleActivity.class);
        intent.putExtra("path", cheminsModeles.get(indice));
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        resetFabriqueIdModeles();
        if(resultCode == RESULT_OK) {
            cheminsModeles.set(indiceModele, data.getStringExtra("path"));
            enregistrerChemins();
            chargerChemins();
            chargerModeles();
            creerRecyclerView();
        }
    }

    /**
     * Créer le RecyclerView des modèles. Reinitialise le compteur d'ids de modeles
     */
    public void creerRecyclerView(){
        recyclerView = findViewById(R.id.recyclerViewModeles);
        RecyclerView.Adapter<ModeleAdapter.ModeleViewHolder> ModelesAdapter = new ModeleAdapter(listeModeles);
        recyclerView.setAdapter(ModelesAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        resetFabriqueIdModeles();
    }

    /**
     * Crée un nouveau modele
     * @param view La vue
     */
    public void nouveauModele(View view){
        this.listeModeles.add(new Modele());
        this.cheminsModeles.add(null);
        creerRecyclerView();
    }

    /**
     * Enregistre les chemins des modèles dans le stockage privé de l'application
     */
    public void enregistrerChemins(){
        try {
            OutputStreamWriter osw = new OutputStreamWriter(this.openFileOutput("chemins.txt", Context.MODE_PRIVATE));
            osw.write(cheminsString());
            osw.close();
        }
        catch (IOException ignored){}
    }

    /**
     * Charge les chemins des modèles stockés dans le stockage privé de l'application dans la liste cheminsModeles
     */
    public void chargerChemins() {
        ArrayList<String> chemins = new ArrayList<>();
        try {
            InputStream is = this.openFileInput("chemins.txt");

            if (is != null) {
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);
                String buffer = "";

                while ((buffer = br.readLine()) != null) {
                    chemins.add(buffer);
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

    /**
     * Charge les modèles stockés dans le stockage privé de l'application à partir des chemins stockés dans le fichier chemins.txt
     */
    public void chargerModeles(){
        ArrayList<Modele> modeles = new ArrayList<>();
        for(String path : cheminsModeles){
            try {
                InputStream is = this.openFileInput(path);

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

    /**
     * Convertit les chemins des modèles en une chaine de caractères unique séparée par des retours à la ligne (\n)
     * @return La chaine de caractères
     */
    public String cheminsString(){
        StringBuilder chemins = new StringBuilder();
        for(String chemin : cheminsModeles){
            chemins.append(chemin);
            chemins.append("\n");
        }
        return chemins.toString();
    }

    /**
     * Réinitialise le compteur d'ids de modeles au nombre de modeles

     */
    public void resetFabriqueIdModeles(){
        FabriqueIDs.getinstance().initIDModele(listeModeles.size());
    }

}