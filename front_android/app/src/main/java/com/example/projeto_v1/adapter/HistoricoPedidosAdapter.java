package com.example.projeto_v1.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projeto_v1.R;
import com.example.projeto_v1.model.Pedido;

import java.util.List;

public class HistoricoPedidosAdapter extends RecyclerView.Adapter<HistoricoPedidosAdapter.PedidoViewHolder> {

    private final List<Pedido> listaPedidos;

    public HistoricoPedidosAdapter(List<Pedido> listaPedidos) {
        this.listaPedidos = listaPedidos;
    }

    @NonNull
    @Override
    public PedidoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Infla o layout do CARD (aquele container branco)
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card_pedido, parent, false);
        return new PedidoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PedidoViewHolder holder, int position) {
        Pedido pedido = listaPedidos.get(position);

        // 1. Preenche Cabeçalho e Rodapé do Card
        if (holder.textStatus != null) holder.textStatus.setText(pedido.getStatus());

        if (holder.textTotalItens != null) {
            int qtd = pedido.getQuantidadeItens();
            holder.textTotalItens.setText("Total " + qtd + (qtd == 1 ? " item" : " itens"));
        }

        if (holder.textTotalPreco != null) {
            holder.textTotalPreco.setText(String.format("R$ %.2f", pedido.getValorTotal()));
        }

        // 2. Configura a Lista Interna de Produtos
        // Usamos o mesmo adapter, mas passamos o layout de VISUALIZAÇÃO (item_pagamentos_produto)
        CarrinhoAdapter adapterInterno = new CarrinhoAdapter(
                pedido.getProdutos(),
                R.layout.item_pagamentos_produto, // Layout que mostra "x2" e imagem pequena
                null // Null porque aqui não pode ter clique para atualizar carrinho
        );

        holder.recyclerInterno.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
        holder.recyclerInterno.setAdapter(adapterInterno);
    }

    @Override
    public int getItemCount() {
        return listaPedidos.size();
    }

    public static class PedidoViewHolder extends RecyclerView.ViewHolder {
        TextView textStatus, textTotalItens, textTotalPreco;
        RecyclerView recyclerInterno;

        public PedidoViewHolder(@NonNull View itemView) {
            super(itemView);
            // Certifique-se que esses IDs existem no 'item_card_pedido.xml'
            textStatus = itemView.findViewById(R.id.textStatusPedido);
            textTotalItens = itemView.findViewById(R.id.textTotalItensCard);
            textTotalPreco = itemView.findViewById(R.id.textTotalPrecoCard);
            recyclerInterno = itemView.findViewById(R.id.recycler_produtos_interno);
        }
    }
}