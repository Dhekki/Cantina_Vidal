package com.example.projeto_v1.utils;

import com.example.projeto_v1.model.Produto;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CarrinhoManager {
    private static Map<Integer, Produto> produtosNoCarrinhoMap = new java.util.HashMap<>();

    public static Map<Integer, Produto> getProdutosNoCarrinhoMap() {
        return produtosNoCarrinhoMap;
    }

    public static void setProdutosNoCarrinho(List<Produto> todosProdutos) {
        produtosNoCarrinhoMap = todosProdutos.stream().filter(p -> p.getQuantidade() > 0).collect(Collectors.toMap(Produto::getId, p -> p));
    }

    public static void limparCarrinho() {
        if (produtosNoCarrinhoMap != null) {
            produtosNoCarrinhoMap.clear();
        }
    }
}