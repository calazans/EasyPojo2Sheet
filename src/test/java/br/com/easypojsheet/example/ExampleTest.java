package br.com.easypojsheet.example;

import br.com.easypojsheet.api.ExcelExporter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Exemplo de uso da biblioteca.
 */
public class ExampleTest {

    public static void main(String[] args) {
        // Cria dados de exemplo
        List<RelatorioVenda> vendas = List.of(
           new RelatorioVenda(
                1L,
                LocalDate.of(2025, 1, 15),
                "João Silva",
                new Funcionario("Maria Santos", "123.456.789-00"),
                new BigDecimal("1500.00"),
                new BigDecimal("150.00")
            ),new RelatorioVenda(
                2L,
                LocalDate.of(2025, 1, 16),
                "Ana Costa",
                new Funcionario("Pedro Oliveira", "987.654.321-00"),
                new BigDecimal("2300.50"),
                new BigDecimal("230.05")
            ),new RelatorioVenda(
                3L,
                LocalDate.of(2025, 1, 17),
                "Carlos Souza",
                new Funcionario("Maria Santos", "123.456.789-00"),
                new BigDecimal("980.00"),
                new BigDecimal("98.00")
            )
        );

        // Exporta para Excel
        try {
            ExcelExporter.<RelatorioVenda>builder()
                .data(vendas)
                .outputFile("relatorio-vendas.xlsx")
                .build()
                .export();
            
            System.out.println("✅ Arquivo gerado com sucesso: relatorio-vendas.xlsx");
        } catch (Exception e) {
            System.err.println("❌ Erro ao gerar arquivo: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
