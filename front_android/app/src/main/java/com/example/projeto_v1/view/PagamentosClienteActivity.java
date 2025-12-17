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
import com.example.projeto_v1.repository.PedidosRepository;
import com.example.projeto_v1.utils.CarrinhoManager;

public class PagamentosClienteActivity extends AppCompatActivity {

    private RecyclerView recyclerProdutos;
    private TextView textSubtotal, textDescontos, textTotalFinal;
    private View btnContinuar; // Referência genérica (pode ser Button ou Layout)
    private ImageView btnVoltar;
    private CarrinhoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagamentos_cliente);

        inicializarViews();

        // VALIDAÇÃO: Evita crash se o botão não foi encontrado
        if (recyclerProdutos == null || btnContinuar == null) {
            Toast.makeText(this, "Erro crítico: Botão ou Lista não encontrados.", Toast.LENGTH_LONG).show();
            return;
        }

        configurarLista();
        calcularValores();

        // Configuração do Botão Voltar
        if (btnVoltar != null) {
            btnVoltar.setOnClickListener(v -> finish());
        }

        // Configuração do Botão Continuar
        btnContinuar.setOnClickListener(v -> {
            if (Carrinho.getInstance().getProdutos() == null || Carrinho.getInstance().getProdutos().isEmpty()) {
                Toast.makeText(this, "Erro: Carrinho vazio.", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                // 1. Salva no Repositório de Pedidos
                PedidosRepository.getInstance().confirmarPedido(Carrinho.getInstance().getProdutos());

                // 2. Limpa dados globais e persistência (Isso atualiza a Home indiretamente ao limpar o Manager)
                Carrinho.getInstance().limpar();
                CarrinhoManager.limparCarrinho();

                // 3. Feedback
                Toast.makeText(this, "Pedido realizado com sucesso!", Toast.LENGTH_SHORT).show();

                // 4. Navegação para Pedidos (Limpando a pilha)
                Intent intent = new Intent(PagamentosClienteActivity.this, PedidosClienteActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Erro ao processar pedido: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void inicializarViews() {
        recyclerProdutos = findViewById(R.id.recycler_pedidos2);
        btnVoltar = findViewById(R.id.btn_voltar);

        // --- CORREÇÃO DO CRASH ---
        // Primeiro, encontramos o container do layout incluído
        View includeTotal = findViewById(R.id.layout_resumo_total);

        if (includeTotal != null) {
            // Se o include foi achado, buscamos o botão DENTRO dele
            textSubtotal = includeTotal.findViewById(R.id.textSubtotalResumo);
            textDescontos = includeTotal.findViewById(R.id.textDescontosResumo);
            textTotalFinal = includeTotal.findViewById(R.id.textTotalFinal);
            btnContinuar = includeTotal.findViewById(R.id.btnContinuarPedido);
        } else {
            // Se por algum motivo o include foi mesclado (merge), tenta buscar direto
            textSubtotal = findViewById(R.id.textSubtotalResumo);
            textDescontos = findViewById(R.id.textDescontosResumo);
            textTotalFinal = findViewById(R.id.textTotalFinal);
            btnContinuar = findViewById(R.id.btnContinuarPedido);
        }
    }

    private void configurarLista() {
        // Usa o adapter com o layout de pagamento (botões + e -)
        adapter = new CarrinhoAdapter(
                Carrinho.getInstance().getProdutos(),
                R.layout.item_pagamentos_produto, // Certifique-se que este é o nome do XML do item com +/-
                () -> {
                    // Callback: Atualiza valores na tela e salva na persistência (Home)
                    calcularValores();
                    CarrinhoManager.setProdutosNoCarrinho(Carrinho.getInstance().getProdutos());
                }
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