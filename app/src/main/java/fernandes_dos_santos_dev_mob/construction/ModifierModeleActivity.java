package fernandes_dos_santos_dev_mob.construction;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import androidx.activity.result.ActivityResult;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.fernandes_dos_santos_dev_mob.R;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fernandes_dos_santos_dev_mob.donnees.Modele;
import fernandes_dos_santos_dev_mob.donnees.Piece;

public class ModifierModeleActivity extends AppCompatActivity {

    private Modele modele, modeleEnModification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifier_modele);

        // TODO : Reset la fabrique d'ids de pieces au nombre de pieces --> saut d'id (0 -> 5 -> 8 -> 13 au lieu de 0 -> 1 -> 2 ...) qd nouvelle piece cree a cause de de/serialisation du modele en json

        String modeleJSON = getIntent().getStringExtra("modele");
        Log.i("System.out", modeleJSON);
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

        // RecyclerView des pieces
        RecyclerView recyclerView = findViewById(R.id.recyclerViewPieces);
        RecyclerView.Adapter<PieceAdapter.PieceViewHolder> ContactsAdapter = new PieceAdapter(modeleEnModification.getListePieces());
        recyclerView.setAdapter(ContactsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(ModifierModeleActivity.this));
    }

    /**
     * Ajoute une nouvelle pièce au modèle
     * @param view la vue
     */
    public void nouvellePiece(View view){
        Piece piece = new Piece(modeleEnModification);

        RecyclerView recyclerView = findViewById(R.id.recyclerViewPieces);
        RecyclerView.Adapter<PieceAdapter.PieceViewHolder> ContactsAdapter = new PieceAdapter(modeleEnModification.getListePieces());
        recyclerView.setAdapter(ContactsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(ModifierModeleActivity.this));
    }

    public void annuler(View view){
        setResult(RESULT_CANCELED);
        finish();
    }

    public void valider(View view){
        modele = modeleEnModification;
        Intent intent = new Intent();
        intent.putExtra("modele", modele.toJSON());
        setResult(RESULT_OK, intent);
        finish();
    }
}