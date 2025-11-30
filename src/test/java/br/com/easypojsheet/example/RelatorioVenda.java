package br.com.easypojsheet.example;


import br.com.easypojsheet.annotation.SheetColumn;
import br.com.easypojsheet.annotation.SheetIgnore;
import br.com.easypojsheet.annotation.Spreadsheet;
import br.com.easypojsheet.model.enums.HorizontalAlignment;

import java.math.BigDecimal;
import java.time.LocalDate;

@Spreadsheet(name = "Relatório de Vendas", autoSizeColumns = true, freezeHeader = true)
public class RelatorioVenda {

    @SheetColumn(name = "Código", order = 1, align = HorizontalAlignment.CENTER)
    private Long id;

    @SheetColumn(name = "Data", order = 2, dateFormat = "dd/MM/yyyy", align = HorizontalAlignment.CENTER)
    private LocalDate dataVenda;

    @SheetColumn(name = "Cliente", order = 3)
    private String nomeCliente;

    @SheetColumn(name = "Vendedor", order = 4, property = "vendedor.nome", align = HorizontalAlignment.CENTER)
    private Funcionario vendedor;

    @SheetColumn(name = "Valor", order = 5, numberFormat = "#,##0.00", align = HorizontalAlignment.RIGHT)
    private BigDecimal valorTotal;

    @SheetColumn(name = "Comissão", order = 6, numberFormat = "#,##0.00", align = HorizontalAlignment.LEFT)
    private BigDecimal comissao;

    @SheetIgnore
    private String observacoesInternas;

    // Construtores
    public RelatorioVenda() {
    }

    public RelatorioVenda(Long id, LocalDate dataVenda, String nomeCliente, 
                          Funcionario vendedor, BigDecimal valorTotal, BigDecimal comissao) {
        this.id = id;
        this.dataVenda = dataVenda;
        this.nomeCliente = nomeCliente;
        this.vendedor = vendedor;
        this.valorTotal = valorTotal;
        this.comissao = comissao;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDataVenda() {
        return dataVenda;
    }

    public void setDataVenda(LocalDate dataVenda) {
        this.dataVenda = dataVenda;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }

    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }

    public Funcionario getVendedor() {
        return vendedor;
    }

    public void setVendedor(Funcionario vendedor) {
        this.vendedor = vendedor;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public BigDecimal getComissao() {
        return comissao;
    }

    public void setComissao(BigDecimal comissao) {
        this.comissao = comissao;
    }

    public String getObservacoesInternas() {
        return observacoesInternas;
    }

    public void setObservacoesInternas(String observacoesInternas) {
        this.observacoesInternas = observacoesInternas;
    }
}
