package com.example.projeto_v1.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projeto_v1.R;
import com.example.projeto_v1.adapter.CarrinhoAdapter;
import com.example.projeto_v1.model.Carrinho;
import com.example.projeto_v1.model.OrderItemRequest; // Import this
import com.example.projeto_v1.model.Produto;
import com.example.projeto_v1.utils.CarrinhoManager;
import com.example.projeto_v1.utils.SessionManager; // Import this
import com.example.projeto_v1.viewmodel.OrderViewModel; // Import this

import java.util.ArrayList;
import java.util.List;

public class PagamentosClienteActivity extends AppCompatActivity {

    private RecyclerView recyclerProdutos;
    private TextView textSubtotal, textDescontos, textTotalFinal;
    private View btnContinuar;
    private ImageView btnVoltar;
    private CarrinhoAdapter adapter;

    // Dependencies for API
    private OrderViewModel orderViewModel;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagamentos_cliente);

        sessionManager = new SessionManager(this);
        orderViewModel = new ViewModelProvider(this).get(OrderViewModel.class);

        inicializarViews();

        if (recyclerProdutos == null || btnContinuar == null) {
            Toast.makeText(this, "Erro crítico: Botão ou Lista não encontrados.", Toast.LENGTH_LONG).show();
            return;
        }

        configurarLista();
        calcularValores();

        if (btnVoltar != null) {
            btnVoltar.setOnClickListener(v -> finish());
        }

        // --- NEW LOGIC: SEND ORDER TO API ---
        btnContinuar.setOnClickListener(v -> realizarPedidoNaApi());
    }

    private void realizarPedidoNaApi() {
        if (Carrinho.getInstance().getProdutos() == null || Carrinho.getInstance().getProdutos().isEmpty()) {
            Toast.makeText(this, "Erro: Carrinho vazio.", Toast.LENGTH_SHORT).show();
            return;
        }

        String token = sessionManager.fetchAuthToken();
        if (token == null) {
            Toast.makeText(this, "Sessão expirada. Faça login novamente.", Toast.LENGTH_SHORT).show();
            // Redirect to login if needed
            return;
        }

        // 1. Prepare Request List
        List<OrderItemRequest> itemRequests = new ArrayList<>();
        for (Produto p : Carrinho.getInstance().getProdutos()) {
            // Ensure ID is valid. Assuming Produto ID is Long now based on previous steps.
            // If Produto ID is still String (UUID) locally but Long in API, this will crash.
            // Ensure your Produto.java uses Long id.
            if (p.getId() != null) {
                itemRequests.add(new OrderItemRequest(p.getId(), p.getQuantidade()));
            }
        }

        // 2. Disable button to prevent double clicks
        btnContinuar.setEnabled(false);
        Toast.makeText(this, "Enviando pedido...", Toast.LENGTH_SHORT).show();

        // 3. Call API
        orderViewModel.realizarPedido(token, itemRequests).observe(this, response -> {
            btnContinuar.setEnabled(true);

            if (response != null) {
                // SUCCESS
                Toast.makeText(this, "Pedido #" + response.getDailyId() + " realizado! Código: " + response.getPickupCode(), Toast.LENGTH_LONG).show();

                // 4. Clear Local Cart
                Carrinho.getInstance().limpar();
                CarrinhoManager.limparCarrinho();

                // 5. Navigate to Orders Screen
                Intent intent = new Intent(PagamentosClienteActivity.this, PedidosClienteActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            } else {
                // FAILURE
                Toast.makeText(this, "Falha ao criar pedido. Tente novamente.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void inicializarViews() {
        recyclerProdutos = findViewById(R.id.recycler_pedidos2);
        btnVoltar = findViewById(R.id.btn_voltar);

        View includeTotal = findViewById(R.id.layout_resumo_total);

        if (includeTotal != null) {
            textSubtotal = includeTotal.findViewById(R.id.textSubtotalResumo);
            textDescontos = includeTotal.findViewById(R.id.textDescontosResumo);
            textTotalFinal = includeTotal.findViewById(R.id.textTotalFinal);
            btnContinuar = includeTotal.findViewById(R.id.btnContinuarPedido);
        } else {
            textSubtotal = findViewById(R.id.textSubtotalResumo);
            textDescontos = findViewById(R.id.textDescontosResumo);
            textTotalFinal = findViewById(R.id.textTotalFinal);
            btnContinuar = findViewById(R.id.btnContinuarPedido);
        }
    }

    private void configurarLista() {
        adapter = new CarrinhoAdapter(
                Carrinho.getInstance().getProdutos(),
                R.layout.item_pagamentos_produto,
                null
        );
        recyclerProdutos.setLayoutManager(new LinearLayoutManager(this));
        recyclerProdutos.setAdapter(adapter);
    }

    private void calcularValores() {
        double subtotal = Carrinho.getInstance().getValorTotal();
        double descontos = 0.0;
        double total = subtotal - descontos;

        if (textSubtotal != null) textSubtotal.setText(String.format("R$ %.2f", subtotal));
        if (textDescontos != null) textDescontos.setText(String.format("-R$ %.2f", descontos));
        if (textTotalFinal != null) textTotalFinal.setText(String.format("R$ %.2f", total));
    }
}