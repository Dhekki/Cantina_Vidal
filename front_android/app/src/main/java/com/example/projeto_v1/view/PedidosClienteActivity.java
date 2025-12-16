package com.example.projeto_v1.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projeto_v1.R;
import com.example.projeto_v1.adapter.PedidoAdapter;
import com.example.projeto_v1.model.Carrinho;
import com.example.projeto_v1.model.Produto;
import com.example.projeto_v1.utils.CarrinhoManager;

import java.util.List;

public class PedidosClienteActivity extends AppCompatActivity {

    private RecyclerView recyclerPedidos;
    private TextView textTotalItens, textTotalPreco;
    private View layoutCancelarPedido;

    private List<Produto> listaProdutos;
    private PedidoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pedidos_cliente);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        inicializarViews();
        configurarRecyclerView();
        // carregarProdutos(); // Removido, pois a lógica de carregamento está em configurarRecyclerView()
        atualizarResumo();
        configurarBotao();
    }

    private void inicializarViews() {
        recyclerPedidos = findViewById(R.id.recycler_pedidos);
        textTotalItens = findViewById(R.id.text_total_itens);
        textTotalPreco = findViewById(R.id.text_total_preco);
        layoutCancelarPedido = findViewById(R.id.layout_cancelar_pedido);
    }

    private void configurarRecyclerView() {
        listaProdutos = Carrinho.getInstance().getProdutos();

        adapter = new PedidoAdapter(listaProdutos, this::atualizarResumo);
        recyclerPedidos.setLayoutManager(new LinearLayoutManager(this));
        recyclerPedidos.setAdapter(adapter);
    }

    private void atualizarResumo() {
        int totalItens = 0;
        double totalPreco = 0;

        for (Produto p : listaProdutos) {
            totalItens += p.getQuantidade();
            totalPreco += p.getPreco() * p.getQuantidade();
        }

        textTotalItens.setText("Total " + totalItens + " itens");
        textTotalPreco.setText(String.format("R$ %.2f", totalPreco));
    }

    private void configurarBotao() {
        layoutCancelarPedido.setOnClickListener(v -> {
            if (listaProdutos.isEmpty()) {
                return;
            }

            // 1. Limpa o carrinho global (singleton Carrinho)
            Carrinho.getInstance().limpar();

            // 2. Limpa o estado salvo no CarrinhoManager
            CarrinhoManager.limparCarrinho();

            // 3. Notifica o adapter que a lista foi zerada
            adapter.notifyDataSetChanged();

            // 4. Atualiza a exibição do resumo para R$0,00 e 0 itens
            atualizarResumo();
        });
    }

    // Navegação inferior
    public void goNavegacaoHome(View view) {
        Intent intent = new Intent(this, HomeClienteActivity.class);
        startActivity(intent);
        finish();
    }

    public void goNavegacaoPedidos(View view) {
        // Permanece na tela atual
    }
}