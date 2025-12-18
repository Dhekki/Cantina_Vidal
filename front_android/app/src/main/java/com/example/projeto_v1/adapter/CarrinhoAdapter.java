package com.example.projeto_v1.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide; // <--- Importante
import com.example.projeto_v1.R;
import com.example.projeto_v1.model.Carrinho;
import com.example.projeto_v1.model.Produto;
import com.example.projeto_v1.network.RetrofitClient; // <--- Importante

import java.util.List;

public class CarrinhoAdapter extends RecyclerView.Adapter<CarrinhoAdapter.CarrinhoViewHolder> {

    private final List<Produto> listaProdutos;
    private final CartChangeListener listener;
    private final int layoutResId;
    private Context context; // <--- Necessário para o Glide

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
        this.context = parent.getContext(); // <--- Captura o contexto aqui
        View view = LayoutInflater.from(parent.getContext()).inflate(this.layoutResId, parent, false);
        return new CarrinhoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CarrinhoViewHolder holder, int position) {
        Produto produto = listaProdutos.get(position);

        // --- BINDING GENÉRICO ---

        if (holder.textNome != null) holder.textNome.setText(produto.getNome());
        if (holder.textDescricao != null) holder.textDescricao.setText(produto.getDescricao());

        if (holder.textPreco != null) {
            holder.textPreco.setText(String.format("R$%.2f", produto.getPreco()));
        }

        // --- LÓGICA DE IMAGEM (GLIDE + LOCAL) ---
        if (holder.imageProduto != null) {
            if (produto.getImageUrl() != null && !produto.getImageUrl().isEmpty()) {
                // Se tem URL da API, carrega com Glide
                String urlCompleta = RetrofitClient.BASE_IMAGE_URL + produto.getImageUrl();

                Glide.with(context)
                        .load(urlCompleta)
                        .placeholder(R.drawable.icon_cocacola2) // Placeholder
                        .error(R.drawable.icon_cocacola2)       // Erro
                        .into(holder.imageProduto);
            } else {
                // Fallback para imagem local
                if (produto.getImagemResourceId() != 0) {
                    holder.imageProduto.setImageResource(produto.getImagemResourceId());
                } else {
                    holder.imageProduto.setImageResource(R.drawable.icon_cocacola2);
                }
            }
        }

        // --- LÓGICA ESPECÍFICA POR TELA ---

        if (layoutResId == R.layout.item_carrinho_produto) {
            // === TELA CARRINHO ===
            if (holder.textQuantidade != null) {
                holder.textQuantidade.setText(String.valueOf(produto.getQuantidade()));
            }
            configurarBotoesCarrinho(holder, produto);

        } else {
            // === TELA CHECKOUT E PEDIDOS ===
            if (holder.textQuantidade != null) {
                holder.textQuantidade.setText(produto.getQuantidade() + "x");
            }
            if (holder.iconMais != null) holder.iconMais.setVisibility(View.GONE);
            if (holder.iconMenos != null) holder.iconMenos.setVisibility(View.GONE);
        }
    }

    private void configurarBotoesCarrinho(CarrinhoViewHolder holder, Produto produto) {
        if (holder.iconMais != null) {
            holder.iconMais.setVisibility(View.VISIBLE);
            holder.iconMais.setOnClickListener(v -> {
                produto.setQuantidade(produto.getQuantidade() + 1);
                notifyItemChanged(holder.getAdapterPosition());
                if (listener != null) listener.onCartChanged();
            });
        }

        if (holder.iconMenos != null) {
            holder.iconMenos.setVisibility(View.VISIBLE);

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
            notifyItemRangeChanged(position, listaProdutos.size()); // Atualiza posições
            if (listener != null) listener.onCartChanged();
        }
    }

    @Override
    public int getItemCount() {
        return listaProdutos.size();
    }

    public static class CarrinhoViewHolder extends RecyclerView.ViewHolder {
        final ImageView imageProduto;
        final TextView textNome, textDescricao, textPreco, textQuantidade;
        final ImageView iconMais, iconMenos;

        public CarrinhoViewHolder(@NonNull View itemView) {
            super(itemView);

            // Busca IDs para Carrinho ou Checkout
            ImageView img = itemView.findViewById(R.id.imageProduto2);
            if (img == null) img = itemView.findViewById(R.id.imageProduto3);
            imageProduto = img;

            TextView nome = itemView.findViewById(R.id.textNome2);
            if (nome == null) nome = itemView.findViewById(R.id.textNome3);
            textNome = nome;

            TextView desc = itemView.findViewById(R.id.textDescricao2);
            if (desc == null) desc = itemView.findViewById(R.id.textDescricao3);
            textDescricao = desc;

            TextView preco = itemView.findViewById(R.id.textPrecoUnitario2);
            if (preco == null) preco = itemView.findViewById(R.id.textPrecoUnitario3);
            textPreco = preco;

            TextView qtd = itemView.findViewById(R.id.textQuantidade);
            if (qtd == null) qtd = itemView.findViewById(R.id.textQuantidade2);
            textQuantidade = qtd;

            iconMais = itemView.findViewById(R.id.iconMais);
            iconMenos = itemView.findViewById(R.id.iconMenos);
        }
    }
}