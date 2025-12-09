package br.com.easypojo2sheet.example;

import net.datafaker.Faker;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DataGenerator {

    private static final Faker FAKER = new Faker(new Locale("pt-BR"));

    public RelatorioVenda gerarRelatorioVenda() {
        Long id = FAKER.number().numberBetween(1L, 1_000_000L);
        LocalDate data = LocalDate.now().minusDays(FAKER.number().numberBetween(0, 30));
        String cliente = FAKER.name().fullName();
        Funcionario vendedor = new Funcionario(
                FAKER.name().fullName(),
                FAKER.cpf().valid()
        );
        BigDecimal valor = BigDecimal.valueOf(FAKER.number().randomDouble(2, 100, 5000))
                .setScale(2, RoundingMode.HALF_UP);
        BigDecimal comissao = valor.multiply(BigDecimal.valueOf(0.10)).setScale(2, RoundingMode.HALF_UP);
        String obs = FAKER.lorem().sentence();

        return new RelatorioVenda(
                id,
                data,
                cliente,
                vendedor,
                valor,
                comissao,
                obs
        );
    }

    public List<RelatorioVenda> gerarListaDeVendas(int qtd) {
        List<RelatorioVenda> lst = new ArrayList<>(qtd);
        for (int i = 0; i < qtd; i++) {
            lst.add(gerarRelatorioVenda());
        }

        return lst;
    }
}
