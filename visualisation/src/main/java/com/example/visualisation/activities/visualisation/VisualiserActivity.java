package com.example.visualisation.activities.visualisation;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.myapplication.R;
import com.example.visualisation.donnees.Modele;
import com.example.visualisation.filesUtils.FilesUtils;

import java.io.IOException;

public class VisualiserActivity extends AppCompatActivity {

    private Modele modele;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualiser);
        String path = getIntent().getStringExtra("path");
        try {
            modele = FilesUtils.chargerModele(this, path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}