package fernandes_dos_santos_dev_mob.construction;

import android.content.Intent;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.fernandes_dos_santos_dev_mob.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * Changer d'activité pour aller à l'activité de modification de modèle
     * @param view la vue
     */
    public void changerActiviteModifierModeleActivity(View view){
        Intent intent = new Intent(this, ModifierModeleActivity.class);
        startActivity(intent);
    }

}