package fernandes_dos_santos_dev_mob.activites.modifierAcces;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.fernandes_dos_santos_dev_mob.R;
import fernandes_dos_santos_dev_mob.donnees.Piece;
import fernandes_dos_santos_dev_mob.donnees.Porte;

import java.util.ArrayList;

public class AccesAdapter extends RecyclerView.Adapter<AccesAdapter.AccesViewHolder> {

    private ArrayList<Porte> listeAcces;
    private ArrayList<Piece> listePieces;
    private Piece pieceActuelle;

    public static class AccesViewHolder extends RecyclerView.ViewHolder {

        private Spinner spinner;
        private TextView idPorte;
        private Porte porte;
        private ArrayList<Piece> listePieces;
        private Button bouttonSupprimer;
        private boolean suppresionDemande;

        public AccesViewHolder(View view) {
            super(view);
            spinner = view.findViewById(R.id.spinnerPieces);
            spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
                @Override
                public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                    porte.setPieceArrivee(listePieces.get(position));
                }

                @Override
                public void onNothingSelected(android.widget.AdapterView<?> parent) {}
            });
            idPorte = view.findViewById(R.id.idPorte);
            bouttonSupprimer = view.findViewById(R.id.boutonSupprimer);
            suppresionDemande = false;
            bouttonSupprimer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(suppresionDemande) {
                        ((ModifierAccesActivity) v.getContext()).supprimerAcces(porte);
                    } else {
                        bouttonSupprimer.setText("Confirmer ?");
                        suppresionDemande = true;
                    }
                }
            });
        }

        /**
         * Modifie la porte de l'acces
         * @param porte la nouvelle porte
         */
        public void setPorte(Porte porte) {
            this.porte = porte;
        }

        /**
         * Modifie le texte de l'id de la porte
         * @param idPorte le nouvel id de la porte
         */
        public void setIdPorte(int idPorte) {
            this.idPorte.setText(Integer.toString(idPorte));
        }

        /**
         * Remplis la liste deroulante avec le nom des pieces et ajoute la liste des pieces en parametre
         * @param listePieces la liste des pieces
         */
        public void remplirSpinner(ArrayList<Piece> listePieces) {
            this.listePieces = listePieces;
            ArrayAdapter<String> adapter = new ArrayAdapter<>(spinner.getContext(), android.R.layout.simple_spinner_item, listePiecesToStringArray(listePieces));
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
        }

        /**
         * Transforme une liste de pieces en liste de noms de pieces
         * @param listePieces la liste de pieces
         * @return la liste de noms de pieces
         */
        public ArrayList<String> listePiecesToStringArray(ArrayList<Piece> listePieces) {
            ArrayList<String> listePiecesString = new ArrayList<>();
            for (Piece piece : listePieces) {
                listePiecesString.add(piece.getNomPiece());
            }
            return listePiecesString;
        }
    }

    public AccesAdapter(ArrayList<Porte> listeAcces, ArrayList<Piece> listePieces, Piece pieceActuelle) {
        this.listeAcces = listeAcces;
        this.listePieces = listePieces;
        this.pieceActuelle = pieceActuelle;
    }

    @Override
    public AccesAdapter.AccesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ligne_acces, parent, false);
        return new AccesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AccesViewHolder holder, int position) {
        holder.setPorte(listeAcces.get(position));
        holder.setIdPorte(listeAcces.get(position).getIdPorte());
        ArrayList<Piece> temp = new ArrayList<>(this.listePieces);
        temp.remove(pieceActuelle);
        holder.remplirSpinner(temp);
    }

    @Override
    public int getItemCount() {
        return listeAcces.size();
    }

}
