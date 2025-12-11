package com.example.projeto_v1.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import android.os.Bundle;
import com.example.projeto_v1.R;
import com.example.projeto_v1.adapter.ProdutoAdapter;
import com.example.projeto_v1.model.Produto;
import java.util.ArrayList;
import java.util.List;

public class HomeClienteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_cliente);

        setupRecyclerView();
    }

    private void setupRecyclerView() {
        androidx.recyclerview.widget.RecyclerView recyclerView =
                findViewById(R.id.recycler_home);

        // 1. Criar lista de produtos
        List<Produto> produtos = criarListaProdutos();

        // 2. Criar adapter
        ProdutoAdapter adapter = new ProdutoAdapter(produtos);

        // 3. IMPORTANTE: Configurar GridLayoutManager com 2 colunas
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);

        // 4. Definir adapter
        recyclerView.setAdapter(adapter);
    }

    private List<Produto> criarListaProdutos() {
        List<Produto> produtos = new ArrayList<>();

        // Adicione produtos - eles aparecerão em 2 colunas
        produtos.add(new Produto(1, "Refrigerante Guaraná Tônico Massa", "Lata 350ml", 3.03,
                R.drawable.icon___cocacola));
        produtos.add(new Produto(2, "Coca-Cola Original", "Lata 350ml", 3.50,
                R.drawable.icon___cocacola));
        produtos.add(new Produto(3, "Fanta Laranja", "Lata 350ml", 3.25,
                R.drawable.icon___cocacola));
        produtos.add(new Produto(4, "Pepsi Twist", "Lata 350ml", 2.99,
                R.drawable.icon___cocacola));
        produtos.add(new Produto(5, "Sprite Limão", "Lata 350ml", 3.10,
                R.drawable.icon___cocacola));
        produtos.add(new Produto(6, "Água com Gás", "Garrafa 500ml", 2.50,
                R.drawable.icon___cocacola));

        return produtos;
    }
}