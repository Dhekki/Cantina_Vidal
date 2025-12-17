package com.example.projeto_v1.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projeto_v1.R;
import com.example.projeto_v1.model.Carrinho;
import com.example.projeto_v1.model.Produto;

import java.util.List;

public class CarrinhoAdapter extends RecyclerView.Adapter<CarrinhoAdapter.CarrinhoViewHolder> {

    private final List<Produto> listaProdutos;
    private final CartChangeListener listener;

    public interface CartChangeListener {
        void onCartChanged();
    }

    public CarrinhoAdapter(List<Produto> listaProdutos, CartChangeListener listener) {
        this.listaProdutos = listaProdutos;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CarrinhoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Usa o layout item_carrinho_produto que tem os botões +, - e lixeira
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_carrinho_produto, parent, false);
        return new CarrinhoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CarrinhoViewHolder holder, int position) {
        Produto produto = listaProdutos.get(position);

        holder.textNome.setText(produto.getNome());
        holder.textDescricao.setText(produto.getDescricao());
        holder.textPrecoUnitario.setText(String.format("R$%.2f", produto.getPreco()));
        holder.textQuantidade.setText(String.valueOf(produto.getQuantidade()));

        if (produto.getImagemResourceId() != 0) {
            holder.imageProduto.setImageResource(produto.getImagemResourceId());
        } else {
            holder.imageProduto.setImageResource(R.drawable.icon_cocacola2);
        }

        // Botão MAIS
        holder.iconMais.setOnClickListener(v -> {
            produto.setQuantidade(produto.getQuantidade() + 1);
            notifyItemChanged(holder.getAdapterPosition());
            listener.onCartChanged();
        });

        // Botão LIXEIRA (Remover)
        // Se no layout de pagamento não tiver lixeira, verifique se o ID existe antes
        if (holder.iconLixeira != null) {
            holder.iconLixeira.setOnClickListener(v -> {
                int currentPosition = holder.getAdapterPosition();
                if (currentPosition != RecyclerView.NO_POSITION) {
                    Carrinho.getInstance().removerProduto(produto);
                    notifyItemRemoved(currentPosition);
                    notifyItemRangeChanged(currentPosition, listaProdutos.size());
                    listener.onCartChanged();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return listaProdutos.size();
    }

    public static class CarrinhoViewHolder extends RecyclerView.ViewHolder {
        final ImageView imageProduto;
        final TextView textNome;
        final TextView textDescricao;
        final TextView textPrecoUnitario;
        final TextView textQuantidade;
        final ImageView iconLixeira;
        final ImageView iconMais;

        public CarrinhoViewHolder(@NonNull View itemView) {
            super(itemView);
            imageProduto = itemView.findViewById(R.id.imageProduto2); // IDs do seu XML item_carrinho_produto
            textNome = itemView.findViewById(R.id.textNome2);
            textDescricao = itemView.findViewById(R.id.textDescricao2);
            textPrecoUnitario = itemView.findViewById(R.id.textPrecoUnitario2);
            textQuantidade = itemView.findViewById(R.id.textQuantidade);
            iconLixeira = itemView.findViewById(R.id.iconLixeira); // Pode ser null no layout de pagamento se vc usar outro XML
            iconMais = itemView.findViewById(R.id.iconMais);
        }
    }
}