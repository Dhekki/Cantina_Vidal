package com.example.projeto_v1.utils;

import com.example.projeto_v1.model.Produto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CarrinhoManager {

    // CORREÇÃO: Mudamos de <String, Produto> para <Long, Produto>
    private static Map<Long, Produto> produtosNoCarrinhoMap = new HashMap<>();

    public static void setProdutosNoCarrinho(List<Produto> todosProdutos) {
        if (todosProdutos == null) return;

        produtosNoCarrinhoMap = todosProdutos.stream()
                .filter(p -> p.getQuantidade() > 0) // Pega só o que tem quantidade
                .filter(p -> p.getId() != null)     // Segurança: ignora produtos sem ID
                // Agora o Produto::getId retorna Long, que casa com o Map<Long, ...>
                // O terceiro parâmetro (p1, p2) -> p1 serve para evitar crash se houver IDs duplicados
                .collect(Collectors.toMap(Produto::getId, p -> p, (p1, p2) -> p1));
    }

    public static void limparCarrinho() {
        produtosNoCarrinhoMap.clear();
    }

    // CORREÇÃO: Recebe Long id
    public static Produto getProdutoNoCarrinho(Long id) {
        return produtosNoCarrinhoMap.get(id);
    }

    // CORREÇÃO: Retorna mapa com chave Long
    public static Map<Long, Produto> getProdutosNoCarrinhoMap() {
        return produtosNoCarrinhoMap;
    }

    // CORREÇÃO: Recebe Long id
    public static boolean contemProduto(Long id) {
        return produtosNoCarrinhoMap.containsKey(id);
    }
}