package com.example.ancrage_avec_firebase;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.ancrage_avec_firebase.modele.Conseiller;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class ProfActivity extends AppCompatActivity {

    private TextView tvProfEmail;
    private Button btnUpdateInfo, btnDeleteAccount, btnVoirConseiller;
    private String userEmail;
    private FirebaseAuth mAuth;
    private DatabaseReference dbRef;

    private static final String TAG = "ProfActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_prof);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tvProfEmail = findViewById(R.id.tvProfEmail);
        btnUpdateInfo = findViewById(R.id.btnUpdateInfo);
        btnDeleteAccount = findViewById(R.id.btnDeleteAccount);
        btnVoirConseiller = findViewById(R.id.btnVoirConseiller);

        mAuth = FirebaseAuth.getInstance();
        dbRef = FirebaseDatabase.getInstance().getReference();

        userEmail = getIntent().getStringExtra("email");
        tvProfEmail.setText("Email : " + userEmail);

        Log.d(TAG, "ProfActivity ouvert pour email: " + userEmail);

        btnUpdateInfo.setOnClickListener(v -> updateInfo());
        btnDeleteAccount.setOnClickListener(v -> confirmDeleteAccount());
        btnVoirConseiller.setOnClickListener(v -> voirConseiller());

        Button btnLogout = findViewById(R.id.btnLogout);

        btnLogout.setOnClickListener(v -> {

            FirebaseAuth.getInstance().signOut();

            Intent intent = new Intent(ProfActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

        });
    }

    private void updateInfo() {
        Toast.makeText(this, "Ici tu pourrais ouvrir un formulaire pour modifier les infos", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "UpdateInfo clicked");
    }

    private void confirmDeleteAccount() {
        new AlertDialog.Builder(this)
                .setTitle("Supprimer le compte")
                .setMessage("Voulez-vous vraiment supprimer votre compte ?")
                .setPositiveButton("Oui", (dialog, which) -> deleteAccount())
                .setNegativeButton("Non", null)
                .show();
    }

    private void deleteAccount() {
        Log.d(TAG, "Suppression du compte pour: " + userEmail);
        mAuth.getCurrentUser().delete()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        // Supprimer dans Realtime DB
                        dbRef.child("profs").orderByChild("courriel").equalTo(userEmail)
                                .get().addOnCompleteListener(t -> {
                                    if(t.isSuccessful() && t.getResult().exists()){
                                        for(DataSnapshot snap : t.getResult().getChildren()) {
                                            snap.getRef().removeValue();
                                        }
                                    }
                                    Toast.makeText(this, "Compte supprimé", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(this, MainActivity.class));
                                    finish();
                                });
                    } else {
                        Toast.makeText(this, "Erreur suppression : " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        Log.e(TAG, "Erreur suppression FirebaseAuth", task.getException());
                    }
                });
    }

    private void voirConseiller() {
        // Récupérer les codes matières du prof connecté
        dbRef.child("profs").orderByChild("courriel").equalTo(userEmail)
                .get().addOnCompleteListener(taskProf -> {
                    if(taskProf.isSuccessful() && taskProf.getResult().exists()) {
                        DataSnapshot profSnap = taskProf.getResult().getChildren().iterator().next();
                        List<String> codes = (List<String>) profSnap.child("codesMatieres").getValue();
                        final List<String> codesProf = (codes != null) ? codes : new ArrayList<>();

                        // Récupérer tous les conseillers
                        dbRef.getDatabase().getReference("conseillers")
                                .get().addOnCompleteListener(taskCons -> {
                                    if(taskCons.isSuccessful() && taskCons.getResult().exists()){
                                        ArrayList<Conseiller> conseillersTrouves = new ArrayList<>();

                                        for(DataSnapshot snap : taskCons.getResult().getChildren()){
                                            Conseiller c = snap.getValue(Conseiller.class);
                                            if(c != null && c.codesMatieres != null){
                                                for(String code : c.codesMatieres){
                                                    if(codesProf.contains(code)){
                                                        conseillersTrouves.add(c);
                                                        break;
                                                    }
                                                }
                                            }
                                        }

                                        if(conseillersTrouves.isEmpty()){
                                            Toast.makeText(this, "Aucun conseiller avec code en commun", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Intent intent = new Intent(this, ListeConseillersActivity.class);
                                            intent.putExtra("conseillers", conseillersTrouves);
                                            startActivity(intent);
                                        }

                                    } else {
                                        Toast.makeText(this, "Erreur récupération conseillers", Toast.LENGTH_SHORT).show();
                                    }
                                });

                    } else {
                        Toast.makeText(this, "Erreur récupération codes prof", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}