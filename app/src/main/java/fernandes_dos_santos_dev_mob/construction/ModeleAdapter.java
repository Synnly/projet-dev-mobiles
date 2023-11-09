package fernandes_dos_santos_dev_mob.construction;

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
        private AppCompatButton boutonModifierModele;

        public ModeleViewHolder(View view) {
            super(view);
            boutonModifierModele = view.findViewById(R.id.boutonModifierModele);
            boutonModifierModele.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity) view.getContext()).changerActiviteModifierModeleActivity(getAdapterPosition());
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
