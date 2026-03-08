
                package com.example.ancrage_avec_firebase;

        import android.os.Bundle;

        import androidx.activity.EdgeToEdge;
        import androidx.appcompat.app.AppCompatActivity;
        import androidx.core.graphics.Insets;
        import androidx.core.view.ViewCompat;
        import androidx.core.view.WindowInsetsCompat;
        import androidx.recyclerview.widget.LinearLayoutManager;
        import androidx.recyclerview.widget.RecyclerView;

        import com.example.ancrage_avec_firebase.ConseillerAdapter;
        import com.example.ancrage_avec_firebase.modele.Conseiller;

        import java.util.ArrayList;

        public class ListeConseillersActivity extends AppCompatActivity {

            private RecyclerView rvConseillers;
            private ConseillerAdapter adapter;
            private ArrayList<Conseiller> conseillers;

            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_liste_conseillers);

                rvConseillers = findViewById(R.id.rvConseillers);

                // Récupérer la liste des conseillers depuis l'Intent
                conseillers = (ArrayList<Conseiller>) getIntent().getSerializableExtra("conseillers");
                adapter = new ConseillerAdapter(conseillers);
                rvConseillers.setLayoutManager(new LinearLayoutManager(this));
                rvConseillers.setAdapter(adapter);
            }
        }