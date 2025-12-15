package com.example.projeto_v1.view;

import com.example.projeto_v1.utils.CarrinhoManager;
import com.example.projeto_v1.utils.GridSpacingItemDecoration;

import androidx.annotation.NonNull;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeClienteActivity extends AppCompatActivity implements ProdutoAdapter.OnProdutoQuantidadeChangeListener, CategoriaAdapter.OnCategoriaClickListener {

    private TextView textPrecoCarrinho;
    private TextView textTotalCarrinho;
    private Button btnCarrinho;
    private View carrinhoBarra;
    private android.widget.EditText barraPesquisa;
    private List<Produto> produtosGeral;
    private ProdutoAdapter produtoAdapter;
    private List<Categoria> categoriasGeral;
    private Map<Integer, Categoria> categoriasMap = new HashMap<>();
    private Categoria categoriaSelecionada;
    private CategoriaAdapter categoriaAdapter;

    public void goNavegacaoPedidos(View view) {
        Intent intent = new Intent(HomeClienteActivity.this, PedidosClienteActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("pesquisa", barraPesquisa.getText().toString());
        outState.putInt("categoria", categoriaSelecionada != null ? categoriaSelecionada.getId() : 0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // produtosGeral = criarListaProdutos();
        aplicarFiltros();
        atualizarEstadoCarrinho(produtosGeral);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_cliente);

        carrinhoBarra = findViewById(R.id.carrinhoBarra);

        textPrecoCarrinho = carrinhoBarra.findViewById(R.id.textPrecoCarrinho);
        textTotalCarrinho = carrinhoBarra.findViewById(R.id.textTotalCarrinho);
        btnCarrinho = carrinhoBarra.findViewById(R.id.btnCarrinho);

        barraPesquisa = findViewById(R.id.barraPesquisa);

        btnCarrinho.setOnClickListener(v -> {
            Intent intent = new Intent(HomeClienteActivity.this, PedidosClienteActivity.class);
            startActivity(intent);
            finish();
        });

        setupRecyclerViewCategorias();

        produtosGeral = criarListaProdutos();

        categoriaSelecionada = getCategoriaById(0);

        setupRecyclerViewProdutos();
        aplicarFiltros();

        atualizarEstadoCarrinho(produtosGeral);

        setupSearchListener();

        if (savedInstanceState != null) {
            String pesquisa = savedInstanceState.getString("pesquisa", "");
            int categoriaId = savedInstanceState.getInt("categoria", 0);

            categoriaSelecionada = getCategoriaById(categoriaId);
            barraPesquisa.setText(pesquisa);

            aplicarFiltros();
        }
    }

    private void setupSearchListener() {

        barraPesquisa.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                aplicarFiltros();
            }

            @Override
            public void afterTextChanged(android.text.Editable s) {

            }
        });
    }

    private void aplicarFiltros() {
        List<Produto> filtrados = new ArrayList<>();

        String termo = barraPesquisa.getText().toString().trim().toLowerCase();

        for (Produto produto : produtosGeral) {

            if (categoriaSelecionada != null && categoriaSelecionada.getId() != 0) {
                if (produto.getCategoria() == null || produto.getCategoria().getId() != categoriaSelecionada.getId()) {
                    continue;
                }
            }

            if (!termo.isEmpty()) {
                if (!produto.getNome().toLowerCase().contains(termo) &&
                        !produto.getDescricao().toLowerCase().contains(termo)) {
                    continue;
                }
            }

            filtrados.add(produto);
        }

        produtoAdapter.atualizarLista(filtrados);

        TextView textSemProdutos = findViewById(R.id.text_sem_produtos);
        if (filtrados.isEmpty()) {
            textSemProdutos.setVisibility(View.VISIBLE);
        } else {
            textSemProdutos.setVisibility(View.GONE);
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

    private void setupRecyclerViewCategorias() {
        androidx.recyclerview.widget.RecyclerView recyclerViewCategorias = findViewById(R.id.recycler_categorias);

        List<Categoria> categorias = criarListaCategorias();

        this.categoriaAdapter = new CategoriaAdapter(categorias, this);

        androidx.recyclerview.widget.LinearLayoutManager layoutManagerCategorias = new androidx.recyclerview.widget.LinearLayoutManager(this, androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL, false);

        recyclerViewCategorias.setLayoutManager(layoutManagerCategorias);

        recyclerViewCategorias.setAdapter(this.categoriaAdapter);
    }

    private List<Categoria> criarListaCategorias() {
        List<Categoria> categorias = new ArrayList<>();

        Categoria todos   = new Categoria(0, "Todos", R.drawable.icon___bebidas);
        Categoria bebidas = new Categoria(1, "Bebidas", R.drawable.icon___bebidas);
        Categoria lanches = new Categoria(2, "Lanches", R.drawable.icon___bebidas);
        Categoria doces   = new Categoria(3, "Doces", R.drawable.icon___bebidas);
        Categoria almocos = new Categoria(4, "Almoços", R.drawable.icon___bebidas);

        categorias.add(todos);
        categorias.add(bebidas);
        categorias.add(lanches);
        categorias.add(doces);
        categorias.add(almocos);

        categoriasMap.put(0, todos);
        categoriasMap.put(1, bebidas);
        categoriasMap.put(2, lanches);
        categoriasMap.put(3, doces);
        categoriasMap.put(4, almocos);

        categoriasGeral = categorias;

        return categorias;
    }

    private Categoria getCategoriaById(int id) {
        return categoriasMap.get(id);
    }

    @Override
    public void onCategoriaClick(Categoria categoria) {
        categoriaSelecionada = categoria;
        aplicarFiltros();
    }

    private void setupRecyclerViewProdutos() {
        androidx.recyclerview.widget.RecyclerView recyclerViewProdutos = findViewById(R.id.recycler_produtos);

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

        recyclerViewProdutos.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacingPx, edgeSpacingPx, true));
    }

    private List<Produto> criarListaProdutos() {
        List<Produto> produtos = new ArrayList<>();

        Categoria bebidas = getCategoriaById(1);
        Categoria lanches = getCategoriaById(2);
        Categoria doces   = getCategoriaById(3);
        Categoria almocos = getCategoriaById(4);

        if (bebidas != null) {
            produtos.add(new Produto(1, "Refrigerante Guaraná Tônico Massa", "Lata 350ml", 3.03, R.drawable.icon___cocacola, bebidas));
            produtos.add(new Produto(2, "Coca-Cola Original", "Lata 350ml", 3.50, R.drawable.icon___cocacola, bebidas));
            produtos.add(new Produto(3, "Fanta Laranja", "Lata 350ml", 3.25, R.drawable.icon___cocacola, bebidas));
        }

        if (lanches != null) {
            produtos.add(new Produto(4, "Coxinha", "300g", 2.99, R.drawable.icon___cocacola, lanches));
            produtos.add(new Produto(5, "Esfirra de Carne", "Unidade", 3.99, R.drawable.icon___cocacola, lanches));
        }

        if (doces != null) {
            produtos.add(new Produto(6, "Bolo de Chocolate", "Fatia", 7.00, R.drawable.icon___cocacola, doces));
        }

        Map<Integer, Produto> estadoSalvo = CarrinhoManager.getProdutosNoCarrinhoMap();
        for (Produto produto : produtos) {
            if (estadoSalvo.containsKey(produto.getId())) {
                produto.setQuantidade(estadoSalvo.get(produto.getId()).getQuantidade());
            }
        }

        return produtos;
    }

    @Override
    public void onQuantidadeChanged() {
        if (produtosGeral != null) {
            CarrinhoManager.setProdutosNoCarrinho(produtosGeral);

            atualizarEstadoCarrinho(produtosGeral);
        }
    }
}