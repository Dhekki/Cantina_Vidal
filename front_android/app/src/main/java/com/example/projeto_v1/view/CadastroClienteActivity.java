package com.example.projeto_v1.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.projeto_v1.R;
import com.example.projeto_v1.viewmodel.AuthViewModel; // Importante: Use AuthViewModel

public class CadastroClienteActivity extends AppCompatActivity {

    private AuthViewModel viewModel; // Alterado para AuthViewModel
    private EditText edtNome, edtEmail, edtSenha, edtConfirmSenha;
    private Button btnCriarConta;

    public void goTelaLogin(View view) {
        Intent intent = new Intent(CadastroClienteActivity.this, LoginClienteActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_cliente);
        //EdgeToEdge.enable(this);
        // EdgeToEdge removido para simplificar, se precisar pode recolocar

        edtNome = findViewById(R.id.edtNome);
        edtEmail = findViewById(R.id.edtEmail);
        edtSenha = findViewById(R.id.edtSenha);
        edtConfirmSenha = findViewById(R.id.edtConfirmSenha);
        btnCriarConta = findViewById(R.id.btnCriarConta);

        // Inicializa o AuthViewModel
        viewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        btnCriarConta.setOnClickListener(v -> {
            String nome = edtNome.getText().toString();
            String email = edtEmail.getText().toString();
            String senha = edtSenha.getText().toString();
            String confirmSenha = edtConfirmSenha.getText().toString();

            if (nome.isEmpty() || email.isEmpty() || senha.isEmpty() || confirmSenha.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!senha.equals(confirmSenha)) {
                Toast.makeText(this, "Senhas não coincidem!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (senha.length() < 8) {
                Toast.makeText(this, "Senha muito curta (min 8)!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Chama o cadastro
            viewModel.fazerCadastro(nome, email, senha).observe(this, userResponse -> {
                if (userResponse != null) {
                    // Sucesso! O servidor retornou o usuário criado
                    Toast.makeText(this, "Conta criada com sucesso!", Toast.LENGTH_SHORT).show();
                    goTelaLogin(null); // Vai para o login
                } else {
                    // Erro (email já existe ou falha na API)
                    Toast.makeText(this, "Erro ao cadastrar. Verifique os dados.", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}