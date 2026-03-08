package com.example.ancrage_avec_firebase;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    private EditText etEmail, etPassword;
    private Button btnLogin;
    private TextView tvRegisterLink;
    private FirebaseAuth mAuth;
    private DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mAuth = FirebaseAuth.getInstance();

        dbRef = FirebaseDatabase.getInstance(
                "https://ancrage-avec-firebase-default-rtdb.firebaseio.com/"
        ).getReference();

        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);
        tvRegisterLink = findViewById(R.id.tv_register_link);

        btnLogin.setOnClickListener(v -> {
            Log.d(TAG, "Login button clicked");
            loginUser();
        });

        tvRegisterLink.setOnClickListener(v -> {
            Log.d(TAG, "Register link clicked");
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    private void loginUser() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        Log.d(TAG, "Attempting login with email: " + email);

        if(email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Login failed: empty email or password");
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if(task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        Log.d(TAG, "Firebase Auth successful for user: " + email);
                        if(user != null) {
                            checkRoleAndRedirect(user.getEmail());
                        }
                    } else {
                        Log.e(TAG, "Firebase Auth failed", task.getException());
                        Toast.makeText(LoginActivity.this, "Erreur : " + task.getException().getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void checkRoleAndRedirect(String email) {
        Log.d(TAG, "Checking role for email: " + email);

        dbRef.child("profs").orderByChild("courriel").equalTo(email)
                .get().addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        DataSnapshot result = task.getResult();
                        Log.d(TAG, "Prof search result exists: " + result.exists());
                        if(result.exists()) {
                            for (DataSnapshot snap : result.getChildren()) {
                                Log.d(TAG, "Prof found: " + snap.getKey() + " -> " + snap.child("nom").getValue());
                            }
                            Intent intent = new Intent(LoginActivity.this, ProfActivity.class);
                            intent.putExtra("email", email);
                            startActivity(intent);
                            finish();
                        } else {
                            Log.d(TAG, "Prof not found, checking conseillers...");
                            dbRef.child("conseillers").orderByChild("courriel").equalTo(email)
                                    .get().addOnCompleteListener(t2 -> {
                                        if(t2.isSuccessful()) {
                                            DataSnapshot res2 = t2.getResult();
                                            Log.d(TAG, "Conseiller search result exists: " + res2.exists());
                                            if(res2.exists()) {
                                                for (DataSnapshot snap : res2.getChildren()) {
                                                    Log.d(TAG, "Conseiller found: " + snap.getKey() + " -> " + snap.child("nom").getValue());
                                                }
                                                Intent intent = new Intent(LoginActivity.this, ConseillerActivity.class);
                                                intent.putExtra("email", email);
                                                startActivity(intent);
                                                finish();
                                            } else {
                                                Log.d(TAG, "Utilisateur non trouvé dans la base de données");
                                                Toast.makeText(this, "Utilisateur non trouvé dans la base de données", Toast.LENGTH_LONG).show();
                                            }
                                        } else {
                                            Log.e(TAG, "Erreur recherche conseillers", t2.getException());
                                        }
                                    });
                        }
                    } else {
                        Log.e(TAG, "Erreur recherche profs", task.getException());
                    }
                });
    }
}