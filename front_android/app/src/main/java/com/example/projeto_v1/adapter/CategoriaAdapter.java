package com.example.projeto_v1.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.projeto_v1.R;
import com.example.projeto_v1.model.Categoria;
import com.example.projeto_v1.network.RetrofitClient;

import java.util.List;

public class CategoriaAdapter extends RecyclerView.Adapter<CategoriaAdapter.ViewHolder> {
    private List<Categoria> categorias;
    private OnCategoriaClickListener listener;
    private int selectedPosition = 0;
    private Context context;

    public interface OnCategoriaClickListener {
        void onCategoriaClick(Categoria categoria);
    }

    public CategoriaAdapter(List<Categoria> categorias, OnCategoriaClickListener listener) {
        this.categorias = categorias;
        this.listener = listener;
    }

    public void atualizarLista(List<Categoria> novaLista) {
        this.categorias = novaLista;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_categorias, parent, false);
        return new ViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Categoria categoria = categorias.get(position);

        holder.textNome2.setText(categoria.getNome());

        // --- IMAGE LOADING LOGIC ---
        if (categoria.getImageUrl() != null && !categoria.getImageUrl().isEmpty()) {
            String fullUrl = RetrofitClient.BASE_IMAGE_URL + categoria.getImageUrl();
            Glide.with(context)
                    .load(fullUrl)
                    .placeholder(R.drawable.icon___bebidas) // Placeholder
                    .error(R.drawable.icon___bebidas)       // Error image
                    .into(holder.imageCategoria);
        } else {
            if (categoria.getIconeResourceId() != 0) {
                holder.imageCategoria.setImageResource(categoria.getIconeResourceId());
            } else {
                holder.imageCategoria.setImageResource(R.drawable.icon___bebidas);
            }
        }

        // --- CLICK HANDLING ---
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                int clickedPosition = holder.getBindingAdapterPosition();
                if (clickedPosition != RecyclerView.NO_POSITION) {
                    listener.onCategoriaClick(categoria);
                    int previousPosition = selectedPosition;
                    selectedPosition = clickedPosition;
                    notifyItemChanged(previousPosition);
                    notifyItemChanged(selectedPosition);
                }
            }
        });

        // --- SELECTION STYLING ---
        int laranjaNeon = ContextCompat.getColor(context, R.color.laranja_neon);
        int laranjaClaro = ContextCompat.getColor(context, R.color.laranja_claro);
        int corTextoBranco = ContextCompat.getColor(context, android.R.color.white);
        int corTextoNormal = ContextCompat.getColor(context, R.color.black2);

        if (position == selectedPosition) {
            holder.textNome2.setTextColor(corTextoBranco);
            holder.containerCategoria.setBackgroundTintList(ColorStateList.valueOf(laranjaNeon));
        } else {
            holder.textNome2.setTextColor(corTextoNormal);
            holder.containerCategoria.setBackgroundTintList(ColorStateList.valueOf(laranjaClaro));
        }
    }

    @Override
    public int getItemCount() {
        return categorias != null ? categorias.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageCategoria;
        TextView textNome2;
        LinearLayout containerCategoria;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageCategoria = itemView.findViewById(R.id.imageCategoria);
            textNome2 = itemView.findViewById(R.id.textCategoriaNome);
            containerCategoria = itemView.findViewById(R.id.containerCategoria);
        }
    }
}