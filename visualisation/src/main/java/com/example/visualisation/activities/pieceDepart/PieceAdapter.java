package com.example.visualisation.activities.pieceDepart;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.R;
import com.example.visualisation.donnees.Modele;
import org.jetbrains.annotations.NotNull;

public class PieceAdapter extends RecyclerView.Adapter<PieceAdapter.PieceViewHolder> {

    private Modele modele;

    public static class PieceViewHolder extends RecyclerView.ViewHolder {

        private AppCompatButton boutonPiece;

        public PieceViewHolder(View view) {
            super(view);
            boutonPiece = view.findViewById(R.id.boutonPiece);
            boutonPiece.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((ChoisirPieceDepartActivity) view.getContext()).changerActiviteVisualiserActivity(getAdapterPosition());
                }
            });
        }

        /**
         * Retourne le bouton de la pièce
         */
        public AppCompatButton getBouton(){
            return boutonPiece;
        }
    }

    public PieceAdapter(Modele m){
        this.modele = m;
    }

    @Override
    public PieceViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ligne_piece, parent, false);
        return new PieceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull PieceViewHolder holder, int position) {
        holder.getBouton().setText(modele.getListePieces().get(position).getNomPiece());
    }

    @Override
    public int getItemCount() {
        return modele.getListePieces().size();
    }


}
