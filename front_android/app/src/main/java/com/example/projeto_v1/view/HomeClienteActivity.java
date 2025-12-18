package com.example.projeto_v1.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projeto_v1.R;
import com.example.projeto_v1.adapter.CategoriaAdapter;
import com.example.projeto_v1.adapter.ProdutoAdapter;
import com.example.projeto_v1.model.Carrinho;
import com.example.projeto_v1.model.Categoria;
import com.example.projeto_v1.model.CategoryResponse;
import com.example.projeto_v1.model.ProductCustomerResponse;
import com.example.projeto_v1.model.Produto;
import com.example.projeto_v1.utils.CarrinhoManager;
import com.example.projeto_v1.utils.GridSpacingItemDecoration;
import com.example.projeto_v1.utils.SessionManager;
import com.example.projeto_v1.viewmodel.CategoriaViewModel;
import com.example.projeto_v1.viewmodel.ProdutoViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeClienteActivity extends AppCompatActivity implements ProdutoAdapter.OnProdutoQuantidadeChangeListener, CategoriaAdapter.OnCategoriaClickListener {

    private TextView textPrecoCarrinho, textTotalCarrinho, textSemProdutos;
    private Button btnCarrinho;
    private View carrinhoBarra;
    private EditText barraPesquisa;
    private RecyclerView recyclerProdutos, recyclerCategorias;

    private List<Produto> produtosGeral = new ArrayList<>();
    private ProdutoAdapter produtoAdapter;

    private List<Categoria> categoriasGeral = new ArrayList<>();
    private Map<Long, Categoria> categoriasMap = new HashMap<>(); // Changed Key to Long
    private Categoria categoriaSelecionada;
    private CategoriaAdapter categoriaAdapter;

    private ProdutoViewModel produtoViewModel;
    private CategoriaViewModel categoriaViewModel;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_cliente);

        sessionManager = new SessionManager(this);
        produtoViewModel = new ViewModelProvider(this).get(ProdutoViewModel.class);
        categoriaViewModel = new ViewModelProvider(this).get(CategoriaViewModel.class);

        inicializarComponentes();
        setupRecyclerViewCategorias(); // Sets up empty adapter
        setupRecyclerViewProdutos();   // Sets up empty adapter
        configurarCarrinho();
        setupSearchListener();

        if (savedInstanceState != null) {
            String pesquisa = savedInstanceState.getString("pesquisa", "");
            long categoriaId = savedInstanceState.getLong("categoria", 0L);
            categoriaSelecionada = getCategoriaById(categoriaId);
            barraPesquisa.setText(pesquisa);
        } else {
            // Default category "Todos"
            categoriaSelecionada = new Categoria(0L, "Todos", R.drawable.icon___bebidas);
            categoriasMap.put(0L, categoriaSelecionada);
        }

        // Fetch Data
        carregarDadosDaApi();
    }

    private void carregarDadosDaApi() {
        String token = sessionManager.fetchAuthToken();
        if (token == null) {
            Toast.makeText(this, "Sessão expirada", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginClienteActivity.class));
            finish();
            return;
        }

        // 1. Fetch Categories first
        categoriaViewModel.listarCategorias().observe(this, apiCategories -> {
            if (apiCategories != null) {
                categoriasGeral.clear();
                categoriasMap.clear();

                // Add "Todos" manually
                Categoria todos = new Categoria(0L, "Todos", R.drawable.icon___bebidas);
                categoriasGeral.add(todos);
                categoriasMap.put(0L, todos);

                // Convert API categories
                for (CategoryResponse catRes : apiCategories) {
                    Categoria c = new Categoria(catRes.getId(), catRes.getName(), catRes.getImageUrl(), catRes.getColorHex());
                    categoriasGeral.add(c);
                    categoriasMap.put(c.getId(), c);
                }

                // Update Adapter
                if (categoriaAdapter != null) {
                    categoriaAdapter.atualizarLista(categoriasGeral);
                }

                // 2. Fetch Products after categories are loaded
                carregarProdutos(token);
            } else {
                Toast.makeText(this, "Erro ao carregar categorias", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void carregarProdutos(String token) {
        produtoViewModel.listarProdutos(token, null, null).observe(this, listaApi -> {
            if (listaApi != null) {
                produtosGeral.clear();
                for (ProductCustomerResponse itemApi : listaApi) {
                    produtosGeral.add(converterApiParaLocal(itemApi));
                }
                sincronizarHomeComCarrinho();
                aplicarFiltros();
            } else {
                Toast.makeText(this, "Erro ao carregar produtos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Produto converterApiParaLocal(ProductCustomerResponse itemApi) {
        // Try to match category by name from the list of loaded categories
        Categoria cat = categoriasMap.get(0L); // Default to 'Todos'

        if (itemApi.getCategories() != null && !itemApi.getCategories().isEmpty()) {
            String apiCatName = itemApi.getCategories().get(0);

            // Search in our map values
            for (Categoria c : categoriasMap.values()) {
                if (c.getNome().equalsIgnoreCase(apiCatName)) {
                    cat = c;
                    break;
                }
            }
        }

        Produto p = new Produto(
                itemApi.getName(),
                itemApi.getDescription(),
                itemApi.getPrice(),
                R.drawable.icon___cocacola,
                cat
        );
        p.setId(itemApi.getId());
        p.setImageUrl(itemApi.getImageUrl());

        return p;
    }

    // --- REMAINING METHODS (Same as before, just slight tweaks for Long IDs) ---

    private void aplicarFiltros() {
        if (produtosGeral == null || produtoAdapter == null) return;

        List<Produto> filtrados = new ArrayList<>();
        String termo = barraPesquisa.getText().toString().trim().toLowerCase();

        for (Produto produto : produtosGeral) {
            boolean categoriaOk = false;

            // Check if "Todos" (ID 0) is selected or matches specific category
            if (categoriaSelecionada == null || categoriaSelecionada.getId() == 0L) {
                categoriaOk = true;
            } else if (produto.getCategoria() != null) {
                if (produto.getCategoria().getId().equals(categoriaSelecionada.getId())) {
                    categoriaOk = true;
                }
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

    private void setupRecyclerViewCategorias() {
        recyclerCategorias = findViewById(R.id.recycler_categorias);
        // Initialize with empty list
        categoriaAdapter = new CategoriaAdapter(new ArrayList<>(), this);
        recyclerCategorias.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerCategorias.setAdapter(categoriaAdapter);
    }

    // ... (All other methods: onResume, setupRecyclerViewProdutos, configuring listeners, etc. remain the same) ...

    private Categoria getCategoriaById(long id) { return categoriasMap.get(id); }

    @Override
    protected void onResume() {
        super.onResume();

        // 1. FORÇA a atualização do Singleton com base no Manager (Backup Seguro)
        // Isso garante que se você alterou qtd ou removeu itens na tela de Carrinho, a Home saiba.
        if (!CarrinhoManager.getProdutosNoCarrinhoMap().isEmpty()) {
            Carrinho.getInstance().setProdutos(new ArrayList<>(CarrinhoManager.getProdutosNoCarrinhoMap().values()));
        }

        // 2. Sincroniza visualmente (pinta os números nos cards)
        sincronizarHomeComCarrinho();

        // 3. Atualiza a barra inferior e reaplica filtros (caso tivesse pesquisa digitada)
        atualizarEstadoCarrinho(produtosGeral);
        aplicarFiltros();
    }

    private void sincronizarHomeComCarrinho() {
        if (produtosGeral == null || produtosGeral.isEmpty()) return;
        for (Produto p : produtosGeral) p.setQuantidade(0);
        List<Produto> itensNoCarrinho = Carrinho.getInstance().getProdutos();

        if (itensNoCarrinho != null) {
            for (Produto pCarrinho : itensNoCarrinho) {
                for (Produto pHome : produtosGeral) {
                    boolean match = false;
                    if (pHome.getId() != null && pHome.getId().equals(pCarrinho.getId())) {
                        match = true;
                    } else if (pHome.getNome().trim().equalsIgnoreCase(pCarrinho.getNome().trim())) {
                        match = true;
                    }

                    if (match) {
                        pHome.setQuantidade(pCarrinho.getQuantidade());
                        break;
                    }
                }
            }
        }
        if (produtoAdapter != null) produtoAdapter.notifyDataSetChanged();
    }

    // ... include inicializarComponentes, configurarCarrinho, navigation methods, setupSearchListener, etc ...
    private void inicializarComponentes() {
        carrinhoBarra = findViewById(R.id.carrinhoBarra);
        textPrecoCarrinho = carrinhoBarra.findViewById(R.id.textPrecoCarrinho);
        textTotalCarrinho = carrinhoBarra.findViewById(R.id.textTotalCarrinho);
        btnCarrinho = carrinhoBarra.findViewById(R.id.btnCarrinho);
        barraPesquisa = findViewById(R.id.barraPesquisa);
        recyclerProdutos = findViewById(R.id.recycler_produtos);
        recyclerCategorias = findViewById(R.id.recycler_categorias);
        textSemProdutos = findViewById(R.id.text_sem_produtos);
    }

    private void configurarCarrinho() {
        btnCarrinho.setOnClickListener(v -> {
            CarrinhoManager.setProdutosNoCarrinho(produtosGeral);
            Intent intent = new Intent(HomeClienteActivity.this, CarrinhoClienteActivity.class);
            startActivity(intent);
        });
    }

    public void goNavegacaoPedidos(View view) {
        startActivity(new Intent(HomeClienteActivity.this, PedidosClienteActivity.class));
        finish();
    }

    public void goNavegacaoPerfil(View view) {
        startActivity(new Intent(this, PerfilClienteActivity.class));
        finish();
    }

    private void setupSearchListener() {
        barraPesquisa.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                aplicarFiltros(); // Filtra enquanto digita
            }

            @Override public void afterTextChanged(Editable s) {}
        });

        // --- ADICIONE ISTO: Força filtro ao perder foco (fechar teclado) ---
        barraPesquisa.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                aplicarFiltros();
            }
        });

        barraPesquisa.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_SEARCH) {
                esconderTeclado();
                barraPesquisa.clearFocus(); // Tira o foco para evitar bugs visuais
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

    private void atualizarEstadoCarrinho(List<Produto> produtos) {
        if (produtos == null) return;

        double precoTotal = 0.0;
        int totalItens = 0;

        for (Produto produto : produtos) {
            if (produto.getQuantidade() > 0) {
                precoTotal += (produto.getPreco() * produto.getQuantidade());
                totalItens += produto.getQuantidade();
            }
        }

        if (totalItens > 0) {
            carrinhoBarra.setVisibility(View.VISIBLE);
            textPrecoCarrinho.setText(String.format("R$%.2f", precoTotal));
            textTotalCarrinho.setText("Total: " + totalItens + " itens");
        } else {
            carrinhoBarra.setVisibility(View.GONE);
        }
    }

    private void setupRecyclerViewProdutos() {
        produtoAdapter = new ProdutoAdapter(new ArrayList<>(), this);
        recyclerProdutos.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerProdutos.setAdapter(produtoAdapter);
        recyclerProdutos.addItemDecoration(new GridSpacingItemDecoration(2, (int)(30 * getResources().getDisplayMetrics().density), (int)(10 * getResources().getDisplayMetrics().density), true));
    }

    @Override
    public void onCategoriaClick(Categoria categoria) {
        categoriaSelecionada = categoria;
        aplicarFiltros();
    }

    @Override
    public void onQuantidadeChanged() {
        atualizarEstadoCarrinho(produtosGeral);
        List<Produto> ativos = new ArrayList<>();
        for(Produto p : produtosGeral) {
            if(p.getQuantidade() > 0) ativos.add(p);
        }
        Carrinho.getInstance().setProdutos(ativos);
        CarrinhoManager.setProdutosNoCarrinho(produtosGeral);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("pesquisa", barraPesquisa.getText().toString());
        outState.putLong("categoria", categoriaSelecionada != null ? categoriaSelecionada.getId() : 0L);
    }
}