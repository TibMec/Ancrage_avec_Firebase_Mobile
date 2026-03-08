package com.example.ancrage_avec_firebase;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ancrage_avec_firebase.modele.Prof;
import java.util.List;
import java.util.Locale;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ProfAdapter extends RecyclerView.Adapter<ProfAdapter.ViewHolder> {

    private List<Prof> profs;

    public ProfAdapter(List<Prof> profs){
        this.profs = profs;
    }

    @Override
    public int getItemCount() {
        return profs.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_prof, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Prof p = profs.get(position);
        holder.nomPrenom.setText(p.nom + " " + p.prenom);
        holder.details.setText(
                "Email: " + p.courriel + "\n" +
                        "Écoles: " + String.join(", ", p.ecoles) + "\n" +
                        "Codes matières: " + String.join(", ", p.codesMatieres)
        );

        // Formatter la date
        String formattedDate = p.dateCreation; // exemple: "2026-02-13T19:47:58.294Z"
        try {
            SimpleDateFormat sdfInput = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
            Date date = sdfInput.parse(p.dateCreation);
            SimpleDateFormat sdfOutput = new SimpleDateFormat("dd-MM-yyyy 'à' HH:mm", Locale.getDefault());
            formattedDate = sdfOutput.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.dateCreation.setText("Créé le: " + formattedDate);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView nomPrenom, details, dateCreation;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nomPrenom = itemView.findViewById(R.id.tvNomPrenom);
            details = itemView.findViewById(R.id.tvDetails);
            dateCreation = itemView.findViewById(R.id.tvDateCreation);
        }
    }
}