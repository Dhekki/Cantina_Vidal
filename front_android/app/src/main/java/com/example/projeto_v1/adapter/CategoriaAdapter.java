package com.example.projeto_v1.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                // Notifica a Activity com o objeto Categoria clicado
                listener.onCategoriaClick(categoria);

                // 4. Atualiza o estado visual (Seleciona o novo item)
                int previousPosition = selectedPosition;
                selectedPosition = position;

                // Notifica o Adapter para redesenhar a cor:
                notifyItemChanged(previousPosition);
                notifyItemChanged(selectedPosition);
            }
        });

        if (position == selectedPosition) {
            holder.textNome2.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.laranja_neon));
        } else {
            // Aplica a cor de 'Não Selecionado' (laranja_claro)
            holder.textNome2.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.laranja_claro));
        }
    }

    @Override
    public int getItemCount() {
        return categorias != null ? categorias.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageCategoria;
        TextView textNome2;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageCategoria = itemView.findViewById(R.id.imageCategoria);
            textNome2 = itemView.findViewById(R.id.textCategoriaNome);
        }
    }
}
