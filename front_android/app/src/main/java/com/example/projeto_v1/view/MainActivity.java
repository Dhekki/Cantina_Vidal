package com.example.projeto_v1.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.projeto_v1.R;

public class MainActivity extends AppCompatActivity {
    private static final long TEMPO_DE_ESPERA = 6000L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_main);
        (new Handler()).postDelayed(new Runnable() {
            public void run() {
                Intent intent = new Intent(MainActivity.this, CadastroClienteActivity.class);
                MainActivity.this.startActivity(intent);
                MainActivity.this.finish();
            }
        }, TEMPO_DE_ESPERA);
        EdgeToEdge.enable(this);
        EdgeToEdge.enable(this);
        this.setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(this.findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}