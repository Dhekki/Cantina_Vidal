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
        // Verifica se o produto já está na lista para não duplicar visualmente
        boolean existe = false;
        for (Produto p : produtos) {
            if (p.getNome().equals(produto.getNome())) {
                existe = true;
                // Se quiser somar quantidade aqui: p.setQuantidade(p.getQuantidade() + produto.getQuantidade());
                break;
            }
        }
        if (!existe) {
            produtos.add(produto);
        }
    }

    public void removerProduto(Produto produto) {
        produtos.remove(produto);
    }

    public void limpar() {
        produtos.clear();
    }

    public void setProdutos(List<Produto> novosProdutos) {
        produtos.clear();
        produtos.addAll(novosProdutos);
    }

    public double getValorTotal() {
        double total = 0;
        for (Produto p : produtos) {
            total += (p.getPreco() * p.getQuantidade());
        }
        return total;
    }
}