package com.example.projeto_v1.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projeto_v1.R;
import com.example.projeto_v1.adapter.HistoricoPedidosAdapter;
import com.example.projeto_v1.utils.SessionManager;
import com.example.projeto_v1.viewmodel.OrderViewModel;

public class PedidosClienteActivity extends AppCompatActivity {

    private RecyclerView recyclerCardsPedidos;
    private TextView textSemPedidos;
    private ImageView btnVoltarPerfil; // Se tiver botão de voltar

    private OrderViewModel orderViewModel;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedidos_cliente);

        sessionManager = new SessionManager(this);
        orderViewModel = new ViewModelProvider(this).get(OrderViewModel.class);

        inicializarComponentes();

        // Busca os pedidos da API
        carregarPedidosDaApi();
    }

    private void inicializarComponentes() {
        recyclerCardsPedidos = findViewById(R.id.recycler_pedidos);
        textSemPedidos = findViewById(R.id.text_sem_pedidos);
        // btnVoltarPerfil = findViewById(R.id.btn_voltar_perfil); // Caso exista
    }

    private void carregarPedidosDaApi() {
        String token = sessionManager.fetchAuthToken();

        if (token == null) {
            Toast.makeText(this, "Sessão expirada", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginClienteActivity.class));
            finish();
            return;
        }

        // Chama o ViewModel para buscar lista
        orderViewModel.listarMeusPedidos(token).observe(this, listaPedidos -> {
            if (listaPedidos != null && !listaPedidos.isEmpty()) {
                recyclerCardsPedidos.setVisibility(View.VISIBLE);
                textSemPedidos.setVisibility(View.GONE);

                // Configura o Adapter com a lista da API
                HistoricoPedidosAdapter adapter = new HistoricoPedidosAdapter(listaPedidos);
                recyclerCardsPedidos.setLayoutManager(new LinearLayoutManager(this));
                recyclerCardsPedidos.setAdapter(adapter);
            } else {
                // Lista vazia ou nula (mas com sucesso na requisição)
                recyclerCardsPedidos.setVisibility(View.GONE);
                textSemPedidos.setVisibility(View.VISIBLE);
            }
        });
    }

    // --- Navegação ---

    public void goNavegacaoHome(View view) {
        Intent intent = new Intent(this, HomeClienteActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }

    public void goNavegacaoPerfil(View view) {
        startActivity(new Intent(this, PerfilClienteActivity.class));
        finish();
    }
}