package com.example.projeto_v1.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.example.projeto_v1.R;
import com.example.projeto_v1.model.Produto;

import java.util.ArrayList;
import java.util.List;

public class ProdutoAdapter extends RecyclerView.Adapter<ProdutoAdapter.ViewHolder> {

    private List<Produto> listaCompleta;
    private List<Produto> listaExibida;
    private OnProdutoQuantidadeChangeListener listener;

    public interface OnProdutoQuantidadeChangeListener {
        void onQuantidadeChanged();
    }

    public ProdutoAdapter(List<Produto> produtos, OnProdutoQuantidadeChangeListener listener) {
        this.listaCompleta = produtos;
        this.listaExibida = new ArrayList<>(produtos);
        this.listener = listener;
    }

    public void atualizarLista(List<Produto> novaLista) {
        listaExibida.clear();
        listaExibida.addAll(novaLista);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_home_produto, parent, false);
        return new ViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Produto produto = listaExibida.get(position);

        holder.textNome.setText(produto.getNome());
        holder.textPreco.setText(String.format("R$ %.2f", produto.getPreco()));
        holder.textDescricao.setText(produto.getDescricao());
        holder.textQuantidade.setText(String.valueOf(produto.getQuantidade()));

        if (produto.getImagemResourceId() != 0) {
            holder.imageProduto.setImageResource(produto.getImagemResourceId());
        }

        holder.btnAumentar.setOnClickListener(v -> {
            int quantidade = produto.getQuantidade() + 1;
            produto.setQuantidade(quantidade);
            holder.textQuantidade.setText(String.valueOf(quantidade));

            if (listener != null) {
                listener.onQuantidadeChanged();
            }
        });

        holder.btnDiminuir.setOnClickListener(v -> {
            int quantidade = produto.getQuantidade();
            if (quantidade > 0) {
                quantidade--;
                produto.setQuantidade(quantidade);
                holder.textQuantidade.setText(String.valueOf(quantidade));

                if (listener != null) {
                    listener.onQuantidadeChanged();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaExibida.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageProduto;
        TextView textNome;
        TextView textPreco;
        TextView textDescricao;
        TextView textQuantidade;
        com.google.android.material.floatingactionbutton.FloatingActionButton btnAumentar;
        com.google.android.material.floatingactionbutton.FloatingActionButton btnDiminuir;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageProduto = itemView.findViewById(R.id.imageProduto);
            textNome = itemView.findViewById(R.id.textProdutoNome);
            textPreco = itemView.findViewById(R.id.textProdutoPreco);
            textDescricao = itemView.findViewById(R.id.textProdutoDescricao);
            textQuantidade = itemView.findViewById(R.id.textQuantidade);
            btnAumentar = itemView.findViewById(R.id.btnAumentar);
            btnDiminuir = itemView.findViewById(R.id.btnDiminuir);
        }
    }
}