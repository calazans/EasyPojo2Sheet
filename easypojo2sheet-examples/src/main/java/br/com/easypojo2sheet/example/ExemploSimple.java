package br.com.easypojo2sheet.example;

import br.com.easypojo2sheet.api.ExcelExporter;

import java.util.List;

/**
 * Exemplo de uso da biblioteca.
 */
public class ExemploSimple {

    public static void main(String[] args) {
        DataGenerator generator = new DataGenerator();
        // Cria dados de exemplo
        List<RelatorioVenda> vendas = generator.gerarListaDeVendas(2000);

        // Exporta para Excel
        try {
            ExcelExporter.<RelatorioVenda>builder()
                .data(vendas)
                .outputFile("relatorio-vendas.xlsx")
                .build()
                .export();
            
            System.out.println("Arquivo gerado com sucesso: relatorio-vendas.xlsx");
        } catch (Exception e) {
            System.err.println(" Erro ao gerar arquivo: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
