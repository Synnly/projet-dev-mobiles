package com.example.visualisation.activities.listeModeles;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.R;
import com.example.visualisation.donnees.Modele;

import java.util.ArrayList;

public class ModeleAdapter extends RecyclerView.Adapter<ModeleAdapter.ModeleViewHolder>{

    private ArrayList<Modele> listeModeles;

    public static class ModeleViewHolder extends RecyclerView.ViewHolder {
        private Modele modele;
        private AppCompatButton boutonModifierModele;

        public ModeleViewHolder(View view) {
            super(view);
            boutonModifierModele = view.findViewById(R.id.boutonModifierModele);
            boutonModifierModele.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity) view.getContext()).changerActiviteVisualiserActivity(getAdapterPosition());
                }
            });
        }

        public void setPiece(Modele m){
            this.modele = m;
        }

        public AppCompatButton getBouton(){
            return boutonModifierModele;
        }
    }

    public ModeleAdapter(ArrayList<Modele> listeModeles){
        this.listeModeles = listeModeles;
    }

    @Override
    public ModeleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ligne_modele, parent, false);
        return new ModeleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ModeleViewHolder holder, int position) {
        holder.getBouton().setText(listeModeles.get(position).getNomModele());
    }

    @Override
    public int getItemCount() {
        return listeModeles.size();
    }

}
