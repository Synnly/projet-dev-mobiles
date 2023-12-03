package fernandes_dos_santos_dev_mob.activites.listeModele;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;
import com.example.fernandes_dos_santos_dev_mob.R;
import fernandes_dos_santos_dev_mob.donnees.Modele;

import java.util.ArrayList;

public class ModeleAdapter extends RecyclerView.Adapter<ModeleAdapter.ModeleViewHolder>{

    private ArrayList<Modele> listeModeles;

    public static class ModeleViewHolder extends RecyclerView.ViewHolder {

        private Modele modele;
        private AppCompatButton boutonModifierModele, boutonSupprimer;
        private boolean suppressionDemandee;

        public ModeleViewHolder(View view) {
            super(view);
            boutonModifierModele = view.findViewById(R.id.boutonModifierModele);
            boutonModifierModele.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity) view.getContext()).changerActiviteModifierModeleActivity(getAdapterPosition());
                }
            });
            boutonSupprimer = view.findViewById(R.id.boutonSupprimer);
            suppressionDemandee = false;
            boutonSupprimer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!suppressionDemandee){
                        boutonSupprimer.setText("Confirmer ?");
                        suppressionDemandee = true;
                    }
                    else{
                        ((MainActivity) view.getContext()).supprimerModele(getAdapterPosition());
                    }
                }
            });
        }

        /**
         * Modifie le modele de la ligne
         * @param m le nouveau modele
         */
        public void setModele(Modele m){
            this.modele = m;
        }

        /**
         * Retourne le bouton de la ligne
         */
        public AppCompatButton getBouton(){
            return boutonModifierModele;
        }
    }

    public ModeleAdapter(ArrayList<Modele> listeModeles){
        this.listeModeles = listeModeles;
    }

    @Override
    public ModeleAdapter.ModeleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ligne_modele, parent, false);
        return new ModeleAdapter.ModeleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ModeleAdapter.ModeleViewHolder holder, int position) {
        holder.getBouton().setText(listeModeles.get(position).getNomModele());
    }

    @Override
    public int getItemCount() {
        return listeModeles.size();
    }

}
