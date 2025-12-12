package br.com.easypojo2sheet.example;


import br.com.easypojo2sheet.annotation.SheetColumn;
import br.com.easypojo2sheet.annotation.SheetIgnore;
import br.com.easypojo2sheet.annotation.Spreadsheet;
import br.com.easypojo2sheet.model.enums.HorizontalAlignment;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Spreadsheet(name = "Relatório de Vendas", autoSizeColumns = true, freezeHeader = true)
public record RelatorioVenda(
        @SheetColumn(name = "Código", order = 1, align = HorizontalAlignment.CENTER)
        Long id,

        @SheetColumn(name = "Data", order = 5, dateFormat = "dd/MM/yyyy", align = HorizontalAlignment.CENTER)
        LocalDate dataVenda,

        @SheetColumn(name = "Cliente", order = 2)
        String nomeCliente,

        @SheetColumn(name = "1º Produto vendido", order = 3, property = "produtos[0].nome", align = HorizontalAlignment.CENTER)
        @SheetColumn(name = "Quantidade  Produtos Vendidos", order = 4, property = "produtos.size", align = HorizontalAlignment.CENTER)
        List<Produto> produtos,

        @SheetColumn(name = "Vendedor", order = 3, property = "vendedor.nome", align = HorizontalAlignment.CENTER)
        @SheetColumn(name = "CPF Vendedor", order = 4, property = "vendedor.cpf", align = HorizontalAlignment.CENTER)
        Funcionario vendedor,

        @SheetColumn(name = "Valor", order = 6, numberFormat = "R$ #,##0.00", align = HorizontalAlignment.RIGHT)
        BigDecimal valorTotal,

        @SheetColumn(name = "Comissão", order = 7, numberFormat = "R$ #,##0.00", align = HorizontalAlignment.RIGHT)
        BigDecimal comissao,

        @SheetIgnore
        String observacoesInternas
) {
}
