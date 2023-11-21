package fernandes_dos_santos_dev_mob.construction.modifierAcces;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import androidx.recyclerview.widget.RecyclerView;
import com.example.fernandes_dos_santos_dev_mob.R;
import fernandes_dos_santos_dev_mob.donnees.Piece;
import fernandes_dos_santos_dev_mob.donnees.Porte;

import java.util.ArrayList;

public class AccesAdapter extends RecyclerView.Adapter<AccesAdapter.AccesViewHolder> {

    private ArrayList<Porte> listeAcces;
    private ArrayList<Piece> listePieces;

    public static class AccesViewHolder extends RecyclerView.ViewHolder {

        private Spinner spinner;
        private Porte porte;

        public AccesViewHolder(View view) {
            super(view);
            spinner = view.findViewById(R.id.spinnerPieces);
        }

        public void setPorte(Porte porte) {
            this.porte = porte;
        }

        public void remplirSpinner(ArrayList<String> listePieces) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(spinner.getContext(), android.R.layout.simple_spinner_item, listePieces);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
        }
    }

    public AccesAdapter(ArrayList<Porte> listeAcces, ArrayList<Piece> listePieces) {
        this.listeAcces = listeAcces;
        this.listePieces = listePieces;
    }

    @Override
    public AccesAdapter.AccesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ligne_acces, parent, false);
        return new AccesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AccesViewHolder holder, int position) {
        holder.setPorte(listeAcces.get(position));
        holder.remplirSpinner(listePiecesToStringArray(listePieces));
    }

    @Override
    public int getItemCount() {
        return listeAcces.size();
    }

    public ArrayList<String> listePiecesToStringArray(ArrayList<Piece> listePieces) {
        ArrayList<String> listePiecesString = new ArrayList<>();
        for (Piece piece : listePieces) {
            listePiecesString.add(piece.getNomPiece());
        }
        return listePiecesString;
    }

}
