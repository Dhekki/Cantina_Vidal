package com.example.projeto_v1.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.projeto_v1.R;
import com.example.projeto_v1.network.RetrofitClient;
import com.example.projeto_v1.utils.SessionManager;
import com.example.projeto_v1.viewmodel.UploadViewModel;
import com.example.projeto_v1.viewmodel.UserViewModel;
import com.google.android.material.button.MaterialButton;

public class PerfilClienteActivity extends AppCompatActivity {

    private SessionManager sessionManager;
    private View includeVisualizar, includeEditar;
    private ImageView btnAbrirEdicao, btnFecharEdicao, btnTrocarFoto;
    private ImageView imgPerfilView, imgPerfilEdit;
    private TextView txtTitulo;

    // Campos de Visualização
    private TextView txtNomeUsuario, txtEmailUsuario;

    // Campos de Edição
    private EditText edtNomeEditar; // <--- NOVO: Para pegar o nome digitado

    private UploadViewModel uploadViewModel;
    private UserViewModel userViewModel;

    private Uri imagemSelecionadaUri = null;
    private String currentImageUrl = null; // <--- NOVO: Para guardar a URL atual caso não troque a foto
    private boolean isExpanded = false;

    private final ActivityResultLauncher<String> selecionarImagemLauncher = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            uri -> {
                if (uri != null) {
                    imagemSelecionadaUri = uri;
                    imgPerfilEdit.setImageURI(uri);
                }
            }
    );

    // Navegação inferior (Exemplo)
    public void goNavegacaoHome(View view) {
        startActivity(new Intent(this, HomeClienteActivity.class));
        finish();
    }

    public void goNavegacaoPedidos(View view) {
        startActivity(new Intent(this, PedidosClienteActivity.class));
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_cliente);

        sessionManager = new SessionManager(this);
        uploadViewModel = new ViewModelProvider(this).get(UploadViewModel.class);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        inicializarComponentes();
        configurarCliques();
        carregarDadosUsuario();
    }

    private void inicializarComponentes() {
        // Includes
        includeVisualizar = findViewById(R.id.include_visualizar);
        includeEditar = findViewById(R.id.include_editar);

        // Botões de controle da tela
        btnAbrirEdicao = findViewById(R.id.btn_abrir_edicao);
        btnFecharEdicao = findViewById(R.id.btn_fechar_edicao);
        txtTitulo = findViewById(R.id.txt_titulo_perfil);

        // Elementos VISUALIZAR
        txtNomeUsuario = includeVisualizar.findViewById(R.id.txt_nome_cliente);
        txtEmailUsuario = includeVisualizar.findViewById(R.id.txt_email_cliente);
        imgPerfilView = includeVisualizar.findViewById(R.id.img_perfil_cliente_view);

        // Elementos EDITAR
        imgPerfilEdit = includeEditar.findViewById(R.id.img_perfil_cliente_edit);
        btnTrocarFoto = includeEditar.findViewById(R.id.btn_trocar_foto);
        edtNomeEditar = includeEditar.findViewById(R.id.edt_nome_editar); // <--- Inicializando o EditText
    }

    private void configurarCliques() {
        final RelativeLayout btnMaisOpcoes = includeVisualizar.findViewById(R.id.btn_mais_opcoes);
        final LinearLayout layoutOculto = includeVisualizar.findViewById(R.id.layout_opcoes_ocultas);
        final ImageView imgSeta = includeVisualizar.findViewById(R.id.img_seta_mais_opcoes);

        // Expandir opções
        btnMaisOpcoes.setOnClickListener(v -> {
            isExpanded = !isExpanded;
            layoutOculto.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
            imgSeta.animate().rotation(isExpanded ? 180f : 0f).setDuration(200).start();
        });

        // Botão Sair
        MaterialButton btnSair = includeVisualizar.findViewById(R.id.btn_sair_app);
        btnSair.setOnClickListener(v -> realizarLogout());

        // Botão Excluir
        MaterialButton btnExcluir = includeVisualizar.findViewById(R.id.btn_excluir_conta);
        btnExcluir.setOnClickListener(v -> Toast.makeText(this, "Em breve...", Toast.LENGTH_SHORT).show());

        // Controles de Edição
        btnAbrirEdicao.setOnClickListener(v -> toggleEditMode(true));

        btnFecharEdicao.setOnClickListener(v -> {
            toggleEditMode(false);
            carregarDadosUsuario(); // Recarrega os dados originais se cancelar
            imagemSelecionadaUri = null; // Limpa seleção de imagem
        });

        btnTrocarFoto.setOnClickListener(v -> selecionarImagemLauncher.launch("image/*"));

        // --- LÓGICA DO BOTÃO SALVAR ---
        MaterialButton btnSalvar = includeEditar.findViewById(R.id.btn_salvar_alteracoes);
        btnSalvar.setOnClickListener(v -> prepararSalvamento());
    }

    // --- NOVA LÓGICA DE SALVAMENTO ---
    private void prepararSalvamento() {
        String novoNome = edtNomeEditar.getText().toString().trim();
        String token = getToken();

        if (novoNome.isEmpty()) {
            Toast.makeText(this, "O nome não pode ser vazio", Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(this, "Salvando alterações...", Toast.LENGTH_SHORT).show();

        if (imagemSelecionadaUri != null) {
            // --- CAMINHO A: USUÁRIO SELECIONOU NOVA FOTO ---
            uploadViewModel.fazerUploadImagem(this, imagemSelecionadaUri, token)
                    .observe(this, uploadResponse -> {
                        if (uploadResponse != null) {
                            String novaUrlRelativa = uploadResponse.getRelativePath();

                            // --- DEBUG: OLHE NO LOGCAT ---
                            android.util.Log.d("DEBUG_PERFIL", "Upload Sucesso! URL retornada: " + novaUrlRelativa);

                            // Verifica se a URL veio vazia
                            if (novaUrlRelativa == null || novaUrlRelativa.isEmpty()) {
                                Toast.makeText(this, "Erro: Servidor não retornou o caminho da imagem", Toast.LENGTH_LONG).show();
                                return;
                            }

                            atualizarPerfilNoBackend(token, novoNome, novaUrlRelativa);
                        } else {
                            Toast.makeText(this, "Erro ao enviar imagem (Upload falhou)", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            // --- CAMINHO B: MANTEVE A FOTO ANTIGA ---
            android.util.Log.d("DEBUG_PERFIL", "Mantendo foto antiga: " + currentImageUrl);
            atualizarPerfilNoBackend(token, novoNome, currentImageUrl);
        }
    }

    private void atualizarPerfilNoBackend(String token, String nome, String imagemUrl) {
        userViewModel.atualizarPerfil(token, nome, imagemUrl)
                .observe(this, userResponse -> {
                    if (userResponse != null) {
                        Toast.makeText(this, "Perfil atualizado com sucesso!", Toast.LENGTH_SHORT).show();

                        // Atualiza UI com os dados novos
                        atualizarCamposUI(userResponse);

                        // Limpa estados temporários
                        imagemSelecionadaUri = null;
                        toggleEditMode(false);
                    } else {
                        Toast.makeText(this, "Erro ao atualizar perfil", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // --- MÉTODOS AUXILIARES ---

    private void carregarDadosUsuario() {
        String token = getToken();
        if (token == null || token.isEmpty()) {
            realizarLogout();
            return;
        }

        userViewModel.carregarPerfil(token).observe(this, userResponse -> {
            if (userResponse != null) {
                atualizarCamposUI(userResponse);
            } else {
                Toast.makeText(this, "Sessão expirada", Toast.LENGTH_SHORT).show();
                realizarLogout();
            }
        });
    }

    private void atualizarCamposUI(com.example.projeto_v1.model.UserResponse user) {
        // Guarda a URL atual para uso posterior
        currentImageUrl = user.getImageUrl();

        // 1. Atualiza modo Visualização
        if (txtNomeUsuario != null) txtNomeUsuario.setText(user.getName());
        if (txtEmailUsuario != null) txtEmailUsuario.setText(user.getEmail());

        // 2. Atualiza modo Edição (Preenche o EditText com o nome atual)
        if (edtNomeEditar != null) edtNomeEditar.setText(user.getName());

        // 3. Carrega Imagens
        if (user.getImageUrl() != null && !user.getImageUrl().isEmpty()) {
            String urlCompleta = RetrofitClient.BASE_IMAGE_URL + user.getImageUrl();
            carregarImagemComGlide(urlCompleta);
        }
    }

    private void carregarImagemComGlide(String url) {
        // Timestamp força o Glide a baixar a imagem de novo
        long timeStamp = System.currentTimeMillis();

        // Configuração para visualização
        Glide.with(this)
                .load(url)
                .signature(new com.bumptech.glide.signature.ObjectKey(timeStamp)) // <--- OBRIGATÓRIO
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.icon_perfil3)
                .error(R.drawable.icon_perfil3)
                .into(imgPerfilView);

        // Configuração para edição
        Glide.with(this)
                .load(url)
                .signature(new com.bumptech.glide.signature.ObjectKey(timeStamp)) // <--- OBRIGATÓRIO
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.icon_perfil3)
                .error(R.drawable.icon_perfil3)
                .into(imgPerfilEdit);
    }

    private void realizarLogout() {
        sessionManager.clearSession();
        Intent intent = new Intent(this, LoginClienteActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void toggleEditMode(boolean editing) {
        // Lógica de Visibilidade (Igual antes)
        includeVisualizar.setVisibility(editing ? View.GONE : View.VISIBLE);
        includeEditar.setVisibility(editing ? View.VISIBLE : View.GONE);
        btnAbrirEdicao.setVisibility(editing ? View.GONE : View.VISIBLE);
        btnFecharEdicao.setVisibility(editing ? View.VISIBLE : View.GONE);
        txtTitulo.setText(editing ? "Editar perfil" : "Perfil");

        // --- CORREÇÃO AQUI ---
        // Se estamos ATIVANDO o modo de edição, preenchemos os campos
        if (editing) {
            // 1. Pega o nome atual do TextView e coloca no EditText
            String nomeAtual = txtNomeUsuario.getText().toString();
            edtNomeEditar.setText(nomeAtual);
            // Move o cursor para o final do texto (melhora a UX)
            edtNomeEditar.setSelection(edtNomeEditar.getText().length());

            // 2. Garante que a imagem de edição esteja sincronizada
            // Se tiver URL salva, carrega ela. Se não, o Glide já mostra o placeholder por padrão.
            if (currentImageUrl != null && !currentImageUrl.isEmpty()) {
                String urlCompleta = RetrofitClient.BASE_IMAGE_URL + currentImageUrl;

                // Força o carregamento na imagem de edição especificamente
                Glide.with(this)
                        .load(urlCompleta)
                        .diskCacheStrategy(DiskCacheStrategy.ALL) // Pode usar cache aqui pra ser rápido
                        .placeholder(R.drawable.icon_perfil3)
                        .error(R.drawable.icon_perfil3)
                        .into(imgPerfilEdit);
            } else {
                // Se não tem URL (caso do seu teste), reseta para o ícone padrão
                imgPerfilEdit.setImageResource(R.drawable.icon_perfil3);
            }
        }
    }

    private String getToken() {
        return sessionManager.fetchAuthToken();
    }
}