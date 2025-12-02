package br.com.easypojo2sheet.example.bechmark;

import br.com.easypojo2sheet.api.ExcelExporter;
import br.com.easypojo2sheet.example.Funcionario;
import br.com.easypojo2sheet.example.RelatorioVenda;
import org.openjdk.jmh.annotations.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
public class BenchMarkRunner {


    @State(Scope.Thread)
    public static class MyState {
        public List<RelatorioVenda> vendas;

        @Setup(Level.Trial)
        public void init() {
            System.setProperty("log4j2.disable.jmx", "true");
            System.setProperty("org.apache.logging.log4j.simplelog.StatusLogger.level", "OFF");
            // Cria dados de exemplo
            vendas = List.of(
                    new RelatorioVenda(
                            1L,
                            LocalDate.of(2025, 1, 15),
                            "João Silva",
                            new Funcionario("Maria Santos", "123.456.789-00"),
                            new BigDecimal("1500.00"),
                            new BigDecimal("150.00")
                    ), new RelatorioVenda(
                            2L,
                            LocalDate.of(2025, 1, 16),
                            "Ana Costa",
                            new Funcionario("Pedro Oliveira", "987.654.321-00"),
                            new BigDecimal("2300.50"),
                            new BigDecimal("230.05")
                    ), new RelatorioVenda(
                            3L,
                            LocalDate.of(2025, 1, 17),
                            "Carlos Souza",
                            new Funcionario("Maria Santos", "123.456.789-00"),
                            new BigDecimal("980.00"),
                            new BigDecimal("98.00")
                    )
            );
        }
    }

    public static void main(String[] args) throws IOException {
        // Suppress Log4j2 warnings for benchmarking
        System.setProperty("log4j2.loggerContextFactory", "org.apache.logging.log4j.simple.SimpleLoggerContextFactory");
        org.openjdk.jmh.Main.main(args);
    }

    @Fork(value = 1, warmups = 1)
    @Benchmark
    @BenchmarkMode(Mode.All)
    @Measurement(iterations = 2)
    @OutputTimeUnit(TimeUnit.SECONDS)
    public void gerarRelatorioPequeno(MyState state){

        try {
            OutputStream ByteArrayOutputStream = new ByteArrayOutputStream();
            ExcelExporter.<RelatorioVenda>builder()
                    .data(state.vendas)
                    .outputStream(ByteArrayOutputStream)
                    .build()
                    .export();
        } catch (Exception e) {
            System.err.println("❌ Erro ao gerar arquivo: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Fork(value = 1, warmups = 1)
    @Benchmark
    @BenchmarkMode(Mode.All)
    @Measurement(iterations = 2)
    @OutputTimeUnit(TimeUnit.SECONDS)
    public void gerarRelatorioPequenoStreamingMode(MyState state){

        try {
            OutputStream ByteArrayOutputStream = new ByteArrayOutputStream();
            ExcelExporter.<RelatorioVenda>builder()
                    .data(state.vendas)
                    .streamingMode(true)
                    .rowAccessWindowSize(50)
                    .outputStream(ByteArrayOutputStream)
                    .build()
                    .export();
        } catch (Exception e) {
            System.err.println("❌ Erro ao gerar arquivo: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
