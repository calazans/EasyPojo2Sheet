package br.com.easypojo2sheet.example.simples;

import br.com.easypojo2sheet.api.ExcelExporter;

import java.util.List;

/**
 * Exemplo simples: gera uma lista de produtos e exporta para Excel.
 */
public class ExemploListaProdutos {

    public static void main(String[] args) {
        DataGenerator generator = new DataGenerator();

        List<Produto> produtos = generator.gerarListaDeProdutos(200);

        try {
            ExcelExporter.<Produto>builder()
                    .data(produtos)
                    .outputFile("lista-produtos.xlsx")
                    .build()
                    .export();

            System.out.println("Arquivo gerado com sucesso: lista-produtos.xlsx");
        } catch (Exception e) {
            System.err.println("Erro ao gerar arquivo: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
