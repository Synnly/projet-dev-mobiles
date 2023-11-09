package fernandes_dos_santos_dev_mob.construction;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.fernandes_dos_santos_dev_mob.R;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fernandes_dos_santos_dev_mob.donnees.Modele;

public class MainActivity extends AppCompatActivity {

    private Modele modele;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        modele = new Modele("Modele 1");
    }

    /**
     * Changer d'activité pour aller à l'activité de modification de modèle
     * @param view la vue
     */
    public void changerActiviteModifierModeleActivity(View view){
        Intent intent = new Intent(this, ModifierModeleActivity.class);
        intent.putExtra("modele", modele.toJSON());
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            String modeleJSON = data.getStringExtra("modele");
            try {
                Log.i("System.out", modeleJSON);
                modele = new ObjectMapper().readValue(modeleJSON, Modele.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
    }

}