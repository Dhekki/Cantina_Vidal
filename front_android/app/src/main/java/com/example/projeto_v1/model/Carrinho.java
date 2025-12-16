package com.example.projeto_v1.model;

import java.util.ArrayList;
import java.util.List;

public class Carrinho {

    private static final Carrinho instance = new Carrinho();

    private final List<Produto> produtos = new ArrayList<>();

    private Carrinho() {}

    public static Carrinho getInstance() {
        return instance;
    }

    public List<Produto> getProdutos() {
        return produtos;
    }

    public void adicionarProduto(Produto produto) {
        for (Produto p : produtos) {
            if (p.getNome().equals(produto.getNome())) {
                p.setQuantidade(p.getQuantidade() + produto.getQuantidade());
                return;
            }
        }
        produtos.add(produto);
    }

    public void removerProduto(Produto produto) {
        produtos.remove(produto);
    }

    public void limpar() {
        produtos.clear();
    }

    public void setProdutos(List<Produto> novosProdutos) {
        produtos.clear();
        for (Produto p : novosProdutos) {
            produtos.add(p);
        }
    }
}