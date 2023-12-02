package com.example.visualisation.activities.pieceDepart;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.R;
import com.example.visualisation.activities.visualisation.VisualiserActivity;
import com.example.visualisation.donnees.Modele;
import com.example.visualisation.filesUtils.FilesUtils;

import java.io.IOException;

public class ChoisirPieceDepartActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Modele modele;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choisir_piece_depart);

        // Chargement du modèle
        modele = new Modele();
        String path = getIntent().getStringExtra("path");
        try {
            modele = FilesUtils.chargerModele(this, path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        creerRecyclerView();
    }


    /**
     * Créer le RecyclerView des pieces de départ
     */
    public void creerRecyclerView(){
        recyclerView = findViewById(R.id.recyclerViewPiecesDepart);
        RecyclerView.Adapter<PieceAdapter.PieceViewHolder> piecesAdapter = new PieceAdapter(modele);
        recyclerView.setAdapter(piecesAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(ChoisirPieceDepartActivity.this));
    }

    /**
     * Change l'activité pour celle de la visualisation et envoie l'indice de la pièce de départ et le chemin du modèle
     * @param indice L'indice de la pièce de départ dans la liste de pièces du modèle
     */
    public void changerActiviteVisualiserActivity(int indice){
        Intent intent = new Intent(this, VisualiserActivity.class);
        intent.putExtra("path", modele.getNomModele()+".json");
        intent.putExtra("indice", indice);
        startActivity(intent);
    }
}