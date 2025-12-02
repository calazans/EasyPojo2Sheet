

<div align="center">
  <img src=".github/assets/logo.png" alt="EasyPojo2Sheet" width="320"/>
  <br><br>
  <p><strong>Simple and powerful Java library to export POJOs to Excel</strong></p>

[![Maven Central](https://img.shields.io/maven-central/v/io.github.calazans/EasyPojo2Sheet?color=0a7a4b&label=Maven%20Central)](https://central.sonatype.com/artifact/io.github.calazans/EasyPojo2Sheet)
[![Java](https://img.shields.io/badge/Java-17%2B-0a7a4b?logo=openjdk)](https://openjdk.org/)
[![License](https://img.shields.io/github/license/calazans/EasyPojo2Sheet?color=0a7a4b)](LICENSE)
[![Stars](https://img.shields.io/github/stars/calazans/EasyPojo2Sheet?style=social)](https://github.com/calazans/EasyPojo2Sheet/stargazers)

---

###  Language / Idioma

**[ English](#english)** | **[ Português](README.md)**

</div>

---

<a name="english"></a>

##  About

**EasyPojo2Sheet** is a lightweight and efficient Java library for converting POJOs (Plain Old Java Objects) into Excel spreadsheets (.xlsx) in a simple and elegant way, using annotations.

### ✨ Why EasyPojo2Sheet?

- 🚀 **Simple and fast** - Configure with annotations and export in seconds
- 📦 **Minimal dependencies** - Only Apache POI for Excel manipulation
- 🎨 **Style support** - Customize colors, fonts, and formats
- ❄️ **Native freeze pane** - Freeze headers automatically
- 📏 **Smart auto-size** - Automatic column width adjustment
- 🔧 **Framework-agnostic** - Works with Spring Boot, Quarkus, Micronaut, Jakarta EE, and plain Java

---

## 📦 Installation

### Maven
```xml
<dependency>
  <groupId>io.github.calazans</groupId>
  <artifactId>easypojo2sheet-core</artifactId>
  <version>1.0.0</version>
</dependency>
```

---

## 🚀 Quick Start

### 1. Annotate your class

```java
import br.com.easypojo2sheet.core.annotations.SpreadSheet;
import br.com.easypojo2sheet.core.annotations.Column;

@SpreadSheet(name = "Sales Report", autoSizeColumns = true, freezeHeader = true)
public class Sale {

    @Column(header = "ID", order = 1)
    private Long id;

    @Column(header = "Product", order = 2)
    private String product;

    @Column(header = "Quantity", order = 3)
    private Integer quantity;

    @Column(header = "Amount", order = 4, format = "$ #,##0.00")
    private Double amount;

    @Column(header = "Date", order = 5, format = "MM/dd/yyyy")
    private LocalDate date;

    // Getters and Setters
}
```

### 2. Export to Excel

```java
import br.com.easypojo2sheet.core.ExcelExporter;

import java.util.List;

public class ExportExample {

    public void exportSales() throws Exception {
        // Your data
        List<Sale> sales = List.of(
                new Sale(1L, "Laptop", 5, 3500.00, LocalDate.now()),
                new Sale(2L, "Mouse", 20, 45.90, LocalDate.now()),
                new Sale(3L, "Keyboard", 10, 150.00, LocalDate.now())
        );

        // Export in one line
        ExcelExporter.export(sales, "sales-report.xlsx");

        System.out.println("✅ Spreadsheet generated successfully!");
    }
}
```

### 3. Result

An Excel spreadsheet will be created with:
- ✅ Formatted and frozen headers
- ✅ Auto-adjusted columns
- ✅ Date and currency formats applied
- ✅ Organized data ready to use

---

### Calculated Fields and Conditional Formatting

```java
@SpreadSheet(name = "Performance Analysis")
public class Performance {
    
    @Column(header = "Salesperson", order = 1)
    private String salesperson;
    
    @Column(header = "Target", order = 2, format = "#,##0")
    private Integer target;
    
    @Column(header = "Achieved", order = 3, format = "#,##0")
    private Integer achieved;
    
    @Column(header = "Achievement %", order = 4, format = "0.00%")
    public Double getAchievementPercentage() {
        return target > 0 ? (double) achieved / target : 0.0;
    }
}
```

---

##  Full Documentation

### Available Annotations

#### `@SpreadSheet`
Defines spreadsheet configurations.

| Attribute | Type | Default | Description |
|-----------|------|---------|-------------|
| `name` | String | Class name | Sheet tab name |
| `autoSizeColumns` | boolean | `false` | Auto-adjust width |
| `freezeHeader` | boolean | `false` | Freeze header row |
| `startRow` | int | `0` | Starting row for data |

#### `@Column`
Defines each column's configurations.

| Attribute | Type | Default | Description |
|-----------|------|---------|-------------|
| `header` | String | Field name | Column title |
| `order` | int | `0` | Display order |
| `format` | String | - | Display format (date, number, etc.) |
| `width` | int | `-1` | Fixed column width |
| `ignored` | boolean | `false` | Ignore field in export |

---

##  Requirements

- **Java**: 17 or higher
- **Maven**: 3.6+ (or Gradle 7+)

---

## ️ Building from Source

```bash
# Clone the repository
git clone https://github.com/calazans/EasyPojo2Sheet.git
cd EasyPojo2Sheet

# Build and install locally
mvn clean install

# Run tests
mvn test

# Generate JavaDoc
mvn javadoc:javadoc
```

---

##  Contributing

Contributions are welcome! Feel free to:

1.  Fork the project
2.  Create a feature branch (`git checkout -b feature/new-feature`)
3. ✅ Commit your changes (`git commit -m 'Add new feature'`)
4.  Push to the branch (`git push origin feature/new-feature`)
5.  Open a Pull Request

### Guidelines

- Write tests for new features
- Keep test coverage above 80%
- Follow Java code conventions
- Document public APIs with JavaDoc

---


##  Reporting Issues

Found a bug? [Open an issue](https://github.com/calazans/EasyPojo2Sheet/issues) with:

- Clear problem description
- Steps to reproduce
- Java and library version
- Sample code (if possible)

---

##  License

This project is licensed under the [Apache License 2.0](LICENSE) - see the LICENSE file for details.

---

## ‍ Author

**Diogo Calazans**

- GitHub: [@calazans](https://github.com/calazans)
- Email: calazans.contato.entering056@passinbox.com

---

## ⭐ Support the Project

If this project was useful to you, consider giving it a ⭐ on GitHub!

---

<div align="center">
  <sub>Made with ❤️ by <a href="https://github.com/calazans">Diogo Calazans</a></sub>
</div>
```
 