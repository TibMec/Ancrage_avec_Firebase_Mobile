package com.example.ancrage_avec_firebase;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private EditText etNom, etPrenom, etEmail, etPassword, etTel;
    private RadioGroup rgRole;
    private RadioButton rbProf, rbConseiller;
    private Button btnRegister, btnSelectEcoles, btnSelectCodes;

    private String[] ecolesList = {"Lester B Pearson High School","Lauren Hill Academy Senior Campus", "Lauren Hill Academy Junior Campus","Royal West Academy","Vincent Massey Collegiate", "Rosemount High School"};
    private boolean[] selectedEcoles;

    private String[] codesMatieresList = { "634-106","634-404","635-106", "635-206", "635-306","444-444","333-333"};
    private boolean[] selectedCodes;

    private ArrayList<String> selectedEcolesList = new ArrayList<>();
    private ArrayList<String> selectedCodesList = new ArrayList<>();
    private FirebaseAuth mAuth;
    private DatabaseReference dbRefProfs;
    private DatabaseReference dbRefConseillers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        etNom = findViewById(R.id.etNom);
        etPrenom = findViewById(R.id.etPrenom);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnSelectEcoles = findViewById(R.id.btnSelectEcoles);
        btnSelectCodes = findViewById(R.id.btnSelectCodes);

        etTel = findViewById(R.id.etTel);
        rgRole = findViewById(R.id.rgRole);
        rbProf = findViewById(R.id.rbProf);
        rbConseiller = findViewById(R.id.rbConseiller);
        btnRegister = findViewById(R.id.btnRegister);

        selectedEcoles = new boolean[ecolesList.length];
        selectedCodes = new boolean[codesMatieresList.length];

        mAuth = FirebaseAuth.getInstance();
        dbRefProfs = FirebaseDatabase.getInstance().getReference("profs");
        dbRefConseillers = FirebaseDatabase.getInstance().getReference("conseillers");

        btnRegister.setOnClickListener(v -> registerUser());

        rgRole.setOnCheckedChangeListener((group, checkedId) -> {

            if(checkedId == R.id.rbProf){
                btnSelectEcoles.setVisibility(View.VISIBLE);
                etTel.setVisibility(View.GONE);
            }
            else if(checkedId == R.id.rbConseiller){
                btnSelectEcoles.setVisibility(View.GONE);
                etTel.setVisibility(View.VISIBLE);
            }

        });

        btnSelectEcoles.setOnClickListener(v -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Choisir les écoles");

            builder.setMultiChoiceItems(ecolesList, selectedEcoles,
                    (dialog, which, isChecked) -> {

                        if(isChecked){
                            selectedEcolesList.add(ecolesList[which]);
                        } else {
                            selectedEcolesList.remove(ecolesList[which]);
                        }

                    });

            builder.setPositiveButton("OK", (dialog, which) -> {
                btnSelectEcoles.setText(selectedEcolesList.toString());
            });

            builder.setNegativeButton("Annuler", null);

            builder.show();
        });

        btnSelectCodes.setOnClickListener(v -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Choisir les codes matières");

            builder.setMultiChoiceItems(codesMatieresList, selectedCodes,
                    (dialog, which, isChecked) -> {

                        if(isChecked){
                            selectedCodesList.add(codesMatieresList[which]);
                        } else {
                            selectedCodesList.remove(codesMatieresList[which]);
                        }

                    });

            builder.setPositiveButton("OK", (dialog, which) -> {
                btnSelectCodes.setText(selectedCodesList.toString());
            });

            builder.setNegativeButton("Annuler", null);

            builder.show();
        });
    }



    private void registerUser() {
        String nom = etNom.getText().toString().trim();
        String prenom = etPrenom.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String ecoles = btnSelectEcoles.getText().toString().trim();
        String codesMatieres = btnSelectCodes.getText().toString().trim();
        String tel = etTel.getText().toString().trim();

        if(nom.isEmpty() || prenom.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Veuillez remplir tous les champs obligatoires", Toast.LENGTH_SHORT).show();
            return;
        }

        int selectedRoleId = rgRole.getCheckedRadioButtonId();
        if(selectedRoleId == -1){
            Toast.makeText(this, "Veuillez choisir un rôle", Toast.LENGTH_SHORT).show();
            return;
        }

        String role = (selectedRoleId == R.id.rbProf) ? "prof" : "conseiller";

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        FirebaseUser user = mAuth.getCurrentUser();
                        if(user != null){
                            saveUserToDatabase(user.getUid(), nom, prenom, email, ecoles, codesMatieres, tel, role);
                        }
                    } else {
                        Toast.makeText(RegisterActivity.this, "Erreur : " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void saveUserToDatabase(String uid, String nom, String prenom, String email,
                                    String ecoles, String codesMatieres, String tel, String role){

        Map<String, Object> userMap = new HashMap<>();

        userMap.put("nom", nom);
        userMap.put("prenom", prenom);
        userMap.put("courriel", email);
        userMap.put("codesMatieres", selectedCodesList);

        if(role.equals("prof")){

            userMap.put("ecoles", selectedEcolesList);
            userMap.put("dateCreation", getCurrentFormattedDate());

            dbRefProfs.child(uid).setValue(userMap).addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    Toast.makeText(this, "Compte professeur créé", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, LoginActivity.class));
                    finish();
                }
            });

        } else {

            userMap.put("tel", tel);

            dbRefConseillers.child(uid).setValue(userMap).addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    Toast.makeText(this, "Compte conseiller créé", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, LoginActivity.class));
                    finish();
                }
            });
        }
    }
    private String getCurrentFormattedDate() {
        java.text.SimpleDateFormat sdf =
                new java.text.SimpleDateFormat("dd-MM-yyyy 'à' HH:mm", java.util.Locale.getDefault());
        return sdf.format(new java.util.Date());
    }
}