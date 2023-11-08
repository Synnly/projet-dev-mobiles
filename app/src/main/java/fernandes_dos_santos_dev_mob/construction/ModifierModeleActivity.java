package fernandes_dos_santos_dev_mob.construction;

import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.fernandes_dos_santos_dev_mob.R;
import fernandes_dos_santos_dev_mob.donnees.Modele;
import fernandes_dos_santos_dev_mob.donnees.Piece;

public class ModifierModeleActivity extends AppCompatActivity {

    private Modele modele, modeleEnModification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifier_modele);

        modele = new Modele("Modele 1");
        modeleEnModification = new Modele(modele);

        ((TextView) findViewById(R.id.texteNomModele)).setText(modeleEnModification.getNom());

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
}