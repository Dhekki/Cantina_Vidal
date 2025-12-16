//package com.example.projeto_v1.view;
//
//import android.net.Uri;
//import android.os.Bundle;
//import android.util.Log;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.Toast;
//
//import androidx.activity.result.ActivityResultLauncher;
//import androidx.activity.result.contract.ActivityResultContracts;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.lifecycle.ViewModelProvider;
//
//import com.bumptech.glide.Glide;
//import com.bumptech.glide.load.engine.DiskCacheStrategy;
//import com.example.projeto_v1.R;
//import com.example.projeto_v1.network.RetrofitClient;
//import com.example.projeto_v1.viewmodel.UploadViewModel;
//
//public class TesteUploadActivity extends AppCompatActivity {
//
//    private UploadViewModel uploadViewModel;
//    private ImageView imageViewPreview;
//    private Button btnSelecionarFoto;
//    private Button btnEnviarFoto;
//
//    private Uri imagemSelecionadaUri = null;
//
//    // Lançador para abrir a galeria
//    private final ActivityResultLauncher<String> selecionarImagemLauncher = registerForActivityResult(
//            new ActivityResultContracts.GetContent(),
//            uri -> {
//                if (uri != null) {
//                    imagemSelecionadaUri = uri;
//                    imageViewPreview.setImageURI(uri); // Mostra prévia na tela
//                    btnEnviarFoto.setEnabled(true); // Habilita o botão de enviar
//                }
//            }
//    );
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        // Dica: Crie um layout simples com 1 ImageView e 2 Buttons para testar
//        setContentView(R.layout.activity_teste_upload);
//
//        // Inicializa o ViewModel
//        uploadViewModel = new ViewModelProvider(this).get(UploadViewModel.class);
//
//        imageViewPreview = findViewById(R.id.imagePreview);
//        btnSelecionarFoto = findViewById(R.id.btnSelecionar);
//        btnEnviarFoto = findViewById(R.id.btnEnviar);
//
//        btnEnviarFoto.setEnabled(false); // Desabilita até selecionar uma foto
//
//        // 1. Botão para abrir a galeria
//        btnSelecionarFoto.setOnClickListener(v -> {
//            selecionarImagemLauncher.launch("image/*");
//        });
//
//        // 2. Botão para fazer o upload
//        btnEnviarFoto.setOnClickListener(v -> {
//            if (imagemSelecionadaUri != null) {
//                fazerUpload();
//            } else {
//                Toast.makeText(this, "Selecione uma imagem primeiro", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    private void fazerUpload() {
//        // Token temporário ou pego do SharedPreferences/Sessão
//        String token = "";
//
//        Toast.makeText(this, "Enviando...", Toast.LENGTH_SHORT).show();
//
//        // CHAMA SUA LÓGICA AQUI
//        uploadViewModel.fazerUploadImagem(this, imagemSelecionadaUri, token)
//                .observe(this, response -> {
//                    if (response != null) {
//                        // 1. Montar a URL completa
//                        // O servidor retorna algo como "uploads/imagem123.jpg" no relativePath
//                        String urlCompleta = RetrofitClient.BASE_IMAGE_URL + response.getRelativePath();
//
//                        Log.d("UPLOAD", "Carregando imagem de: " + urlCompleta);
//
//                        // 2. Usar Glide para carregar a imagem do servidor no ImageView
//                        Glide.with(this)
//                                .load(urlCompleta)
//                                .skipMemoryCache(true) // Opcional: evita cache se você estiver testando upload da mesma imagem repetidamente
//                                .diskCacheStrategy(DiskCacheStrategy.NONE) // Opcional: para testes
//                                .placeholder(android.R.drawable.ic_menu_upload) // Imagem enquanto carrega
//                                .error(android.R.drawable.stat_notify_error) // Imagem se der erro
//                                .into(imageViewPreview); // O mesmo ImageView que usou para o preview local
//
//                        Toast.makeText(this, "Upload concluído! Mostrando imagem do servidor.", Toast.LENGTH_LONG).show();
//
//                        // Opcional: Limpar a URI local para garantir que estamos vendo a do servidor
//                        imagemSelecionadaUri = null;
//                        btnEnviarFoto.setEnabled(false);
//
//                    } else {
//                        Log.e("UPLOAD", "Falha no upload");
//                        Toast.makeText(this, "Erro ao enviar imagem", Toast.LENGTH_SHORT).show();
//                    }
//                });
//    }
//}