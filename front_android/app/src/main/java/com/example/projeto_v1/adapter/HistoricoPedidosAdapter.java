package com.example.projeto_v1.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projeto_v1.R;
import com.example.projeto_v1.model.Categoria;
import com.example.projeto_v1.model.OrderItemResponse;
import com.example.projeto_v1.model.OrderResponse;
import com.example.projeto_v1.model.Produto;

import java.util.ArrayList;
import java.util.List;

public class HistoricoPedidosAdapter extends RecyclerView.Adapter<HistoricoPedidosAdapter.PedidoViewHolder> {

    private final List<OrderResponse> listaPedidos;
    private Context context; // Necessário para acessar Resources

    public HistoricoPedidosAdapter(List<OrderResponse> listaPedidos) {
        this.listaPedidos = listaPedidos;
    }

    @NonNull
    @Override
    public PedidoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_card_pedido, parent, false);
        return new PedidoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PedidoViewHolder holder, int position) {
        OrderResponse pedido = listaPedidos.get(position);

        // --- LÓGICA DE STATUS (TRADUÇÃO E CORES) ---
        configurarVisualStatus(holder.textStatus, pedido.getStatus());

        // 2. Resumo de Itens e Preço
        if (holder.textTotalItens != null) {
            int qtdTotal = 0;
            if (pedido.getItems() != null) {
                for (OrderItemResponse item : pedido.getItems()) {
                    qtdTotal += item.getQuantity();
                }
            }
            holder.textTotalItens.setText("Total " + qtdTotal + (qtdTotal == 1 ? " item" : " itens"));
        }

        if (holder.textTotalPreco != null) {
            holder.textTotalPreco.setText(String.format("R$ %.2f", pedido.getTotal()));
        }

        // 3. Configura a Lista Interna
        List<Produto> produtosConvertidos = converterItensParaProdutos(pedido.getItems());
        CarrinhoAdapter adapterInterno = new CarrinhoAdapter(
                produtosConvertidos,
                R.layout.item_pagamentos_produto,
                null
        );

        holder.recyclerInterno.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
        holder.recyclerInterno.setAdapter(adapterInterno);
    }

    /**
     * Método responsável por traduzir o status e definir as cores/ícones corretos.
     */
    private void configurarVisualStatus(TextView textView, String statusBackend) {
        if (textView == null || statusBackend == null) return;

        String textoTraduzido;
        int corTexto;
        int backgroundRes;
        int iconRes; // Ícone que vai na esquerda

        // Lógica Switch Case para definir estilo
        switch (statusBackend) {
            case "RECEIVED":
                textoTraduzido = context.getString(R.string.status_received);
                corTexto = ContextCompat.getColor(context, R.color.azul); // Seu padrão atual
                backgroundRes = R.drawable.shape_status_recebido; // Seu shape atual
                iconRes = R.drawable.icon_status_recebido; // Seu ícone atual
                break;

            case "IN_PREPARATION":
                textoTraduzido = context.getString(R.string.status_in_preparation);
                corTexto = ContextCompat.getColor(context, R.color.laranja); // Exemplo: Laranja para processo
                backgroundRes = R.drawable.shape_status_recebido; // Reutilizando shape (ou crie shape_status_preparo)
                iconRes = R.drawable.icon_status_recebido; // Trocar por ícone de relógio/panela se tiver
                break;

            case "DONE":
                textoTraduzido = context.getString(R.string.status_done);
                corTexto = ContextCompat.getColor(context, R.color.verde_escuro); // Verde para pronto
                backgroundRes = R.drawable.shape_status_recebido;
                iconRes = R.drawable.icon_status_recebido; // Trocar por check
                break;

            case "DELIVERED":
                textoTraduzido = context.getString(R.string.status_delivered);
                corTexto = ContextCompat.getColor(context, R.color.verde_escuro);
                backgroundRes = R.drawable.shape_status_recebido;
                iconRes = R.drawable.icon_status_recebido;
                break;

            case "CANCELLED":
                textoTraduzido = context.getString(R.string.status_cancelled);
                corTexto = ContextCompat.getColor(context, R.color.laranja_escuro);
                backgroundRes = R.drawable.shape_status_recebido;
                iconRes = R.drawable.icon_status_recebido;
                break;

            default:
                textoTraduzido = context.getString(R.string.status_unknown);
                corTexto = ContextCompat.getColor(context, R.color.cinza2);
                backgroundRes = R.drawable.shape_status_recebido;
                iconRes = R.drawable.icon_status_recebido;
                break;
        }

        // Aplica as mudanças na View
        textView.setText(textoTraduzido);
        textView.setTextColor(corTexto);

        // Se você tiver shapes diferentes para cada cor, use setBackgroundResource
        // Se usar o mesmo shape e quiser pintar dinamicamente:
        textView.setBackgroundResource(backgroundRes);
        Drawable background = textView.getBackground();
        // DrawableCompat.setTint(background, ColorUtils.setAlphaComponent(corTexto, 30)); // Opcional: Tint leve no fundo

        // Configura o Ícone à esquerda (drawableStart/Left) e pinta ele da mesma cor do texto
        Drawable icon = ContextCompat.getDrawable(context, iconRes);
        if (icon != null) {
            Drawable iconWrapped = DrawableCompat.wrap(icon);
            DrawableCompat.setTint(iconWrapped, corTexto); // Pinta o ícone com a cor do texto
            textView.setCompoundDrawablesWithIntrinsicBounds(iconWrapped, null, null, null);
        }
    }

    private List<Produto> converterItensParaProdutos(List<OrderItemResponse> itensApi) {
        List<Produto> lista = new ArrayList<>();
        if (itensApi == null) return lista;

        for (OrderItemResponse item : itensApi) {
            Produto p = new Produto(
                    item.getProductName(),
                    "",
                    item.getUnitPrice(),
                    R.drawable.icon_cocacola2,
                    new Categoria(0L, "Geral", 0)
            );
            p.setQuantidade(item.getQuantity());
            lista.add(p);
        }
        return lista;
    }

    @Override
    public int getItemCount() {
        return listaPedidos != null ? listaPedidos.size() : 0;
    }

    public static class PedidoViewHolder extends RecyclerView.ViewHolder {
        TextView textStatus, textTotalItens, textTotalPreco;
        RecyclerView recyclerInterno;

        public PedidoViewHolder(@NonNull View itemView) {
            super(itemView);
            // Mapeia os IDs do item_card_pedido.xml
            textStatus = itemView.findViewById(R.id.textStatusPedido);
            textTotalItens = itemView.findViewById(R.id.textTotalItensCard);
            textTotalPreco = itemView.findViewById(R.id.textTotalPrecoCard);
            recyclerInterno = itemView.findViewById(R.id.recycler_produtos_interno);
        }
    }
}