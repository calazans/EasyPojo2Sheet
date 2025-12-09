package br.com.easypojo2sheet.example;


import br.com.easypojo2sheet.annotation.SheetColumn;
import br.com.easypojo2sheet.annotation.SheetIgnore;
import br.com.easypojo2sheet.annotation.Spreadsheet;
import br.com.easypojo2sheet.model.enums.HorizontalAlignment;

import java.math.BigDecimal;
import java.time.LocalDate;

@Spreadsheet(name = "Relatório de Vendas", autoSizeColumns = true, freezeHeader = true)
public record RelatorioVenda(
        @SheetColumn(name = "Código", order = 1, align = HorizontalAlignment.CENTER)
        Long id,

        @SheetColumn(name = "Data", order = 4, dateFormat = "dd/MM/yyyy", align = HorizontalAlignment.CENTER)
        LocalDate dataVenda,

        @SheetColumn(name = "Cliente", order = 2)
        String nomeCliente,

        @SheetColumn(name = "Vendedor", order = 3, property = "vendedor.nome", align = HorizontalAlignment.CENTER)
        Funcionario vendedor,

        @SheetColumn(name = "Valor", order = 5, numberFormat = "R$ #,##0.00", align = HorizontalAlignment.RIGHT)
        BigDecimal valorTotal,

        @SheetColumn(name = "Comissão", order = 6, numberFormat = "R$ #,##0.00", align = HorizontalAlignment.RIGHT)
        BigDecimal comissao,

        @SheetIgnore
        String observacoesInternas
) {
}
