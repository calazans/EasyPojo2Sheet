<div align="center">
  <img src=".github/assets/logo.png" alt="EasyPojo2Sheet" width="320"/>
  <br><br>
  <p><strong>Biblioteca Java simples e poderosa para exportar POJOs para Excel</strong></p>

[![Maven Central](https://img.shields.io/maven-central/v/io.github.calazans/EasyPojo2Sheet?color=0a7a4b&label=Maven%20Central)](https://central.sonatype.com/artifact/io.github.calazans/EasyPojo2Sheet)
[![codecov](https://codecov.io/github/calazans/EasyPojo2Sheet/branch/main/graph/badge.svg?token=1Y7G60N1O1)](https://codecov.io/github/calazans/EasyPojo2Sheet)
[![Java](https://img.shields.io/badge/Java-17%2B-0a7a4b?logo=openjdk)](https://openjdk.org/)
[![License](https://img.shields.io/github/license/calazans/EasyPojo2Sheet?color=0a7a4b)](LICENSE.md)
[![Stars](https://img.shields.io/github/stars/calazans/EasyPojo2Sheet?style=social)](https://github.com/calazans/EasyPojo2Sheet/stargazers)

---

### 🌐 Language / Idioma

**[🇧🇷 Português](#portuguese)** | **[🇺🇸 English](README_EN.md)**

</div>

---

<a name="portuguese"></a>

## 📖 Sobre o Projeto

**EasyPojo2Sheet** é uma biblioteca Java leve e eficiente para converter POJOs (Plain Old Java Objects) em planilhas Excel (.xlsx) de forma simples e elegante, utilizando anotações.

### ✨ Por que EasyPojo2Sheet?

- 🚀 **Simples e rápido** - Configure com anotações e exporte em segundos
- 📦 **Dependências mínimas** - Apenas Apache POI para manipulação de Excel
- 🎨 **Suporte a estilos** - Personalize cores, fontes e formatos
- ❄️ **Freeze Pane nativo** - Congele cabeçalhos automaticamente
- 📏 **Auto-size inteligente** - Ajuste automático de largura de colunas
- 🔧 **Framework-agnostic** - Funciona com Spring Boot, Quarkus, Micronaut, Jakarta EE e Java puro

---
## ⚡ Performance e Benchmarks

Benchmarks JMH comparando EasyPojo2Sheet com Apache POI, EasyExcel e FastExcel revelam vantagens significativas:

- 💾 **Consumo de memória 67% menor que Apache POI** - apenas ~1.650 MB para processar 100.000 linhas
- 📊 **Throughput consistente** em diferentes volumes de dados (10k-500k linhas)
- 🔄 **Modo streaming integrado** garante uso de memória previsível mesmo com grandes datasets
- ⚖️ **Melhor equilíbrio** entre simplicidade de código, eficiência de recursos e performance adequada
- 🎯 **Ideal para casos de uso empresariais típicos** onde manutenibilidade é prioridade
- 🐳 **Perfeito para ambientes com memória limitada** como containers e serverless

> **Nota**: Embora não seja a biblioteca mais rápida em termos absolutos, o EasyPojo2Sheet prioriza produtividade do desenvolvedor e uso eficiente de recursos sobre micro-otimizações de performance.



## 📦 Instalação

### Maven
```xml
<dependency>
    <groupId>io.github.calazans</groupId>
    <artifactId>easypojo2sheet-core</artifactId>
    <version>1.0.0</version>
</dependency>
```

---

## 🚀 Guia Rápido

### 1. Anote sua classe

```java 
import br.com.easypojo2sheet.core.annotations.SpreadSheet;
import br.com.easypojo2sheet.core.annotations.Column;

@SpreadSheet(name = "Relatório de Vendas", autoSizeColumns = true, freezeHeader = true)
public class Venda {
    @Column(header = "ID", order = 1)
    private Long id;

    @Column(header = "Produto", order = 2)
    private String produto;

    @Column(header = "Quantidade", order = 3)
    private Integer quantidade;

    @Column(header = "Valor", order = 4, format = "R$ #,##0.00")
    private Double valor;

    @Column(header = "Data", order = 5, format = "dd/MM/yyyy")
    private LocalDate data;

    // Getters e Setters
}
```

### 2. Exporte para Excel

```java 
import br.com.easypojo2sheet.core.ExcelExporter;

import java.util.List;

public class ExemploExportacao {
    public void exportarVendas() throws Exception {
        // Seus dados
        List<Venda> vendas = List.of(
                new Venda(1L, "Notebook", 5, 3500.00, LocalDate.now()),
                new Venda(2L, "Mouse", 20, 45.90, LocalDate.now()),
                new Venda(3L, "Teclado", 10, 150.00, LocalDate.now())
        );

        // Exporte em uma linha
        ExcelExporter.export(vendas, "relatorio-vendas.xlsx");

        System.out.println("✅ Planilha gerada com sucesso!");
    }

}

```

### 3. Resultado

Uma planilha Excel será criada com:
- ✅ Cabeçalhos formatados e congelados
- ✅ Colunas ajustadas automaticamente
- ✅ Formatos de data e moeda aplicados
- ✅ Dados organizados e prontos para uso

---

### ### Campos Calculados e Formatação Condicional

```java
@SpreadSheet(name = "Análise de Performance")
public class Desempenho {

    @Column(header = "Vendedor", order = 1)
    private String vendedor;

    @Column(header = "Meta", order = 2, format = "#,##0")
    private Integer meta;

    @Column(header = "Realizado", order = 3, format = "#,##0")
    private Integer realizado;

    @Column(header = "% Atingimento", order = 4, format = "0.00%")
    public Double getPercentualAtingimento() {
        return meta > 0 ? (double) realizado / meta : 0.0;
    }
}

```
---

## Documentação Completa

### Anotações Disponíveis

#### `@SpreadSheet`
Define as configurações da planilha.

| Atributo | Tipo | Padrão | Descrição                      |
|-----------|------|---------|--------------------------------|
| `name` | String | Nome da classe | Nome da aba da planilha        |
| `autoSizeColumns` | boolean | `false` | Ajusta largura automaticamente |
| `freezeHeader` | boolean | `false` | Congela a linha de cabeçalho   |
| `startRow` | int | `0` | Linha inicial para dados       |

#### `@Column`
Define as configurações de cada coluna.

| Atributo | Tipo | Padrão | Descrição                                |
|-----------|------|---------|------------------------------------------|
| `header` | String | Nome do campo | Título da coluna                         |
| `order` | int | `0` | Ordem de exibição                        |
| `format` | String | - | Formato de exibição (data, número, etc.) |
| `width` | int | `-1` | Largura fixa da coluna                   |
| `ignored` | boolean | `false` | Ignora o campo na exportação             |

---

##  Requisitos

- **Java**:  17 ou superior
- **Maven**: 3.6+ 

---

## ️ ️Compilando do Código Fonte

```bash
# Clone o repositório
git clone https://github.com/calazans/EasyPojo2Sheet.git
cd EasyPojo2Sheet

# Compile e instale localmente
mvn clean install

# Execute os testes
mvn test

# Gere o JavaDoc
mvn javadoc:javadoc
```

---

##  Contribuindo

Contribuições são bem-vindas! Sinta-se à vontade para:

1. Fazer um fork do projeto
2. Criar uma branch para sua feature (git checkout -b feature/nova-funcionalidade)
3. ✅ Commit suas mudanças (git commit -m 'Adiciona nova funcionalidade')
4. Push para a branch (git push origin feature/nova-funcionalidade)
5. Abrir um Pull Request

### Diretrizes

- Escreva testes para novas funcionalidades
- Mantenha a cobertura de testes acima de 80%
- Siga as convenções de código Java
- Documente APIs públicas com JavaDoc

---


##  Reportar Problemas

Encontrou um bug? [Abra uma issue](https://github.com/calazans/EasyPojo2Sheet/issues)  com:

- Descrição clara do problema
- Passos para reproduzir
- Versão do Java e da biblioteca
- Código de exemplo (se possível)

---

##  Licença

Este projeto está licenciado sob a [Apache License 2.0](LICENSE.md) - veja o arquivo LICENSE para detalhes.

---

## ‍ Autor

**Diogo Calazans**

- GitHub: [@calazans](https://github.com/calazans)
- Email: calazans.contato.entering056@passinbox.com

---

## ⭐ Apoie o Projeto

Se este projeto foi útil para você, considere dar uma ⭐ no GitHub!

---

<div align="center">
  <sub>Feito com ❤️ por <a href="https://github.com/calazans">Diogo Calazans</a></sub>
</div>
```
