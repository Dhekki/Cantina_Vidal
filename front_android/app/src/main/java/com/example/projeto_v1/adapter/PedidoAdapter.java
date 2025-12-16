package com.example.projeto_v1.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projeto_v1.R;
import com.example.projeto_v1.model.Produto;

import java.util.List;

public class PedidoAdapter extends RecyclerView.Adapter<PedidoAdapter.PedidoViewHolder> {

    private final List<Produto> listaProdutos;
    private final ResumoChangeListener listener;

    // Interface para notificar a Activity sobre mudanças no resumo (preço/itens)
    public interface ResumoChangeListener {
        void onResumoChanged();
    }

    public PedidoAdapter(List<Produto> listaProdutos, ResumoChangeListener listener) {
        this.listaProdutos = listaProdutos;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PedidoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Assume que o layout do item é item_pedidos_produto.xml (seu segundo código XML)
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pedidos_produto, parent, false);
        return new PedidoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PedidoViewHolder holder, int position) {
        Produto produto = listaProdutos.get(position);

        // 1. Preenche o Nome e a Descrição
        holder.textNome.setText(produto.getNome());
        holder.textDescricao.setText(produto.getDescricao());

        // 2. Preenche o Preço Unitário e a Quantidade
        String precoUnitarioFormatado = String.format("R$%.2f", produto.getPreco());
        holder.textPrecoUnitario.setText(precoUnitarioFormatado);

        holder.textQuantidade.setText(String.format("x%d", produto.getQuantidade()));

        // 3. Preenche a Imagem (use o resourceId do Produto)
        if (produto.getImagemResourceId() != 0) {
            holder.imageProduto.setImageResource(produto.getImagemResourceId());
        } else {
            // Caso não haja imagem definida, use um placeholder
            holder.imageProduto.setImageResource(R.drawable.icon_cocacola2);
        }

        /* // Lógica de remoção: Se você quiser um botão de remover por item:
        holder.btnRemover.setOnClickListener(v -> {
            removerProduto(position);
        });
        */
    }

    // Método auxiliar para remoção (se você for usar)
    /*
    private void removerProduto(int position) {
        Produto produtoRemovido = listaProdutos.remove(position);

        // Limpar o item do carrinho global para sincronia, se necessário
        // Carrinho.getInstance().removerProduto(produtoRemovido);

        notifyItemRemoved(position);
        notifyItemRangeChanged(position, listaProdutos.size());

        // Notifica a Activity para atualizar o Total
        if (listener != null) {
            listener.onResumoChanged();
        }
    }
    */

    @Override
    public int getItemCount() {
        return listaProdutos.size();
    }

    public static class PedidoViewHolder extends RecyclerView.ViewHolder {
        final ImageView imageProduto;
        final TextView textNome;
        final TextView textDescricao;
        final TextView textPrecoUnitario;
        final TextView textQuantidade;
        // final Button btnRemover; // Se houver um botão de remover

        public PedidoViewHolder(@NonNull View itemView) {
            super(itemView);

            // Mapeando as Views do item_pedidos_produto.xml:
            imageProduto = itemView.findViewById(R.id.imageProduto);
            textNome = itemView.findViewById(R.id.textNome);
            textDescricao = itemView.findViewById(R.id.textDescricao);
            textPrecoUnitario = itemView.findViewById(R.id.textPrecoUnitario);
            textQuantidade = itemView.findViewById(R.id.textQuantidade);
            // btnRemover = itemView.findViewById(R.id.btnRemover);
        }
    }
}