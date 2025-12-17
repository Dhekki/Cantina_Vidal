package com.example.projeto_v1.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projeto_v1.R;
import com.example.projeto_v1.adapter.HistoricoPedidosAdapter;
import com.example.projeto_v1.repository.PedidosRepository;

public class PedidosClienteActivity extends AppCompatActivity {

    private RecyclerView recyclerCardsPedidos;
    private TextView textSemPedidos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedidos_cliente);

        recyclerCardsPedidos = findViewById(R.id.recycler_pedidos);
        textSemPedidos = findViewById(R.id.text_sem_pedidos);

        carregarPedidos();
    }

    private void carregarPedidos() {
        var lista = PedidosRepository.getInstance().getListaPedidos();

        if (lista == null || lista.isEmpty()) {
            recyclerCardsPedidos.setVisibility(View.GONE);
            textSemPedidos.setVisibility(View.VISIBLE);
        } else {
            recyclerCardsPedidos.setVisibility(View.VISIBLE);
            textSemPedidos.setVisibility(View.GONE);

            HistoricoPedidosAdapter adapter = new HistoricoPedidosAdapter(lista);
            recyclerCardsPedidos.setLayoutManager(new LinearLayoutManager(this));
            recyclerCardsPedidos.setAdapter(adapter);
        }
    }

    public void goNavegacaoHome(View view) {
        Intent intent = new Intent(this, HomeClienteActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }
}