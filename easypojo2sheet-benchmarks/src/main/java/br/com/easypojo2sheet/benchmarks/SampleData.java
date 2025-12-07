package br.com.easypojo2sheet.benchmarks;

import br.com.easypojo2sheet.annotation.SheetColumn;
import br.com.easypojo2sheet.annotation.Spreadsheet;

import java.time.LocalDate;

@Spreadsheet(name = "Benchmark", autoSizeColumns = false, freezeHeader = true)
public record SampleData(@SheetColumn(name = "ID", order = 1)
                  Long id, @SheetColumn(name = "Nome", order = 2)
                  String nome, @SheetColumn(name = "Valor", order = 3, numberFormat = "#,##0.00")
                  Double valor, @SheetColumn(name = "Data", order = 4, dateFormat = "dd/MM/yyyy")
                  LocalDate data) {
}
