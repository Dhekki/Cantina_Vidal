package com.example.projeto_v1.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projeto_v1.R;
import com.example.projeto_v1.model.Cliente;

import java.util.List;

public class ClienteAdapter extends RecyclerView.Adapter<ClienteAdapter.ViewHolder> {


    private List<Cliente> lista;


    public ClienteAdapter(List<Cliente> lista) {
        this.lista = lista;
    }


    public void atualizarLista(List<Cliente> novaLista) {
        this.lista = novaLista;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_cadastro_cliente, parent, false);
        return new ViewHolder(item);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Cliente c = lista.get(position);
//        holder.txtNome.setText(c.getNome());
//        holder.txtEmail.setText(c.getEmail());
//        holder.txtSenha.setText(c.getSenha());
    }


    @Override
    public int getItemCount() {
        return lista != null ? lista.size() : 0;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {


//        TextView txtNome, txtEmail, txtSenha;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
//            txtNome = itemView.findViewById(R.id.txtNome);
//            txtEmail = itemView.findViewById(R.id.txtEmail);
//            txtSenha = itemView.findViewById(R.id.txtSenha);
        }
    }
}
