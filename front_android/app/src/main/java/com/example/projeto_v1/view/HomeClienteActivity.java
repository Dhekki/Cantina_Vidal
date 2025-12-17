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
        setupRecyclerViewCategorias();

        produtosGeral = criarListaProdutos();
        categoriaSelecionada = getCategoriaById(0);

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

    // --- PONTO CRUCIAL: ONDE A MÁGICA ACONTECE AO VOLTAR DO CARRINHO ---
    @Override
    protected void onResume() {
        super.onResume();

        // 1. Força a sincronização entre a Lista Visual (Home) e a Lista Real (Singleton)
        sincronizarListasPeloNome();

        // 2. Atualiza a barra inferior com os novos totais
        atualizarEstadoCarrinho(produtosGeral);

        // 3. Reaplica filtros (caso o usuário tivesse digitado algo na busca)
        aplicarFiltros();
    }

    private void sincronizarListasPeloNome() {
        if (produtosGeral == null) return;

        // Passo A: Zera TUDO na Home. Assumimos que o carrinho foi esvaziado.
        for (Produto pHome : produtosGeral) {
            pHome.setQuantidade(0);
        }

        // Passo B: Pega o que REALMENTE está no carrinho (Singleton)
        List<Produto> itensNoCarrinho = Carrinho.getInstance().getProdutos();

        // Passo C: Para cada item do carrinho, procura o "irmão gêmeo" na Home pelo NOME e atualiza
        if (itensNoCarrinho != null) {
            for (Produto pCarrinho : itensNoCarrinho) {
                for (Produto pHome : produtosGeral) {
                    // Compara Nomes ignorando maiúsculas/minúsculas e espaços
                    if (pHome.getNome().trim().equalsIgnoreCase(pCarrinho.getNome().trim())) {
                        pHome.setQuantidade(pCarrinho.getQuantidade());
                        break; // Achou, pula para o próximo item do carrinho
                    }
                }
            }
        }

        // Passo D: Avisa o Adapter para redesenhar os números (0 ou X) na tela
        if (produtoAdapter != null) {
            produtoAdapter.notifyDataSetChanged();
        }

        // Passo E: Atualiza o Manager (Persistência) para ficar igual ao Singleton e Home
        CarrinhoManager.setProdutosNoCarrinho(produtosGeral);
    }

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
            // Antes de ir, garante que o Manager está atualizado
            CarrinhoManager.setProdutosNoCarrinho(produtosGeral);
            Intent intent = new Intent(HomeClienteActivity.this, CarrinhoClienteActivity.class);
            startActivity(intent);
        });
    }

    public void goNavegacaoPedidos(View view) {
        startActivity(new Intent(this, PedidosClienteActivity.class));
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
            } else if (produto.getCategoria() != null) {
                if (produto.getCategoria().getId() == categoriaSelecionada.getId()) {
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

    private void atualizarEstadoCarrinho(List<Produto> produtos) {
        if (produtos == null) return;

        double precoTotal = 0.0;
        int totalItens = 0;

        // Calcula com base nos produtos da Home (que já foram sincronizados no onResume)
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

    // --- Listas e Dados ---

    private void setupRecyclerViewCategorias() {
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

        // Atualiza o Singleton IMEDIATAMENTE (Cria uma lista nova baseada na Home)
        List<Produto> ativos = new ArrayList<>();
        for(Produto p : produtosGeral) {
            if(p.getQuantidade() > 0) ativos.add(p);
        }
        Carrinho.getInstance().setProdutos(ativos);

        // Atualiza o Manager (Persistência)
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
            produtos.add(new Produto("Refrigerante Guaraná Tônico Massa", "Lata 350ml", 3.03, R.drawable.icon___cocacola, bebidas));
            produtos.add(new Produto("Coca-Cola Original", "Lata 350ml", 3.50, R.drawable.icon___cocacola, bebidas));
            produtos.add(new Produto("Fanta Laranja", "Lata 350ml", 3.25, R.drawable.icon___cocacola, bebidas));
        }

        if (lanches != null) {
            produtos.add(new Produto("Coxinha", "300g", 5.00, R.drawable.icon___cocacola, lanches));
            produtos.add(new Produto("Esfirra de Carne", "Unidade", 5.00, R.drawable.icon___cocacola, lanches));
            produtos.add(new Produto("Baurú", "Unidade", 5.00, R.drawable.icon___cocacola, lanches));
        }

        if (doces != null) {
            produtos.add(new Produto("Bolo de Chocolate", "Fatia", 7.00, R.drawable.icon___cocacola, doces));
            produtos.add(new Produto("Cocada", "Pedaço", 7.00, R.drawable.icon___cocacola, doces));
            produtos.add(new Produto("Torta de Limão", "Fatia", 7.00, R.drawable.icon___cocacola, doces));
        }

        if (almocos != null) {
            produtos.add(new Produto("Executivo de Frango", "400g", 15.00, R.drawable.icon___cocacola, almocos));
            produtos.add(new Produto("Executivo de Carne", "500g", 15.00, R.drawable.icon___cocacola, almocos));
            produtos.add(new Produto("Prato Feito", "600g", 15.00, R.drawable.icon___cocacola, almocos));
        }

        // RECUPERA DADOS PERSISTIDOS (Backup)
        // Isso é útil se o Singleton foi limpo, mas o Manager ainda tem dados
        Map<String, Produto> salvos = CarrinhoManager.getProdutosNoCarrinhoMap();
        if (salvos != null && !salvos.isEmpty()) {
            for (Produto produto : produtos) {
                for (Produto salvo : salvos.values()) {
                    if (salvo.getNome().trim().equalsIgnoreCase(produto.getNome().trim())) {
                        produto.setQuantidade(salvo.getQuantidade());
                        break;
                    }
                }
            }
        }

        return produtos;
    }
}