package com.example.projeto_v1.view;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
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
import com.example.projeto_v1.viewmodel.UploadViewModel;
import com.google.android.material.button.MaterialButton;

public class PerfilClienteActivity extends AppCompatActivity {

    private View includeVisualizar, includeEditar;
    private ImageView btnAbrirEdicao, btnFecharEdicao, btnTrocarFoto;
    private ImageView imgPerfilView, imgPerfilEdit;
    private TextView txtTitulo;
    private UploadViewModel uploadViewModel;
    private Uri imagemSelecionadaUri = null;
    private boolean isExpanded = false;

    // Lançador para abrir a galeria
    private final ActivityResultLauncher<String> selecionarImagemLauncher = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            uri -> {
                if (uri != null) {
                    imagemSelecionadaUri = uri;
                    imgPerfilEdit.setImageURI(uri); // Prévia local na tela de edição
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_cliente);

        uploadViewModel = new ViewModelProvider(this).get(UploadViewModel.class);
        inicializarComponentes();
        configurarCliques();
    }

    private void inicializarComponentes() {
        includeVisualizar = findViewById(R.id.include_visualizar);
        includeEditar = findViewById(R.id.include_editar);
        btnAbrirEdicao = findViewById(R.id.btn_abrir_edicao);
        btnFecharEdicao = findViewById(R.id.btn_fechar_edicao);
        txtTitulo = findViewById(R.id.txt_titulo_perfil);

        imgPerfilView = includeVisualizar.findViewById(R.id.img_perfil_cliente_view);
        imgPerfilEdit = includeEditar.findViewById(R.id.img_perfil_cliente_edit);
        btnTrocarFoto = includeEditar.findViewById(R.id.btn_trocar_foto);
    }

    private void configurarCliques() {
        final RelativeLayout btnMaisOpcoes = includeVisualizar.findViewById(R.id.btn_mais_opcoes);
        final LinearLayout layoutOculto = includeVisualizar.findViewById(R.id.layout_opcoes_ocultas);
        final ImageView imgSeta = includeVisualizar.findViewById(R.id.img_seta_mais_opcoes);

        btnMaisOpcoes.setOnClickListener(v -> {
            isExpanded = !isExpanded;
            layoutOculto.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
            imgSeta.animate().rotation(isExpanded ? 180f : 0f).setDuration(200).start();
        });

        btnAbrirEdicao.setOnClickListener(v -> toggleEditMode(true));
        btnFecharEdicao.setOnClickListener(v -> toggleEditMode(false));
        btnTrocarFoto.setOnClickListener(v -> selecionarImagemLauncher.launch("image/*"));

        MaterialButton btnSalvar = includeEditar.findViewById(R.id.btn_salvar_alteracoes);
        btnSalvar.setOnClickListener(v -> {
            if (imagemSelecionadaUri != null) {
                executarUpload();
            } else {
                toggleEditMode(false);
            }
        });
    }

    private void executarUpload() {
        String token = ""; // Insira a lógica para recuperar seu token
        Toast.makeText(this, "Salvando...", Toast.LENGTH_SHORT).show();

        uploadViewModel.fazerUploadImagem(this, imagemSelecionadaUri, token)
                .observe(this, response -> {
                    if (response != null) {
                        // Monta a URL vinda do servidor
                        String urlCompleta = RetrofitClient.BASE_IMAGE_URL + response.getRelativePath();

                        // Atualiza os dois ImageViews para a imagem não sumir
                        Glide.with(this)
                                .load(urlCompleta)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .placeholder(R.drawable.icon_perfil3)
                                .into(imgPerfilView);

                        Glide.with(this)
                                .load(urlCompleta)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .placeholder(R.drawable.icon_perfil3)
                                .into(imgPerfilEdit);

                        imagemSelecionadaUri = null;
                        toggleEditMode(false);
                        Toast.makeText(this, "Sucesso!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Erro no upload", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void toggleEditMode(boolean editing) {
        includeVisualizar.setVisibility(editing ? View.GONE : View.VISIBLE);
        includeEditar.setVisibility(editing ? View.VISIBLE : View.GONE);
        btnAbrirEdicao.setVisibility(editing ? View.GONE : View.VISIBLE);
        btnFecharEdicao.setVisibility(editing ? View.VISIBLE : View.GONE);
        txtTitulo.setText(editing ? "Editar perfil" : "Perfil");
    }
}