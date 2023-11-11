package fernandes_dos_santos_dev_mob.construction;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.fernandes_dos_santos_dev_mob.R;
import fernandes_dos_santos_dev_mob.donnees.Mur;
import fernandes_dos_santos_dev_mob.donnees.Piece;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class PieceAdapter extends RecyclerView.Adapter<PieceAdapter.PieceViewHolder>{

    private ArrayList<Piece> listePieces;

    public static class PieceViewHolder extends RecyclerView.ViewHolder {
        private Piece piece;
        private ImageView nord, est, sud, ouest;
        private Button bouttonNord, bouttonEst, bouttonSud, bouttonOuest;
        private TextView nomPiece;

        public PieceViewHolder(View view) {
            super(view);
            nord = view.findViewById(R.id.imageNord);
            est = view.findViewById(R.id.imageEst);
            sud = view.findViewById(R.id.imageSud);
            ouest = view.findViewById(R.id.imageOuest);
            nomPiece = view.findViewById(R.id.texteNomPiece);

            bouttonNord = view.findViewById(R.id.boutonPhotoNord);
            bouttonEst = view.findViewById(R.id.boutonPhotoEst);
            bouttonSud = view.findViewById(R.id.boutonPhotoSud);
            bouttonOuest = view.findViewById(R.id.boutonPhotoOuest);

            nomPiece.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void afterTextChanged(Editable s) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(piece != null) {
                        piece.setNomPiece(s.toString());
                    }
                }
            });
            creerEcouteurButton(bouttonNord, Mur.NORD);
            creerEcouteurButton(bouttonEst, Mur.EST);
            creerEcouteurButton(bouttonSud, Mur.SUD);
            creerEcouteurButton(bouttonOuest, Mur.OUEST);
        }

        public void setPiece(Piece p){
            this.piece = p;
        }

        public ImageView getNord() {
            return nord;
        }

        public ImageView getEst() {
            return est;
        }

        public ImageView getSud() {
            return sud;
        }

        public ImageView getOuest() {
            return ouest;
        }

        public TextView getNomPiece() {
            return nomPiece;
        }

        public void creerEcouteurButton(View view, int orientation){
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((ModifierModeleActivity) view.getContext()).prendrePhotoMur(getAdapterPosition(), orientation);
                }
            });
        }
    }

    public PieceAdapter(ArrayList<Piece> listePieces){
        this.listePieces = listePieces;
    }

    @Override
    public PieceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ligne_piece, parent, false);
        return new PieceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PieceAdapter.PieceViewHolder holder, int position) {
        holder.getNomPiece().setText(listePieces.get(position).getNomPiece());
        holder.setPiece(listePieces.get(position));
        if(listePieces.get(position).getMur(Mur.NORD) != null){
            holder.getNord().setImageBitmap(listePieces.get(position).getMur(Mur.NORD).getImage());
        }
        if(listePieces.get(position).getMur(Mur.EST) != null){
            holder.getEst().setImageBitmap(listePieces.get(position).getMur(Mur.EST).getImage());
        }
        if(listePieces.get(position).getMur(Mur.SUD) != null){
            holder.getSud().setImageBitmap(listePieces.get(position).getMur(Mur.SUD).getImage());
        }
        if(listePieces.get(position).getMur(Mur.OUEST) != null){
            holder.getOuest().setImageBitmap(listePieces.get(position).getMur(Mur.OUEST).getImage());
        }
    }

    @Override
    public int getItemCount() {
        return listePieces.size();
    }
}
