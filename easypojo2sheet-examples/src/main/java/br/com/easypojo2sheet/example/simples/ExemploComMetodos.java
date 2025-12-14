package br.com.easypojo2sheet.example.simples;

import br.com.easypojo2sheet.annotation.SheetColumn;
import br.com.easypojo2sheet.annotation.Spreadsheet;
import br.com.easypojo2sheet.api.ExcelExporter;
import br.com.easypojo2sheet.model.enums.HorizontalAlignment;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static br.com.easypojo2sheet.api.ExcelExporter.builder;

public class ExemploComMetodos {

    @Spreadsheet(name = "Vendas com Cálculos", autoSizeColumns = true, freezeHeader = true)
    public static class VendaComCalculos {
        @SheetColumn(name = "ID", order = 1, align = HorizontalAlignment.CENTER)
        private Long id;

        @SheetColumn(name = "Cliente", order = 2)
        private String cliente;

        @SheetColumn(name = "Data", order = 3, dateFormat = "dd/MM/yyyy", align = HorizontalAlignment.CENTER)
        private LocalDate data;

        @SheetColumn(name = "Valor Bruto", order = 4, numberFormat = "R$ #,##0.00", align = HorizontalAlignment.RIGHT)
        private BigDecimal valorBruto;

        @SheetColumn(name = "% Desconto", order = 5)
        private BigDecimal percentualDesconto;

        // Campos não exportados diretamente
        private BigDecimal custoOperacional;

        public VendaComCalculos(Long id, String cliente, LocalDate data, BigDecimal valorBruto, 
                               BigDecimal percentualDesconto, BigDecimal custoOperacional) {
            this.id = id;
            this.cliente = cliente;
            this.data = data;
            this.valorBruto = valorBruto;
            this.percentualDesconto = percentualDesconto;
            this.custoOperacional = custoOperacional;
        }

        /**
         * Método anotado: calcula valor do desconto
         */
        @SheetColumn(name = "Valor Desconto", order = 6, numberFormat = "R$ #,##0.00", align = HorizontalAlignment.RIGHT)
        public BigDecimal calcularValorDesconto() {
            return valorBruto.multiply(percentualDesconto).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
        }

        /**
         * Método anotado: calcula valor líquido
         */
        @SheetColumn(name = "Valor Líquido", order = 7, numberFormat = "R$ #,##0.00", align = HorizontalAlignment.RIGHT)
        public BigDecimal getValorLiquido() {
            return valorBruto.subtract(calcularValorDesconto());
        }

        /**
         * Método anotado: calcula margem de lucro
         */
        @SheetColumn(name = "Margem Lucro %", order = 8, numberFormat = "#,##0.00", align = HorizontalAlignment.RIGHT)
        public BigDecimal calcularMargemLucro() {
            BigDecimal lucro = getValorLiquido().subtract(custoOperacional);
            if (getValorLiquido().compareTo(BigDecimal.ZERO) == 0) {
                return BigDecimal.ZERO;
            }
            return lucro.divide(getValorLiquido(), 4, RoundingMode.HALF_UP)
                       .multiply(new BigDecimal("100"))
                       .setScale(2, RoundingMode.HALF_UP);
        }

        /**
         * Método anotado: retorna status da venda
         */
        @SheetColumn(name = "Status", order = 9, align = HorizontalAlignment.CENTER)
        public String getStatusVenda() {
            BigDecimal margem = calcularMargemLucro();
            if (margem.compareTo(new BigDecimal("20")) >= 0) {
                return "ÓTIMA";
            } else if (margem.compareTo(new BigDecimal("10")) >= 0) {
                return "BOA";
            } else if (margem.compareTo(BigDecimal.ZERO) >= 0) {
                return "REGULAR";
            } else {
                return "PREJUÍZO";
            }
        }

        /**
         * Método anotado: formata resumo
         */
        @SheetColumn(name = "Resumo", order = 10)
        public String getResumo() {
            return String.format("%s - %s (Margem: %.2f%%)", 
                cliente, 
                getStatusVenda(), 
                calcularMargemLucro());
        }
    }

    public static void main(String[] args) {
        List<VendaComCalculos> vendas = Arrays.asList(
            new VendaComCalculos(1L, "Empresa A", LocalDate.of(2024, 1, 15), 
                new BigDecimal("10000.00"), new BigDecimal("10"), new BigDecimal("7000.00")),
            new VendaComCalculos(2L, "Empresa B", LocalDate.of(2024, 1, 16), 
                new BigDecimal("25000.00"), new BigDecimal("5"), new BigDecimal("15000.00")),
            new VendaComCalculos(3L, "Empresa C", LocalDate.of(2024, 1, 17), 
                new BigDecimal("8000.00"), new BigDecimal("15"), new BigDecimal("7500.00")),
            new VendaComCalculos(4L, "Empresa D", LocalDate.of(2024, 1, 18), 
                new BigDecimal("50000.00"), new BigDecimal("8"), new BigDecimal("35000.00"))
        );

        try {
            ExcelExporter.<VendaComCalculos>builder()
                .data(vendas)
                .outputFile("vendas-com-calculos.xlsx")
                .build()
                .export();

            System.out.println("Arquivo 'vendas-com-calculos.xlsx' gerado com sucesso!");

        } catch (Exception e) {
            System.err.println("Erro ao gerar Excel: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
