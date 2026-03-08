package com.example.ancrage_avec_firebase;


import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ancrage_avec_firebase.modele.Conseiller;

import java.util.ArrayList;
import java.util.List;

public class ConseillerAdapter extends RecyclerView.Adapter<ConseillerAdapter.ViewHolder> {

    private List<Conseiller> conseillers;

    public ConseillerAdapter(ArrayList<Conseiller> conseillers){
        this.conseillers = conseillers;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_conseiller, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Conseiller c = conseillers.get(position);
        holder.nomPrenom.setText(c.nom + " " + c.prenom);
        holder.courriel.setText("📧 " + c.courriel);
        holder.tel.setText("📞 " + c.tel);
        holder.codes.setText("📚 " + String.join(", ", c.codesMatieres));

        holder.tel.setOnClickListener(v -> {

            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + c.tel));
            v.getContext().startActivity(intent);

        });
    }

    @Override
    public int getItemCount() {
        return conseillers.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView nomPrenom, courriel, tel, codes;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nomPrenom = itemView.findViewById(R.id.tvNomPrenom);
            courriel = itemView.findViewById(R.id.tvCourriel);
            tel = itemView.findViewById(R.id.tvTel);
            codes = itemView.findViewById(R.id.tvCodes);
        }
    }
}