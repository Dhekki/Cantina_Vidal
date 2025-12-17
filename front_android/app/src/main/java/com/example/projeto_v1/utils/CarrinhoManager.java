package com.example.projeto_v1.utils;

import com.example.projeto_v1.model.Produto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CarrinhoManager {

    // Mapa usando String (UUID) como chave
    private static Map<String, Produto> produtosNoCarrinhoMap = new HashMap<>();

    public static void setProdutosNoCarrinho(List<Produto> todosProdutos) {
        if (todosProdutos == null) return;

        // Filtra apenas produtos com quantidade > 0 e mapeia pelo ID
        produtosNoCarrinhoMap = todosProdutos.stream()
                .filter(p -> p.getQuantidade() > 0)
                .collect(Collectors.toMap(Produto::getId, p -> p));
    }

    public static void limparCarrinho() {
        produtosNoCarrinhoMap.clear();
    }

    public static Produto getProdutoNoCarrinho(String id) {
        return produtosNoCarrinhoMap.get(id);
    }

    // --- MÉTODO QUE FALTAVA ---
    public static Map<String, Produto> getProdutosNoCarrinhoMap() {
        return produtosNoCarrinhoMap;
    }

    public static boolean contemProduto(String id) {
        return produtosNoCarrinhoMap.containsKey(id);
    }
}