package fernandes_dos_santos_dev_mob.construction.modifierModele;

import android.graphics.drawable.BitmapDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.fernandes_dos_santos_dev_mob.R;
import fernandes_dos_santos_dev_mob.donnees.Mur;
import fernandes_dos_santos_dev_mob.donnees.Piece;

import java.util.ArrayList;

public class PieceAdapter extends RecyclerView.Adapter<PieceAdapter.PieceViewHolder>{

    private ArrayList<Piece> listePieces;

    public static class PieceViewHolder extends RecyclerView.ViewHolder {
        private Piece piece;
        private Button imageNord, imageEst, imageSud, imageOuest;
        private Button bouttonNord, bouttonEst, bouttonSud, bouttonOuest;
        private TextView nomPiece;

        public PieceViewHolder(View view) {
            super(view);
            imageNord = view.findViewById(R.id.imageNord);
            imageEst = view.findViewById(R.id.imageEst);
            imageSud = view.findViewById(R.id.imageSud);
            imageOuest = view.findViewById(R.id.imageOuest);
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
            creerEcouteurButtonPhoto(bouttonNord, Mur.NORD);
            creerEcouteurButtonPhoto(bouttonEst, Mur.EST);
            creerEcouteurButtonPhoto(bouttonSud, Mur.SUD);
            creerEcouteurButtonPhoto(bouttonOuest, Mur.OUEST);

            creerEcouteurButtonAcces(imageNord, Mur.NORD);
            creerEcouteurButtonAcces(imageEst, Mur.EST);
            creerEcouteurButtonAcces(imageSud, Mur.SUD);
            creerEcouteurButtonAcces(imageOuest, Mur.OUEST);
        }

        public void setPiece(Piece p){
            this.piece = p;
        }

        public Button getImageNord() {
            return imageNord;
        }

        public Button getImageEst() {
            return imageEst;
        }

        public Button getImageSud() {
            return imageSud;
        }

        public Button getImageOuest() {
            return imageOuest;
        }

        public TextView getNomPiece() {
            return nomPiece;
        }

        public void creerEcouteurButtonPhoto(View view, int orientation){
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((ModifierModeleActivity) view.getContext()).prendrePhotoMur(getAdapterPosition(), orientation);
                }
            });
        }

        public void creerEcouteurButtonAcces(View view, int orientation){
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((ModifierModeleActivity) view.getContext()).changerActiviteModifierAccesActivity(piece.getIdPiece(), orientation);
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
            BitmapDrawable image = new BitmapDrawable(holder.itemView.getContext().getResources(), listePieces.get(position).getMur(Mur.NORD).getImageBitmap());
            holder.getImageNord().setBackground(image);
        }
        if(listePieces.get(position).getMur(Mur.EST) != null){
            BitmapDrawable image = new BitmapDrawable(holder.itemView.getContext().getResources(), listePieces.get(position).getMur(Mur.EST).getImageBitmap());
            holder.getImageEst().setBackground(image);
        }
        if(listePieces.get(position).getMur(Mur.SUD) != null){
            BitmapDrawable image = new BitmapDrawable(holder.itemView.getContext().getResources(), listePieces.get(position).getMur(Mur.SUD).getImageBitmap());
            holder.getImageSud().setBackground(image);
        }
        if(listePieces.get(position).getMur(Mur.OUEST) != null){
            BitmapDrawable image = new BitmapDrawable(holder.itemView.getContext().getResources(), listePieces.get(position).getMur(Mur.OUEST).getImageBitmap());
            holder.getImageOuest().setBackground(image);
        }
    }

    @Override
    public int getItemCount() {
        return listePieces.size();
    }
}
