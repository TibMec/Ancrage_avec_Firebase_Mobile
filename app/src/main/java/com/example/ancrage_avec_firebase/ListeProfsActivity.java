package com.example.ancrage_avec_firebase;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import com.example.ancrage_avec_firebase.modele.Prof;

public class ListeProfsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProfAdapter profAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_profs);

        recyclerView = findViewById(R.id.recyclerProfs);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Récupérer la liste de profs depuis l'intent
        ArrayList<Prof> profs = (ArrayList<Prof>) getIntent().getSerializableExtra("profs");

        profAdapter = new ProfAdapter(profs != null ? profs : new ArrayList<>());
        recyclerView.setAdapter(profAdapter);
    }
}