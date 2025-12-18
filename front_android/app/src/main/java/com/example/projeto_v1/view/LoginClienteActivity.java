package com.example.projeto_v1.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.projeto_v1.R;
import com.example.projeto_v1.utils.SessionManager;
import com.example.projeto_v1.viewmodel.AuthViewModel;

public class LoginClienteActivity extends AppCompatActivity {

    private AuthViewModel authViewModel;
    private SessionManager sessionManager;
    private EditText edtEmail, edtSenha;
    private Button btnEntrar;
    private TextView txtIrParaCadastro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_cliente); // Certifique-se que o XML tem esse nome

        // 1. Inicializa SessionManager e ViewModel
        sessionManager = new SessionManager(this);
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        // 2. Verifica se já está logado (Opcional: Auto-login)
        if (sessionManager.fetchAuthToken() != null) {
            abrirPerfil();
        }

        inicializarComponentes();
        configurarCliques();
    }

    private void inicializarComponentes() {
        edtEmail = findViewById(R.id.edtEmailLogin); // Verifique o ID no seu XML
        edtSenha = findViewById(R.id.edtSenhaLogin); // Verifique o ID no seu XML
        btnEntrar = findViewById(R.id.btnEntrar);     // Verifique o ID no seu XML
        txtIrParaCadastro = findViewById(R.id.txtIrParaCadastro); // Verifique o ID no seu XML
    }

    private void configurarCliques() {
        // Botão de Login
        btnEntrar.setOnClickListener(v -> {
            String email = edtEmail.getText().toString();
            String senha = edtSenha.getText().toString();

            if (email.isEmpty() || senha.isEmpty()) {
                Toast.makeText(this, "Preencha email e senha", Toast.LENGTH_SHORT).show();
                return;
            }

            fazerLogin(email, senha);
        });

        // Link para tela de cadastro
        txtIrParaCadastro.setOnClickListener(v -> {
            Intent intent = new Intent(LoginClienteActivity.this, CadastroClienteActivity.class);
            startActivity(intent);
        });
    }

    private void fazerLogin(String email, String senha) {
        // Feedback visual simples
        btnEntrar.setEnabled(false);
        btnEntrar.setText("Entrando...");

        authViewModel.fazerLogin(email, senha).observe(this, loginResponse -> {
            btnEntrar.setEnabled(true);
            btnEntrar.setText("Entrar");

            if (loginResponse != null && loginResponse.getAccessToken() != null) {
                // SUCESSO!
                // 1. Salva o token usando o SessionManager
                sessionManager.saveAuthToken(loginResponse.getAccessToken(), loginResponse.getName());

                // 2. Feedback e Navegação
                Toast.makeText(this, "Bem-vindo(a) " + loginResponse.getName(), Toast.LENGTH_SHORT).show();
                abrirPerfil();
            } else {
                // ERRO
                Toast.makeText(this, "Email ou senha inválidos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void abrirPerfil() {
        Intent intent = new Intent(LoginClienteActivity.this, HomeClienteActivity.class);
        startActivity(intent);
        finish(); // Impede que o usuário volte para o login ao apertar "voltar"
    }
}