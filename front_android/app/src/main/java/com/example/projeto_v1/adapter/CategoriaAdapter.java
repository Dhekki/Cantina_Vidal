package com.example.projeto_v1.adapter;

import androidx.core.content.ContextCompat;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.projeto_v1.R;
import com.example.projeto_v1.model.Categoria;

import java.util.List;

public class CategoriaAdapter extends RecyclerView.Adapter<CategoriaAdapter.ViewHolder> {
    private List<Categoria> categorias;
    private OnCategoriaClickListener listener;
    private int selectedPosition = 0;

    public interface OnCategoriaClickListener {
        void onCategoriaClick(Categoria categoria);
    }

    public CategoriaAdapter(List<Categoria> categorias, OnCategoriaClickListener listener) {
        this.categorias = categorias;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CategoriaAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_categorias, parent, false);
        return new ViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriaAdapter.ViewHolder holder, int position) {
        Categoria categoria = categorias.get(position);

        holder.textNome2.setText(categoria.getNome());

        if (categorias != null && categorias.size() < 2) {
            holder.itemView.setClickable(false);
            holder.itemView.setFocusable(false);

        } else {
            holder.itemView.setClickable(true);
            holder.itemView.setFocusable(true);

            holder.itemView.setOnClickListener(v -> {
                if (listener != null) {
                    int clickedPosition = holder.getBindingAdapterPosition();

                    if (clickedPosition != RecyclerView.NO_POSITION) {
                        Categoria clickedCategoria = categorias.get(clickedPosition);
                        listener.onCategoriaClick(clickedCategoria);

                        int previousPosition = selectedPosition;
                        selectedPosition = clickedPosition;

                        notifyItemChanged(previousPosition);
                        notifyItemChanged(selectedPosition);
                    }
                }
            });
        }

        int laranjaNeon = ContextCompat.getColor(holder.itemView.getContext(), R.color.laranja_neon);
        int laranjaClaro = ContextCompat.getColor(holder.itemView.getContext(), R.color.laranja_claro);
        int corTextoBranco = ContextCompat.getColor(holder.itemView.getContext(), android.R.color.white);
        int corTextoNormal = ContextCompat.getColor(holder.itemView.getContext(), R.color.black2);

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

    public class ViewHolder extends RecyclerView.ViewHolder {
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