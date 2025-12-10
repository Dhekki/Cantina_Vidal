package com.example.projeto_v1.view;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.projeto_v1.R;
import com.example.projeto_v1.viewmodel.ClienteViewModel;

public class LoginClienteActivity extends AppCompatActivity {

    private ClienteViewModel viewModel;
    EditText edtEmail2, edtSenha2;
    Button btnLogin;

    public void goTelaCadastro (View view) {
        Intent intent = new Intent(LoginClienteActivity.this, CadastroClienteActivity.class);
        startActivity(intent);
        finish();
    }

    public void goTelaRecuperarSenha (View view) {
        Intent intent = new Intent(LoginClienteActivity.this, RecuperarSenhaActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_cliente);
        EdgeToEdge.enable(this);

        edtEmail2 = findViewById(R.id.edtEmail2);
        edtSenha2 = findViewById(R.id.edtSenha2);
        btnLogin = findViewById(R.id.btnLogin);

        viewModel = new ViewModelProvider(this).get(ClienteViewModel.class);

        btnLogin.setOnClickListener(v -> {
            String email = edtEmail2.getText().toString().trim();
            String senha = edtSenha2.getText().toString();

            // Validação
//            if (email.isEmpty() || senha.isEmpty()) {
//                Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
//                return;
//            }

            // Realiza login
            viewModel.realizarLogin(email, senha).observe(this, sucesso -> {
                //if (sucesso != null && sucesso) {
                    Toast.makeText(this, "Login realizado com sucesso!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginClienteActivity.this, HomeClienteActivity.class);
                    startActivity(intent);
                    finish();
                //} else {
                //    Toast.makeText(this, "Email ou senha incorretos!", Toast.LENGTH_SHORT).show();
                //}
            });
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}