package com.example.ancrage_avec_firebase;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.ancrage_avec_firebase.modele.Prof;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ConseillerActivity extends AppCompatActivity {

    private TextView tvConseillerEmail;
    private Button btnUpdate, btnDelete, btnVoirProfs;
    private String userEmail;
    private FirebaseAuth mAuth;
    private DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_conseiller);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tvConseillerEmail = findViewById(R.id.tvConseillerEmail);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);
        btnVoirProfs = findViewById(R.id.btnVoirProfs);

        mAuth = FirebaseAuth.getInstance();
        dbRef = FirebaseDatabase.getInstance().getReference();

        userEmail = getIntent().getStringExtra("email");
        tvConseillerEmail.setText("Email : " + userEmail);

        btnUpdate.setOnClickListener(v -> Toast.makeText(this, "Modifier vos infos", Toast.LENGTH_SHORT).show());
        btnDelete.setOnClickListener(v -> Toast.makeText(this, "Supprimer votre compte", Toast.LENGTH_SHORT).show());
        btnVoirProfs.setOnClickListener(v -> voirProfs());

        Button btnLogout = findViewById(R.id.btnLogout);

        btnLogout.setOnClickListener(v -> {

            FirebaseAuth.getInstance().signOut();

            Intent intent = new Intent(ConseillerActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

        });
    }

    private void voirProfs() {
        dbRef.child("profs").get().addOnCompleteListener(task -> {
            if(task.isSuccessful() && task.getResult().exists()){
                ArrayList<Prof> profs = new ArrayList<>();
                for(DataSnapshot snap : task.getResult().getChildren()){
                    Prof p = snap.getValue(Prof.class);
                    if(p != null){
                        profs.add(p);
                    }
                }
                Intent intent = new Intent(this, ListeProfsActivity.class);
                intent.putExtra("profs", profs);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Aucun prof trouvé", Toast.LENGTH_SHORT).show();
            }
        });
    }
}