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
    private final int layoutResId;

    public interface CartChangeListener {
        void onCartChanged();
    }

    // Construtor Carrinho (Editável)
    public CarrinhoAdapter(List<Produto> listaProdutos, CartChangeListener listener) {
        this.listaProdutos = listaProdutos;
        this.listener = listener;
        this.layoutResId = R.layout.item_carrinho_produto;
    }

    // Construtor Checkout/Pedidos (Visualização)
    public CarrinhoAdapter(List<Produto> listaProdutos, int layoutResId, CartChangeListener listener) {
        this.listaProdutos = listaProdutos;
        this.layoutResId = layoutResId;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CarrinhoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(this.layoutResId, parent, false);
        return new CarrinhoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CarrinhoViewHolder holder, int position) {
        Produto produto = listaProdutos.get(position);

        // --- BINDING GENÉRICO (Funciona para as duas telas) ---

        // Nome
        if (holder.textNome != null) {
            holder.textNome.setText(produto.getNome());
        }

        // Descrição
        if (holder.textDescricao != null) {
            holder.textDescricao.setText(produto.getDescricao());
        }

        // Preço
        if (holder.textPreco != null) {
            // Se for checkout, pode querer mostrar o total do item (qtd * preco)
            // Aqui mantive unitário, mas pode alterar para: produto.getPreco() * produto.getQuantidade()
            holder.textPreco.setText(String.format("R$%.2f", produto.getPreco()));
        }

        // Imagem
        if (holder.imageProduto != null) {
            if (produto.getImagemResourceId() != 0) {
                holder.imageProduto.setImageResource(produto.getImagemResourceId());
            } else {
                holder.imageProduto.setImageResource(R.drawable.icon_cocacola2);
            }
        }


        // --- LÓGICA ESPECÍFICA POR TELA ---

        if (layoutResId == R.layout.item_carrinho_produto) {
            // === TELA CARRINHO ===

            if (holder.textQuantidade != null) {
                holder.textQuantidade.setText(String.valueOf(produto.getQuantidade()));
            }

            // Configuração dos Botões (Mais / Menos / Lixeira)
            configurarBotoesCarrinho(holder, produto);

        } else {
            // === TELA CHECKOUT E PEDIDOS ===

            // Aqui usamos o textQuantidade que mapeia para 'textQuantidade2' no XML
            if (holder.textQuantidade != null) {
                holder.textQuantidade.setText(produto.getQuantidade() + "x");
            }

            // Garante que botões não apareçam
            if (holder.iconMais != null) holder.iconMais.setVisibility(View.GONE);
            if (holder.iconMenos != null) holder.iconMenos.setVisibility(View.GONE);
        }
    }

    private void configurarBotoesCarrinho(CarrinhoViewHolder holder, Produto produto) {
        // Botão MAIS
        if (holder.iconMais != null) {
            holder.iconMais.setVisibility(View.VISIBLE);
            holder.iconMais.setOnClickListener(v -> {
                produto.setQuantidade(produto.getQuantidade() + 1);
                notifyItemChanged(holder.getAdapterPosition());
                if (listener != null) listener.onCartChanged();
            });
        }

        // Botão MENOS / LIXEIRA
        if (holder.iconMenos != null) {
            holder.iconMenos.setVisibility(View.VISIBLE);

            // Lógica Visual (Padding e Ícone)
            float density = holder.itemView.getContext().getResources().getDisplayMetrics().density;
            int padding6dp = (int) (6 * density);

            if (produto.getQuantidade() == 1) {
                holder.iconMenos.setImageResource(R.drawable.icon_lixeira);
                holder.iconMenos.setPadding(padding6dp, padding6dp, padding6dp, padding6dp);
                holder.iconMenos.setImageTintList(null);
            } else {
                holder.iconMenos.setImageResource(R.drawable.icon_menos2);
                holder.iconMenos.setPadding(0, 0, 0, 0);
            }

            // Lógica de Clique
            holder.iconMenos.setOnClickListener(v -> {
                if (produto.getQuantidade() > 1) {
                    produto.setQuantidade(produto.getQuantidade() - 1);
                    notifyItemChanged(holder.getAdapterPosition());
                    if (listener != null) listener.onCartChanged();
                } else {
                    removerItem(holder.getAdapterPosition(), produto);
                }
            });
        }
    }

    private void removerItem(int position, Produto produto) {
        if (position != RecyclerView.NO_POSITION) {
            Carrinho.getInstance().removerProduto(produto);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, listaProdutos.size());
            if (listener != null) listener.onCartChanged();
        }
    }

    @Override
    public int getItemCount() {
        return listaProdutos.size();
    }

    // --- VIEWHOLDER ROBUSTO ---
    public static class CarrinhoViewHolder extends RecyclerView.ViewHolder {
        final ImageView imageProduto;
        final TextView textNome, textDescricao, textPreco, textQuantidade;
        final ImageView iconMais, iconMenos;

        public CarrinhoViewHolder(@NonNull View itemView) {
            super(itemView);

            // Tenta achar ID do Carrinho. Se for null, tenta achar ID do Checkout.

            // IMAGEM
            ImageView img = itemView.findViewById(R.id.imageProduto2); // Carrinho
            if (img == null) img = itemView.findViewById(R.id.imageProduto3); // Checkout
            imageProduto = img;

            // NOME
            TextView nome = itemView.findViewById(R.id.textNome2);
            if (nome == null) nome = itemView.findViewById(R.id.textNome3);
            textNome = nome;

            // DESCRIÇÃO
            TextView desc = itemView.findViewById(R.id.textDescricao2);
            if (desc == null) desc = itemView.findViewById(R.id.textDescricao3);
            textDescricao = desc;

            // PREÇO
            TextView preco = itemView.findViewById(R.id.textPrecoUnitario2);
            if (preco == null) preco = itemView.findViewById(R.id.textPrecoUnitario3);
            textPreco = preco;

            // QUANTIDADE
            TextView qtd = itemView.findViewById(R.id.textQuantidade); // Carrinho
            if (qtd == null) qtd = itemView.findViewById(R.id.textQuantidade2); // Checkout ("1x")
            textQuantidade = qtd;

            // BOTÕES (Só existem no carrinho)
            iconMais = itemView.findViewById(R.id.iconMais);
            iconMenos = itemView.findViewById(R.id.iconMenos);
        }
    }
}