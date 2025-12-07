package br.com.easypojo2sheet.benchmarks;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.format.NumberFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;

import java.time.LocalDate;

public class AlibabaExcelData {
    @ExcelProperty(value = "ID", index = 0)
    @ColumnWidth(10)
    private Long id;

    @ExcelProperty(value = "Nome", index = 1)
    @ColumnWidth(20)
    private String nome;

    @ExcelProperty(value = "Valor", index = 2)
    @NumberFormat("#,##0.00")
    @ColumnWidth(15)
    private Double valor;

    @ExcelProperty(value = "Data", index = 3)
    @DateTimeFormat("dd/MM/yyyy")
    @ColumnWidth(12)
    private LocalDate data;

    public AlibabaExcelData() {}

    public AlibabaExcelData(Long id, String nome, Double valor, LocalDate data) {
        this.id = id;
        this.nome = nome;
        this.valor = valor;
        this.data = data;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public Double getValor() { return valor; }
    public void setValor(Double valor) { this.valor = valor; }
    public LocalDate getData() { return data; }
    public void setData(LocalDate data) { this.data = data; }
}