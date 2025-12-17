package com.example.projeto_v1.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projeto_v1.R;
import com.example.projeto_v1.adapter.PedidoAdapter;
import com.example.projeto_v1.repository.PedidosRepository;

public class PedidosClienteActivity extends AppCompatActivity {

    private RecyclerView recyclerPedidos;
    private TextView textTotalItens, textTotalPreco;
    private View layoutCancelarPedido;
    private View layoutContainerGeral; // Para esconder se estiver vazio
    private ImageView btnVoltar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedidos_cliente);

        inicializarViews();
        verificarPedidoAtivo();

        btnVoltar.setOnClickListener(v -> {
            Intent intent = new Intent(PedidosClienteActivity.this, HomeClienteActivity.class);
            startActivity(intent);
            finish();
        });

        layoutCancelarPedido.setOnClickListener(v -> {
            // Lógica: Cancelar pedido esvazia o pedido ativo
            PedidosRepository.getInstance().cancelarPedido();
            Toast.makeText(this, "Pedido cancelado.", Toast.LENGTH_SHORT).show();
            verificarPedidoAtivo(); // Atualiza a tela para vazio
        });
    }

    private void inicializarViews() {
        recyclerPedidos = findViewById(R.id.recycler_pedidos2);
        textTotalItens = findViewById(R.id.text_total_itens2);
        textTotalPreco = findViewById(R.id.text_total_preco2);
        layoutCancelarPedido = findViewById(R.id.layout_confirmar_pedido); // Reutilizando ID ou crie um layout_cancelar
        btnVoltar = findViewById(R.id.btn_voltar);
        // Sugestão: No XML mude o texto do botão para "Cancelar Pedido" e a cor para Vermelho se possível
    }

    private void verificarPedidoAtivo() {
        if (!PedidosRepository.getInstance().hasPedidoAtivo()) {
            // Se não tem pedido, esconde a lista ou mostra msg "Sem pedidos"
            recyclerPedidos.setAdapter(null);
            textTotalItens.setText("0 itens");
            textTotalPreco.setText("R$ 0,00");
            layoutCancelarPedido.setVisibility(View.GONE); // Esconde botão cancelar
        } else {
            layoutCancelarPedido.setVisibility(View.VISIBLE);
            configurarRecyclerView();
            atualizarResumo();
        }
    }

    private void configurarRecyclerView() {
        PedidoAdapter adapter = new PedidoAdapter(PedidosRepository.getInstance().getPedidoAtual(), null);
        recyclerPedidos.setLayoutManager(new LinearLayoutManager(this));
        recyclerPedidos.setAdapter(adapter);
    }

    private void atualizarResumo() {
        double total = PedidosRepository.getInstance().getTotalPedido();
        int qtd = 0;
        for (var p : PedidosRepository.getInstance().getPedidoAtual()) qtd += p.getQuantidade();

        textTotalItens.setText("Total " + qtd + " itens");
        textTotalPreco.setText(String.format("R$ %.2f", total));
    }

    // Métodos de navegação da BottomBar (Home, Pedidos, Menu...)
    public void goNavegacaoHome(View view) {
        startActivity(new Intent(this, HomeClienteActivity.class));
        finish();
    }
}