package com.example.projeto_v1.view;

import android.content.Intent;
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

public class CadastroClienteActivity extends AppCompatActivity {

    private ClienteViewModel viewModel;
    private EditText edtNome, edtEmail, edtSenha, edtConfirmSenha;
    private Button btnCriarConta;

    public void goTelaLogin (View view) {
        Intent intent = new Intent(CadastroClienteActivity.this, LoginClienteActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_cliente);
        EdgeToEdge.enable(this);

        edtNome = findViewById(R.id.edtNome);
        edtEmail = findViewById(R.id.edtEmail);
        edtSenha = findViewById(R.id.edtSenha);
        edtConfirmSenha = findViewById(R.id.edtConfirmSenha);
        btnCriarConta = findViewById(R.id.btnCriarConta);

        viewModel = new ViewModelProvider(this).get(ClienteViewModel.class);

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
                Toast.makeText(this, "A senha e a confirmação de senha não coincidem!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (senha.length() < 8 || confirmSenha.length() < 8) {
                Toast.makeText(this, "A senha deve conter no mínimo 8 caracteres!", Toast.LENGTH_SHORT).show();
                return;
            }

            viewModel.cadastrarCliente(nome, email, senha).observe(this, sucesso -> {
                if (sucesso != null && sucesso) {
                    Toast.makeText(this, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show();
                    goTelaLogin(null);
                } else {
                    Toast.makeText(this, "Erro ao cadastrar!", Toast.LENGTH_SHORT).show();
                }
            });
        });

        View rootView = findViewById(android.R.id.content);

        ViewCompat.setOnApplyWindowInsetsListener(rootView, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}