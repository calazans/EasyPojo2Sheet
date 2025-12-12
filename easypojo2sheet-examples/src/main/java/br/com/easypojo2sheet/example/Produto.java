package br.com.easypojo2sheet.example;

import java.math.BigDecimal;

public record Produto(String sku, String nome, BigDecimal valorUnitario) {
    public Produto(String sku, String nome, double valorUnitario) {
        this(sku, nome, new BigDecimal(valorUnitario));
    }
}
