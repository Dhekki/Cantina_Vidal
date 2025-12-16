package com.example.projeto_v1.view;

import com.example.projeto_v1.model.Carrinho;
import com.example.projeto_v1.utils.CarrinhoManager;
import com.example.projeto_v1.utils.GridSpacingItemDecoration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
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
    private TextView textSemProdutos;
    private Button btnCarrinho;
    private View carrinhoBarra;
    private EditText barraPesquisa;
    private RecyclerView recyclerProdutos;

    private List<Produto> produtosGeral;
    private ProdutoAdapter produtoAdapter;
    private List<Categoria> categoriasGeral;
    private Map<Integer, Categoria> categoriasMap = new HashMap<>();
    private Categoria categoriaSelecionada;
    private CategoriaAdapter categoriaAdapter;

    public void goNavegacaoPedidos(View view) {
        preencherCarrinhoComProdutosAtivos();
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
        aplicarFiltros();
        atualizarEstadoCarrinho(produtosGeral);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_cliente);

        inicializarComponentes();
        configurarCarrinho();
        setupRecyclerViewCategorias();

        produtosGeral = criarListaProdutos();
        categoriaSelecionada = getCategoriaById(0);

        setupRecyclerViewProdutos();
        atualizarEstadoCarrinho(produtosGeral);
        setupSearchListener();

        if (savedInstanceState != null) {
            String pesquisa = savedInstanceState.getString("pesquisa", "");
            int categoriaId = savedInstanceState.getInt("categoria", 0);

            categoriaSelecionada = getCategoriaById(categoriaId);
            barraPesquisa.setText(pesquisa);
        }

        aplicarFiltros();
    }

    private void inicializarComponentes() {
        carrinhoBarra = findViewById(R.id.carrinhoBarra);
        textPrecoCarrinho = carrinhoBarra.findViewById(R.id.textPrecoCarrinho);
        textTotalCarrinho = carrinhoBarra.findViewById(R.id.textTotalCarrinho);
        btnCarrinho = carrinhoBarra.findViewById(R.id.btnCarrinho);
        barraPesquisa = findViewById(R.id.barraPesquisa);
        recyclerProdutos = findViewById(R.id.recycler_produtos);
        textSemProdutos = findViewById(R.id.text_sem_produtos);
    }

    private void configurarCarrinho() {
        btnCarrinho.setOnClickListener(v -> {
            preencherCarrinhoComProdutosAtivos();
            Intent intent = new Intent(HomeClienteActivity.this, PedidosClienteActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void preencherCarrinhoComProdutosAtivos() {
        List<Produto> produtosComQuantidade = new ArrayList<>();
        for (Produto produto : produtosGeral) {
            if (produto.getQuantidade() > 0) {
                produtosComQuantidade.add(produto);
            }
        }
        Carrinho.getInstance().setProdutos(produtosComQuantidade);
        CarrinhoManager.setProdutosNoCarrinho(produtosGeral);
    }

    private void setupSearchListener() {
        barraPesquisa.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                aplicarFiltros();
            }

            @Override
            public void afterTextChanged(android.text.Editable s) {}
        });

        barraPesquisa.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_SEARCH) {
                esconderTeclado();
                // REMOVIDO: barraPesquisa.clearFocus(); -> Isso causava o "pulo" da tela.
                return true;
            }
            return false;
        });
    }

    private void esconderTeclado() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void aplicarFiltros() {
        if (produtosGeral == null || produtoAdapter == null) return;

        List<Produto> filtrados = new ArrayList<>();
        String termo = barraPesquisa.getText().toString().trim().toLowerCase();

        for (Produto produto : produtosGeral) {
            boolean categoriaOk = false;

            if (categoriaSelecionada == null || categoriaSelecionada.getId() == 0) {
                categoriaOk = true;
            } else if (produto.getCategoria() != null && produto.getCategoria().getId().equals(categoriaSelecionada.getId())) {
                categoriaOk = true;
            }

            boolean pesquisaOk = termo.isEmpty() ||
                    produto.getNome().toLowerCase().contains(termo) ||
                    produto.getDescricao().toLowerCase().contains(termo);

            if (categoriaOk && pesquisaOk) {
                filtrados.add(produto);
            }
        }

        produtoAdapter.atualizarLista(filtrados);

        if (filtrados.isEmpty()) {
            recyclerProdutos.setVisibility(View.GONE);
            textSemProdutos.setVisibility(View.VISIBLE);
        } else {
            recyclerProdutos.setVisibility(View.VISIBLE);
            textSemProdutos.setVisibility(View.GONE);
        }
    }

    private void atualizarEstadoCarrinho(List<Produto> produtosCarrinho) {
        if (produtosCarrinho == null) return;

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
        RecyclerView recyclerViewCategorias = findViewById(R.id.recycler_categorias);
        List<Categoria> categorias = criarListaCategorias();
        this.categoriaAdapter = new CategoriaAdapter(categorias, this);

        androidx.recyclerview.widget.LinearLayoutManager layoutManager =
                new androidx.recyclerview.widget.LinearLayoutManager(this, androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL, false);

        recyclerViewCategorias.setLayoutManager(layoutManager);
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
        produtoAdapter = new ProdutoAdapter(produtosGeral, this);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);

        recyclerProdutos.setLayoutManager(layoutManager);
        recyclerProdutos.setAdapter(produtoAdapter);
        recyclerProdutos.setNestedScrollingEnabled(false);

        if (recyclerProdutos.getItemDecorationCount() > 0) {
            for (int i = 0; i < recyclerProdutos.getItemDecorationCount(); i++) {
                recyclerProdutos.removeItemDecorationAt(i);
            }
        }

        int spanCount = 2;
        int internalSpacing = 30;
        int edgeSpacing = 10;
        int spacingPx = (int) (internalSpacing * getResources().getDisplayMetrics().density);
        int edgeSpacingPx = (int) (edgeSpacing * getResources().getDisplayMetrics().density);

        recyclerProdutos.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacingPx, edgeSpacingPx, true));
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
            produtos.add(new Produto(4, "Coxinha", "300g", 5.00, R.drawable.icon___cocacola, lanches));
            produtos.add(new Produto(5, "Esfirra de Carne", "Unidade", 5.00, R.drawable.icon___cocacola, lanches));
            produtos.add(new Produto(6, "Baurú", "Unidade", 5, R.drawable.icon___cocacola, lanches));
        }

        if (doces != null) {
            produtos.add(new Produto(7, "Bolo de Chocolate", "Fatia", 7.00, R.drawable.icon___cocacola, doces));
            produtos.add(new Produto(8, "Cocada", "Pedaço", 7.00, R.drawable.icon___cocacola, doces));
            produtos.add(new Produto(9, "Torta de Limão", "Fatia", 7.00, R.drawable.icon___cocacola, doces));
        }

        if (almocos != null) {
            produtos.add(new Produto(10, "Executivo de Frango", "400g", 15.00, R.drawable.icon___cocacola, almocos));
            produtos.add(new Produto(11, "Executivo de Carne", "500g", 15.00, R.drawable.icon___cocacola, almocos));
            produtos.add(new Produto(12, "Prato Feito", "600g", 15.00, R.drawable.icon___cocacola, almocos));
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