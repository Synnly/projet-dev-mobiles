package com.example.visualisation.activities.listeModeles;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.R;
import com.example.visualisation.activities.visualisation.VisualiserActivity;
import com.example.visualisation.donnees.Modele;
import com.example.visualisation.filesUtils.FilesUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ParcelFileDescriptor pfd;
    private ArrayList<String> chemins;
    private ArrayList<Modele> listeModeles;
    private RecyclerView recyclerView;
    private static final int REQUEST_CHEMINS = 0;
    private static final int REQUEST_FICHIER = 1;
    private static final String URI_FICHIER_CHEMINS = "content://com.example.fernandes_dos_santos_dev_mob.fileprovider/modeles/chemins.txt";
    private static final String URI_DOSSIER_JSON = "content://com.example.fernandes_dos_santos_dev_mob.fileprovider/modeles/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        chemins = new ArrayList<>();
        listeModeles = new ArrayList<>();
        chargerChemins();
        creerRecyclerView();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {

            Uri returnUri = data.getData();
            try {
                pfd = getContentResolver().openFileDescriptor(returnUri, "r");
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }

            FileDescriptor fd = pfd.getFileDescriptor();
            InputStream is = new FileInputStream(fd);

            switch(requestCode) {
                case REQUEST_CHEMINS: { // On récupère les chemins des fichiers JSON
                    ArrayList<String> cheminsFichier = new ArrayList<>();
                    try {
                        InputStreamReader isr = new InputStreamReader(is);
                        BufferedReader br = new BufferedReader(isr);
                        String buffer = "";
                        while ((buffer = br.readLine()) != null) {
                            cheminsFichier.add(buffer);
                        }
                        is.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    chemins = cheminsFichier;
                    chargerJSON();
                    break;
                }

                case REQUEST_FICHIER:{ // On récupère les fichiers JSON
                    Modele m = chargerModele(fd);
                    listeModeles.add(m);
                    try {
                        FilesUtils.ecrireTexte(this, m.toJSON(), m.getNomModele()+".json"); // Enregistrement dans le stockage interne

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    creerRecyclerView();
                }
            }
        }
    }

    /**
     * Demande à l'application construction les différents modeles et les charge dans la liste des chemins
     */
    public void chargerJSON(){
        for(String chemin : chemins) {
            Intent requestFileIntent = new Intent(Intent.ACTION_PICK);
            requestFileIntent.setPackage("com.example.fernandes_dos_santos_dev_mob");
            requestFileIntent.setDataAndType(Uri.parse(URI_DOSSIER_JSON + chemin), "text/json");
            startActivityForResult(requestFileIntent, REQUEST_FICHIER);
        }
    }

    /**
     * Demande à l'application construction le fichier contenant les chemins
     */
    public void chargerChemins(){
        Intent requestFileIntent = new Intent(Intent.ACTION_PICK);
        requestFileIntent.setPackage("com.example.fernandes_dos_santos_dev_mob");
        requestFileIntent.setDataAndType(Uri.parse(URI_FICHIER_CHEMINS), "text/plain");
        startActivityForResult(requestFileIntent, REQUEST_CHEMINS);
    }

    /**
     * Charge le modèle décris par le descripteur fd
     * @param fd Le descripteur de fichier
     * @return Le modèle
     */
    public Modele chargerModele(FileDescriptor fd){
        InputStream is = new FileInputStream(fd);

        try {
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder builder = new StringBuilder();
            String buffer = "";
            while ((buffer = br.readLine()) != null) {
                builder.append(buffer);
                builder.append("\n");
            }
            is.close();
            return new ObjectMapper().readValue(builder.toString(), Modele.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
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
    }

    public void changerActiviteVisualiserActivity(int indice){
        Intent intent = new Intent(this, VisualiserActivity.class);
        intent.putExtra("path", listeModeles.get(indice).getNomModele()+".json");
        startActivity(intent);
    }
}