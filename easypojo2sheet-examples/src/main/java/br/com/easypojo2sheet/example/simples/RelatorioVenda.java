package br.com.easypojo2sheet.example.simples;


import br.com.easypojo2sheet.annotation.SheetColumn;
import br.com.easypojo2sheet.annotation.SheetIgnore;
import br.com.easypojo2sheet.annotation.Spreadsheet;
import br.com.easypojo2sheet.model.enums.HorizontalAlignment;
import br.com.easypojo2sheet.model.enums.ListRenderStrategy;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Spreadsheet(name = "Relatório de Vendas", autoSizeColumns = true, freezeHeader = true)
public record RelatorioVenda(
        @SheetColumn(name = "Código", order = 1, align = HorizontalAlignment.CENTER)
        Long id,

        @SheetColumn(name = "Data", order = 2, dateFormat = "dd/MM/yyyy", align = HorizontalAlignment.CENTER)
        LocalDate dataVenda,

        @SheetColumn(name = "Cliente", order = 3)
        String nomeCliente,

//        @SheetColumn(name = "1º Produto vendido", order = 7, property = "produtos.first.nome", align = HorizontalAlignment.CENTER)
//        @SheetColumn(name = "Quantidade  Produtos Vendidos", order = 8, property = "produtos.size", align = HorizontalAlignment.CENTER)

        // Concatenação

//        @SheetColumn(name = "SKUs", order = 11, property = "produtos.join.sku", separator = ", ")

        // Agregações numéricas
        @SheetColumn(name = "Valor Total Produtos", order = 8, property = "produtos.sum.valorUnitario", numberFormat = "R$ #,##0.00", align = HorizontalAlignment.RIGHT)
        @SheetColumn(name = "Preço Médio Produtos", order = 9, property = "produtos.avg.valorUnitario", numberFormat = "R$ #,##0.00", align = HorizontalAlignment.RIGHT)
//        @SheetColumn(name = "Menor Preço", order = 14, property = "produtos.min.valorUnitario", numberFormat = "R$ #,##0.00", align = HorizontalAlignment.RIGHT)
//        @SheetColumn(name = "Maior Preço", order = 15, property = "produtos.max.valorUnitario", numberFormat = "R$ #,##0.00", align = HorizontalAlignment.RIGHT)
        @SheetColumn(name = "Nome Produto", order = 10, property = "nome", listStrategy = ListRenderStrategy.EXPAND_ROWS)
        @SheetColumn(name = "Valor Unitario Produto", order = 11, property = "valorUnitario",numberFormat = "R$ #,##0.00" , listStrategy = ListRenderStrategy.EXPAND_ROWS)
        List<Produto> produtos,

        @SheetColumn(name = "Vendedor", order = 4, property = "vendedor.nome", align = HorizontalAlignment.CENTER)
        @SheetColumn(name = "CPF Vendedor", order = 5, property = "vendedor.cpf", align = HorizontalAlignment.CENTER)
        Funcionario vendedor,

        @SheetColumn(name = "Valor", order = 6, numberFormat = "R$ #,##0.00", align = HorizontalAlignment.RIGHT)
        BigDecimal valorTotal,

        @SheetColumn(name = "Comissão", order = 7, numberFormat = "R$ #,##0.00", align = HorizontalAlignment.RIGHT)
        BigDecimal comissao,

        @SheetIgnore
        String observacoesInternas
) {
}
