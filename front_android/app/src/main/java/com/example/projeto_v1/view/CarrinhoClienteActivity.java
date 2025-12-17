package com.example.projeto_v1.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projeto_v1.R;
import com.example.projeto_v1.adapter.CarrinhoAdapter;
import com.example.projeto_v1.model.Carrinho;
import com.example.projeto_v1.model.Produto;
import com.example.projeto_v1.utils.CarrinhoManager;

public class CarrinhoClienteActivity extends AppCompatActivity {

    private RecyclerView recyclerCarrinho;
    private TextView textTotalItens, textTotalPreco, textCarrinhoVazioAviso;
    private LinearLayout btnConfirmarPedido, containerCard;
    private ImageView btnVoltar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrinho_cliente);

        inicializarComponentes();
        configurarRecycler();
        atualizarResumo(); // Calcula totais iniciais

        // Botão Voltar
        btnVoltar.setOnClickListener(v -> finish());

        // Botão Confirmar (Layout clicável)
        btnConfirmarPedido.setOnClickListener(v -> {
            if (Carrinho.getInstance().getProdutos().isEmpty()) {
                Toast.makeText(this, "Adicione itens antes de continuar.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Salva estado e vai para Checkout
            CarrinhoManager.setProdutosNoCarrinho(Carrinho.getInstance().getProdutos());

            Intent intent = new Intent(CarrinhoClienteActivity.this, PagamentosClienteActivity.class);
            startActivity(intent);
        });
    }

    private void inicializarComponentes() {
        // IDs atualizados conforme seu XML
        recyclerCarrinho = findViewById(R.id.recycler_pedidos2);
        textTotalItens = findViewById(R.id.text_total_itens2);
        textTotalPreco = findViewById(R.id.text_total_preco2);
        btnConfirmarPedido = findViewById(R.id.layout_confirmar_pedido);
        btnVoltar = findViewById(R.id.btn_voltar);

        containerCard = findViewById(R.id.container_carrinho_card);
        textCarrinhoVazioAviso = findViewById(R.id.text_carrinho_vazio_aviso);
    }

    private void configurarRecycler() {
        // Adapter com Listener para atualizar totais em tempo real
        CarrinhoAdapter adapter = new CarrinhoAdapter(
                Carrinho.getInstance().getProdutos(),
                () -> {
                    // 1. Atualiza visual do carrinho
                    atualizarResumo();

                    // 2. Atualiza a Persistência (Backup)
                    // Isso é essencial para que a Home encontre os dados atualizados ao voltar
                    CarrinhoManager.setProdutosNoCarrinho(Carrinho.getInstance().getProdutos());
                }
        );

        recyclerCarrinho.setLayoutManager(new LinearLayoutManager(this));
        recyclerCarrinho.setAdapter(adapter);
    }

    private void atualizarResumo() {
        // 1. Calcula Preço Total
        double precoTotal = Carrinho.getInstance().getValorTotal();

        // 2. Calcula Quantidade Total de Itens (ex: 2 cocas + 1 bolo = 3 itens)
        int qtdTotal = 0;
        for (Produto p : Carrinho.getInstance().getProdutos()) {
            qtdTotal += p.getQuantidade();
        }

        // 3. Atualiza Textos
        textTotalPreco.setText(String.format("R$%.2f", precoTotal));
        textTotalItens.setText("Total " + qtdTotal + (qtdTotal == 1 ? " item" : " itens"));

        // 4. Verifica se ficou vazio para esconder o Card
        if (qtdTotal == 0) {
            containerCard.setVisibility(View.GONE); // Esconde o card branco
            textCarrinhoVazioAviso.setVisibility(View.VISIBLE); // Mostra aviso
        } else {
            containerCard.setVisibility(View.VISIBLE);
            textCarrinhoVazioAviso.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(recyclerCarrinho.getAdapter() != null) {
            recyclerCarrinho.getAdapter().notifyDataSetChanged();
        }
        atualizarResumo();
    }
}