package br.com.easypojo2sheet.example.simples;

import br.com.easypojo2sheet.annotation.SheetColumn;
import br.com.easypojo2sheet.annotation.Spreadsheet;
import br.com.easypojo2sheet.model.enums.HorizontalAlignment;

import java.math.BigDecimal;

@Spreadsheet(name = "Lista de Produtos", autoSizeColumns = true, freezeHeader = true)
public record Produto(
        @SheetColumn(name = "SKU", order = 1, align = HorizontalAlignment.CENTER)
        String sku,

        @SheetColumn(name = "Nome", order = 2)
        String nome,

        @SheetColumn(name = "Valor Unitário", order = 3, numberFormat = "R$ #,##0.00", align = HorizontalAlignment.RIGHT)
        BigDecimal valorUnitario
) {
    public Produto(String sku, String nome, double valorUnitario) {
        this(sku, nome, BigDecimal.valueOf(valorUnitario));
    }
}
