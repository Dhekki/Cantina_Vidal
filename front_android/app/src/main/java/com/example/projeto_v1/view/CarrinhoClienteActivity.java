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
import com.example.projeto_v1.adapter.CarrinhoAdapter;
import com.example.projeto_v1.model.Carrinho;

public class CarrinhoClienteActivity extends AppCompatActivity {

    private RecyclerView recyclerPedidos;
    private TextView textTotalItens, textTotalPreco;
    private View layoutConfirmarPedido;
    private ImageView btnVoltar;
    private CarrinhoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrinho_cliente);

        recyclerPedidos = findViewById(R.id.recycler_pedidos2);
        textTotalItens = findViewById(R.id.text_total_itens2);
        textTotalPreco = findViewById(R.id.text_total_preco2);
        layoutConfirmarPedido = findViewById(R.id.layout_confirmar_pedido);
        btnVoltar = findViewById(R.id.btn_voltar);

        configurarRecyclerView();
        atualizarResumo();

        btnVoltar.setOnClickListener(v -> {
            Intent intent = new Intent(CarrinhoClienteActivity.this, HomeClienteActivity.class);
            startActivity(intent);
            finish();
        });

        // LÓGICA DE CONFIRMAÇÃO: Vai para Pagamentos
        layoutConfirmarPedido.setOnClickListener(v -> {
            if (Carrinho.getInstance().getProdutos().isEmpty()) {
                Toast.makeText(this, "Seu carrinho está vazio", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent(CarrinhoClienteActivity.this, PagamentosClienteActivity.class);
            startActivity(intent);
        });
    }

    private void configurarRecyclerView() {
        // Passa 'this::atualizarResumo' para atualizar os valores quando aumentar/diminuir qtd
        adapter = new CarrinhoAdapter(Carrinho.getInstance().getProdutos(), this::atualizarResumo);
        recyclerPedidos.setLayoutManager(new LinearLayoutManager(this));
        recyclerPedidos.setAdapter(adapter);
    }

    private void atualizarResumo() {
        double total = Carrinho.getInstance().getValorTotal();
        int qtd = 0;
        for(var p : Carrinho.getInstance().getProdutos()) qtd += p.getQuantidade();

        textTotalItens.setText("Total " + qtd + " itens");
        textTotalPreco.setText(String.format("R$ %.2f", total));
    }
}