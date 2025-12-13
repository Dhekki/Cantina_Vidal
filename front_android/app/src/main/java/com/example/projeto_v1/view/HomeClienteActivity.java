package com.example.projeto_v1.view;

import com.example.projeto_v1.utils.GridSpacingItemDecoration;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.projeto_v1.R;
import com.example.projeto_v1.adapter.CategoriaAdapter;
import com.example.projeto_v1.adapter.ProdutoAdapter;
import com.example.projeto_v1.model.Categoria;
import com.example.projeto_v1.model.Produto;
import java.util.ArrayList;
import java.util.List;

public class HomeClienteActivity extends AppCompatActivity implements ProdutoAdapter.OnProdutoQuantidadeChangeListener, CategoriaAdapter.OnCategoriaClickListener {

    private TextView textPrecoCarrinho;
    private TextView textTotalCarrinho;
    private Button btnCarrinho;
    private View carrinhoBarra;

    private List<Produto> produtosGeral;
    private ProdutoAdapter produtoAdapter;
    public void goNavegacaoPedidos(View view) {
        Intent intent = new Intent(HomeClienteActivity.this, PedidosClienteActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_cliente);

        carrinhoBarra = findViewById(R.id.carrinhoBarra);

        textPrecoCarrinho = carrinhoBarra.findViewById(R.id.textPrecoCarrinho);
        textTotalCarrinho = carrinhoBarra.findViewById(R.id.textTotalCarrinho);
        btnCarrinho = carrinhoBarra.findViewById(R.id.btnCarrinho);

        btnCarrinho.setOnClickListener(v -> {
            Intent intent = new Intent(HomeClienteActivity.this, PedidosClienteActivity.class);
            startActivity(intent);
        });

        setupRecyclerViewProdutos();

        setupRecyclerViewCategorias();

        //atualizarEstadoCarrinho(criarListaProdutos());

        if (produtosGeral != null) {
            atualizarEstadoCarrinho(produtosGeral);
        }
    }

    private void atualizarEstadoCarrinho(List<Produto> produtosCarrinho) {
        double precoTotal = 0.0;
        int totalItens = 0;

        for (Produto produto : produtosCarrinho) {
            if (produto.getQuantidade() > 0) {
                precoTotal += (produto.getPreco() * produto.getQuantidade());
                totalItens += produto.getQuantidade();
            }
        }

        if (totalItens > 0) {
            carrinhoBarra.setVisibility(View.VISIBLE);

            String precoFormatado = String.format("R$%.2f", precoTotal);
            textPrecoCarrinho.setText(precoFormatado);

            String totalItensTexto = String.format("Total: %d %s", totalItens, (totalItens == 1 ? "item" : "itens"));
            textTotalCarrinho.setText(totalItensTexto);
        } else {
            carrinhoBarra.setVisibility(View.GONE);
        }
    }

    @Override
    public void onCategoriaClick(Categoria categoria) {
        List<Produto> produtosFiltrados;

        if (categoria.getId() == 0) {
            produtosFiltrados = produtosGeral;
        } else {
            produtosFiltrados = new ArrayList<>();
            for (Produto produto : produtosGeral) {
                if (produto.getCategoria() != null && produto.getCategoria().getId().equals(categoria.getId())) {
                    produtosFiltrados.add(produto);
                }
            }
        }

        produtoAdapter.atualizarLista(produtosFiltrados);
        atualizarEstadoCarrinho(produtosGeral);
    }

    private void setupRecyclerViewProdutos() {
        androidx.recyclerview.widget.RecyclerView recyclerViewProdutos = findViewById(R.id.recycler_produtos);

        produtosGeral = criarListaProdutos();

        produtoAdapter = new ProdutoAdapter(produtosGeral, this);

        GridLayoutManager layoutManagerProdutos = new GridLayoutManager(this, 2);

        recyclerViewProdutos.setLayoutManager(layoutManagerProdutos);

        recyclerViewProdutos.setAdapter(produtoAdapter);

        if (recyclerViewProdutos.getItemDecorationCount() > 0) {
            for (int i = 0; i < recyclerViewProdutos.getItemDecorationCount(); i++) {
                recyclerViewProdutos.removeItemDecorationAt(i);
            }
        }

        int spanCount = 2;
        int internalSpacing = 30;
        int edgeSpacing = 10;

        int spacingPx = (int) (internalSpacing * getResources().getDisplayMetrics().density);
        int edgeSpacingPx = (int) (edgeSpacing * getResources().getDisplayMetrics().density);

        recyclerViewProdutos.addItemDecoration(new GridSpacingItemDecoration(
                spanCount,
                spacingPx,
                edgeSpacingPx,
                true
        ));
    }

    private List<Produto> criarListaProdutos() {
        List<Produto> produtos = new ArrayList<>();

        Categoria bebidas = new Categoria(1, "Bebidas", R.drawable.icon___bebidas);
        Categoria salgados = new Categoria(2, "Salgados", R.drawable.icon___bebidas);
        Categoria almocos = new Categoria(3, "Almoços", R.drawable.icon___bebidas);
        Categoria cafes = new Categoria(4, "Cafés", R.drawable.icon___bebidas);
        Categoria doces = new Categoria(5, "Doces", R.drawable.icon___bebidas);
        Categoria sobremesas = new Categoria(6, "Sobremesas", R.drawable.icon___bebidas);

        produtos.add(new Produto(1, "Refrigerante Guaraná Tônico Massa", "Lata 350ml", 3.03, R.drawable.icon___cocacola, bebidas));
        produtos.add(new Produto(2, "Coca-Cola Original", "Lata 350ml", 3.50, R.drawable.icon___cocacola, salgados));
        produtos.add(new Produto(3, "Fanta Laranja", "Lata 350ml", 3.25, R.drawable.icon___cocacola, almocos));
        produtos.add(new Produto(4, "Coxinha", "300g", 2.99, R.drawable.icon___cocacola, cafes));
        produtos.add(new Produto(5, "Sprite Limão", "Lata 350ml", 3.10, R.drawable.icon___cocacola, doces));
        produtos.add(new Produto(6, "Água com Gás", "Garrafa 500ml", 2.50, R.drawable.icon___cocacola, sobremesas));

        return produtos;
    }

    private void setupRecyclerViewCategorias() {
        androidx.recyclerview.widget.RecyclerView recyclerViewCategorias = findViewById(R.id.recycler_categorias);

        List<Categoria> categorias = criarListaCategorias();

        CategoriaAdapter adapterCategorias = new CategoriaAdapter(categorias, this);

        androidx.recyclerview.widget.LinearLayoutManager layoutManagerCategorias = new androidx.recyclerview.widget.LinearLayoutManager(this, androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL, false);

        recyclerViewCategorias.setLayoutManager(layoutManagerCategorias);

        recyclerViewCategorias.setAdapter(adapterCategorias);
    }

    private List<Categoria> criarListaCategorias() {
        List<Categoria> categorias = new ArrayList<>();

        categorias.add(new Categoria(0, "Todos", R.drawable.icon___bebidas));
        categorias.add(new Categoria(1, "Bebidas", R.drawable.icon___bebidas));
        categorias.add(new Categoria(2, "Salgados", R.drawable.icon___bebidas));
        categorias.add(new Categoria(3, "Almoços", R.drawable.icon___bebidas));
        categorias.add(new Categoria(4, "Cafés", R.drawable.icon___bebidas));
        categorias.add(new Categoria(5, "Doces", R.drawable.icon___bebidas));
        categorias.add(new Categoria(6, "Sobremesas", R.drawable.icon___bebidas));

        return categorias;
    }

    @Override
    public void onQuantidadeChanged() {
        if (produtosGeral != null) {
            atualizarEstadoCarrinho(produtosGeral);
        }
    }
}