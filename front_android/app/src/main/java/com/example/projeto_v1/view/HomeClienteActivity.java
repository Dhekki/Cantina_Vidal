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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projeto_v1.R;
import com.example.projeto_v1.adapter.CategoriaAdapter;
import com.example.projeto_v1.adapter.ProdutoAdapter;
import com.example.projeto_v1.model.Carrinho;
import com.example.projeto_v1.model.Categoria;
import com.example.projeto_v1.model.Produto;
import com.example.projeto_v1.utils.CarrinhoManager;
import com.example.projeto_v1.utils.GridSpacingItemDecoration;

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

    private List<Produto> produtosGeral;
    private ProdutoAdapter produtoAdapter;
    private List<Categoria> categoriasGeral;
    private Map<Integer, Categoria> categoriasMap = new HashMap<>();
    private Categoria categoriaSelecionada;
    private CategoriaAdapter categoriaAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_cliente);

        inicializarComponentes();
        setupRecyclerViewCategorias(); // Carrega categorias primeiro

        produtosGeral = criarListaProdutos();
        categoriaSelecionada = getCategoriaById(0); // Inicia com "Todos"

        setupRecyclerViewProdutos();
        configurarCarrinho();
        setupSearchListener();

        if (savedInstanceState != null) {
            String pesquisa = savedInstanceState.getString("pesquisa", "");
            int categoriaId = savedInstanceState.getInt("categoria", 0);
            categoriaSelecionada = getCategoriaById(categoriaId);
            barraPesquisa.setText(pesquisa);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // CORREÇÃO: Ao voltar para esta tela, sincroniza a lista da Home com o Carrinho real
        sincronizarQuantidadesComCarrinho();
        atualizarEstadoCarrinho(produtosGeral);
        aplicarFiltros();
    }

    private void sincronizarQuantidadesComCarrinho() {
        if (produtosGeral == null) return;

        // 1. Zera tudo primeiro (assume que tudo foi removido/resetado)
        for (Produto p : produtosGeral) {
            p.setQuantidade(0);
        }

        // 2. Reaplica as quantidades APENAS dos itens que ainda estão no Carrinho (Singleton)
        List<Produto> noCarrinho = Carrinho.getInstance().getProdutos();
        if (noCarrinho != null) {
            for (Produto pCarrinho : noCarrinho) {
                for (Produto pGeral : produtosGeral) {
                    // Compara por ID (mais seguro) ou Nome se ID for zero
                    if (pGeral.getId() == pCarrinho.getId()) {
                        pGeral.setQuantidade(pCarrinho.getQuantidade());
                        break;
                    }
                }
            }
        }

        // 3. Notifica o adaptador da Home para redesenhar os números (0 ou X)
        if (produtoAdapter != null) {
            produtoAdapter.notifyDataSetChanged();
        }
    }

    private void inicializarComponentes() {
        carrinhoBarra = findViewById(R.id.carrinhoBarra);
        textPrecoCarrinho = carrinhoBarra.findViewById(R.id.textPrecoCarrinho);
        textTotalCarrinho = carrinhoBarra.findViewById(R.id.textTotalCarrinho);
        btnCarrinho = carrinhoBarra.findViewById(R.id.btnCarrinho);
        barraPesquisa = findViewById(R.id.barraPesquisa);
        recyclerProdutos = findViewById(R.id.recycler_produtos);
        recyclerCategorias = findViewById(R.id.recycler_categorias); // Certifique-se que o ID no XML é este
        textSemProdutos = findViewById(R.id.text_sem_produtos);
    }

    private void configurarCarrinho() {
        btnCarrinho.setOnClickListener(v -> {
            preencherCarrinhoComProdutosAtivos();
            // Vai para a tela de Carrinho (para editar/revisar)
            Intent intent = new Intent(HomeClienteActivity.this, CarrinhoClienteActivity.class);
            startActivity(intent);
        });
    }

    // Navegação da barra inferior (ícone de pedidos)
    public void goNavegacaoPedidos(View view) {
        startActivity(new Intent(this, PedidosClienteActivity.class));
    }

    private void preencherCarrinhoComProdutosAtivos() {
        // Atualiza o Manager para persistência se o app fechar,
        // mas a lógica principal usa o Singleton Carrinho.
        CarrinhoManager.setProdutosNoCarrinho(produtosGeral);
    }

    private void setupSearchListener() {
        barraPesquisa.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) { aplicarFiltros(); }
            @Override public void afterTextChanged(Editable s) {}
        });

        barraPesquisa.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_SEARCH) {
                esconderTeclado();
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

    // --- Configuração Listas e Dados ---

    private void setupRecyclerViewCategorias() {
        // Agora busca pelo ID correto do XML
        recyclerCategorias = findViewById(R.id.recycler_categorias);
        List<Categoria> categorias = criarListaCategorias();
        categoriaAdapter = new CategoriaAdapter(categorias, this);

        recyclerCategorias.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerCategorias.setAdapter(categoriaAdapter);
    }

    private void setupRecyclerViewProdutos() {
        produtoAdapter = new ProdutoAdapter(produtosGeral, this);
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
        // Atualiza a barra visual
        atualizarEstadoCarrinho(produtosGeral);

        // Atualiza o Singleton do Carrinho imediatamente ao clicar nos botões + ou - na Home
        List<Produto> ativos = new ArrayList<>();
        for(Produto p : produtosGeral) {
            if(p.getQuantidade() > 0) ativos.add(p);
        }
        Carrinho.getInstance().setProdutos(ativos);

        // Atualiza o Manager para persistência
        CarrinhoManager.setProdutosNoCarrinho(produtosGeral);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("pesquisa", barraPesquisa.getText().toString());
        outState.putInt("categoria", categoriaSelecionada != null ? categoriaSelecionada.getId() : 0);
    }

    private List<Categoria> criarListaCategorias() {
        List<Categoria> categorias = new ArrayList<>();
        categorias.add(new Categoria(0, "Todos", R.drawable.icon___bebidas));
        categorias.add(new Categoria(1, "Bebidas", R.drawable.icon___bebidas));
        categorias.add(new Categoria(2, "Lanches", R.drawable.icon___bebidas));
        categorias.add(new Categoria(3, "Doces", R.drawable.icon___bebidas));
        categorias.add(new Categoria(4, "Almoços", R.drawable.icon___bebidas));

        for(Categoria c : categorias) categoriasMap.put(c.getId(), c);
        categoriasGeral = categorias;
        return categorias;
    }

    private Categoria getCategoriaById(int id) { return categoriasMap.get(id); }

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

        // Restaura do Manager (persistência)
        Map<Integer, Produto> salvos = CarrinhoManager.getProdutosNoCarrinhoMap();
        for (Produto produto : produtos) {
            if (salvos.containsKey(produto.getId())) {
                produto.setQuantidade(salvos.get(produto.getId()).getQuantidade());
            }
        }
        return produtos;
    }
}