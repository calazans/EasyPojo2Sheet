

<div align="center">
  <img src=".github/assets/logo.png" alt="EasyPojo2Sheet" width="320"/>
  <br><br>
  <p><strong>Simple and powerful Java library to export POJOs to Excel</strong></p>

[![Maven Central](https://img.shields.io/maven-central/v/io.github.calazans/EasyPojo2Sheet?color=0a7a4b&label=Maven%20Central)](https://central.sonatype.com/artifact/io.github.calazans/EasyPojo2Sheet)
[![codecov](https://codecov.io/github/calazans/EasyPojo2Sheet/branch/main/graph/badge.svg?token=1Y7G60N1O1)](https://codecov.io/github/calazans/EasyPojo2Sheet)
[![Java](https://img.shields.io/badge/Java-17%2B-0a7a4b?logo=openjdk)](https://openjdk.org/)
[![License](https://img.shields.io/github/license/calazans/EasyPojo2Sheet?color=0a7a4b "License")](LICENSE.md)
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
- 🔗 **Nested properties** - Access deeply nested data using dot notation
- 📊 **Advanced aggregations** - Sum, avg, min, max, count/size, join and distinct join on lists
- 🎯 **Flexible list rendering strategies** - Expand lists into multiple rows or aggregate values

---
## ⚡ Performance and Benchmarks

JMH benchmarks comparing EasyPojo2Sheet with Apache POI, EasyExcel, and FastExcel reveal significant advantages:

- 💾 **67% lower memory consumption than Apache POI** - only ~1,650 MB to process 100,000 rows
- 📊 **Consistent throughput** across different data volumes (10k-500k rows)
- 🔄 **Built-in streaming mode** ensures predictable memory usage even with large datasets
- ⚖️ **Best balance** between code simplicity, resource efficiency, and adequate performance
- 🎯 **Ideal for typical enterprise use cases** where maintainability is a priority
- 🐳 **Perfect for memory-constrained environments** such as containers and serverless

> **Note**: While it is not the fastest library in absolute terms, EasyPojo2Sheet prioritizes developer productivity and efficient resource usage over performance micro-optimizations.

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
import br.com.easypojo2sheet.annotation.Spreadsheet;
import br.com.easypojo2sheet.annotation.SheetColumn;

@Spreadsheet(name = "Sales Report", autoSizeColumns = true, freezeHeader = true)
public class Sale {
    @SheetColumn(name = "ID", order = 1)
    private Long id;

    @SheetColumn(name = "Product", order = 2)
    private String product;

    @SheetColumn(name = "Quantity", order = 3)
    private Integer quantity;

    @SheetColumn(name = "Amount", order = 4, numberFormat = "$ #,##0.00")
    private BigDecimal amount;

    @SheetColumn(name = "Date", order = 5, dateFormat = "MM/dd/yyyy")
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

        // Export using the builder pattern
        ExcelExporter.<Sale>builder()
                .data(sales)
                .outputFile("sales-report.xlsx")
                .build()
                .export();

        System.out.println("Spreadsheet generated successfully!");
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

## 🎯 Advanced Features

### 🔗 Nested Properties

Access properties of complex objects using dot notation:

```java
@Spreadsheet(name = "Sales Report")
public class Sale {
    @SheetColumn(name = "Customer", order = 1)
    private String customerName;

    // Access nested property from seller object
    @SheetColumn(name = "Seller", order = 2, property = "seller.name")
    @SheetColumn(name = "Seller SSN", order = 3, property = "seller.ssn")
    private Employee seller;

    // ... other fields
}

public class Employee {
    private String name;
    private String ssn;
    // getters and setters
}
```

### 📊 List Aggregations

Perform powerful aggregations on collections without extra code:

```java
@Spreadsheet(name = "Products Report")
public class Order {
    @SheetColumn(name = "Order No.", order = 1)
    private Long number;

    // Sum of values
    @SheetColumn(name = "Total Amount", order = 2,
            property = "items.sum.amount",
            numberFormat = "$ #,##0.00")

    // Average price
    @SheetColumn(name = "Average Price", order = 3,
            property = "items.avg.amount",
            numberFormat = "$ #,##0.00")

    // Minimum price
    @SheetColumn(name = "Min Price", order = 4,
            property = "items.min.amount",
            numberFormat = "$ #,##0.00")

    // Maximum price
    @SheetColumn(name = "Max Price", order = 5,
            property = "items.max.amount",
            numberFormat = "$ #,##0.00")

    // Join names
    @SheetColumn(name = "Products", order = 6,
            property = "items.join.name",
            separator = "; ")

    // Distinct categories joined
    @SheetColumn(name = "Categories", order = 7,
            property = "items.distinct_join.category",
            separator = ", ")

    private List<OrderItem> items;
}

public class OrderItem {
    private Long number;
    private String name;
    private String category;
    private BigDecimal amount;
    // getters and setters
}
```

#### 📋 Available Aggregations

| Aggregation     | Description                 | Example                          |
|-----------------|-----------------------------|----------------------------------|
| `sum`           | Sum numeric values          | `products.sum.price`             |
| `avg`           | Average of numeric values   | `products.avg.price`             |
| `min`           | Minimum value               | `products.min.price`             |
| `max`           | Maximum value               | `products.max.price`             |
| `size`          | List size                   | `products.size`                  |
| `join`          | Join values                 | `products.join.name`             |
| `distinct_join` | Join distinct values        | `products.distinct_join.category`|

### 🎨 List Rendering Strategies

Control how lists are rendered in the spreadsheet:

```java
@Spreadsheet(name = "Detailed Sales")
public class DetailedSale {
    @SheetColumn(name = "Sale Code", order = 1)
    private Long id;

    @SheetColumn(name = "Customer", order = 2)
    private String customer;

    // EXPAND_ROWS: Creates one row per product
    @SheetColumn(name = "Product", order = 3,
            property = "name",
            listStrategy = ListRenderStrategy.EXPAND_ROWS)

    @SheetColumn(name = "Price", order = 4,
            property = "amount",
            numberFormat = "$ #,##0.00",
            listStrategy = ListRenderStrategy.EXPAND_ROWS)

    private List<Product> products;
}
```

Result with EXPAND_ROWS:

| Sale Code | Customer | Product  | Price      |
|-----------|----------|----------|------------|
| 1         | John     | Laptop   | $ 3,500.00 |
|           |          | Mouse    | $ 45.90    |
|           |          | Keyboard | $ 150.00   |
| 2         | Mary     | Monitor  | $ 800.00   |

#### 📋 Available Strategies

| Strategy                         | Description                              | Usage                                      |
|----------------------------------|------------------------------------------|--------------------------------------------|
| `AGGREGATE`                      | Uses aggregations (sum, join, etc.)      | Default when there is an aggregation       |
| `EXPAND_ROWS`                    | Creates one row per item                 | Ideal for detailed lists                   |
| `EXPAND_ROWS_WITH_MERGED_ROWS`   | Expands and merges non-list cells        | Cleaner visual layout                      |
| `IGNORE`                         | Ignores the list                         | For non-relevant lists                     |

### 🔢 Index Access and Special Tokens

Access specific elements from lists:

```java
@Spreadsheet(name = "Analysis")
public class Analysis {
    // First element
    @SheetColumn(name = "First Product", order = 1, property = "products.first.name")

    // Last element
    @SheetColumn(name = "Last Product", order = 2, property = "products.last.name")

    // List size
    @SheetColumn(name = "Total Products", order = 3, property = "products.size")

    // Specific index
    @SheetColumn(name = "Second Product", order = 4, property = "products[1].name")
    private List<Product> products;
}
```

### 🎭 Calculated Fields and Methods

```java
@Spreadsheet(name = "Performance")
public class Performance {
    @SheetColumn(name = "Salesperson", order = 1)
    private String salesperson;

    @SheetColumn(name = "Target", order = 2, numberFormat = "#,##0")
    private Integer target;

    @SheetColumn(name = "Achieved", order = 3, numberFormat = "#,##0")
    private Integer achieved;

    // Annotated method is exported as a column
    @SheetColumn(name = "% Achievement", order = 4, numberFormat = "0.00%")
    public Double getAchievementPercentage() {
        return target > 0 ? (double) achieved / target : 0.0;
    }

    @SheetColumn(name = "Status", order = 5)
    public String getStatus() {
        double p = getAchievementPercentage();
        if (p >= 1.0) return "Target Met";
        if (p >= 0.8) return "Near Target";
        return "Below Target";
    }
}
```

### ♻️ Multiple Columns from the Same Field

Use `@SheetColumns` to create multiple columns from a single field:

```java
@Spreadsheet(name = "Full Report")
public class FullReport {
    @SheetColumn(name = "Grand Total", order = 1,
            property = "items.sum.amount",
            numberFormat = "$ #,##0.00")
    @SheetColumn(name = "Average Price", order = 2,
            property = "items.avg.amount",
            numberFormat = "$ #,##0.00")
    @SheetColumn(name = "Items Qty", order = 3,
            property = "items.size")
    private List<Item> items;
}
```

### 🚫 Ignore Fields

Use `@SheetIgnore` to exclude fields from the export:

```java
@Spreadsheet(name = "Sales")
public class Sale {
    @SheetColumn(name = "ID", order = 1)
    private Long id;

    @SheetColumn(name = "Amount", order = 2, numberFormat = "$ #,##0.00")
    private BigDecimal amount;

    // Ignored field
    @SheetIgnore
    private String internalNotes;

    @SheetIgnore
    private byte[] sensitiveData;
}
```

---

##  Full Documentation

### Available Annotations

#### `@Spreadsheet`
Defines sheet-level configurations.

| Attribute | Type | Default | Description |
|-----------|------|---------|-------------|
| `name` | String | Class name | Sheet tab name |
| `autoSizeColumns` | boolean | `false` | Auto-adjust column width |
| `freezeHeader` | boolean | `false` | Freeze header row |
| `startRow` | int | `0` | Starting row for data (0-based) |

#### `@SheetColumn`
Defines column-level configurations.

| Attribute | Type | Default | Description |
|-----------|------|---------|-------------|
| `name` | String | Field name | Column title |
| `order` | int | `Integer.MAX_VALUE` | Display order |
| `property` | String | `""` | Path to nested property |
| `dateFormat` | String | `""` | Date format (SimpleDateFormat) |
| `numberFormat` | String | `""` | Number format (DecimalFormat) |
| `width` | int | `-1` | Fixed column width (-1 = auto) |
| `align` | HorizontalAlignment | `AUTO` | Horizontal alignment |
| `valign` | VerticalAlignment | `CENTER` | Vertical alignment |
| `separator` | String | `", "` | Separator used in JOIN aggregations |
| `listStrategy` | ListRenderStrategy | `AGGREGATE` | List rendering strategy |

#### `@SheetColumns`
Container for multiple `@SheetColumn` annotations on the same field.

#### `@SheetIgnore`
Marks a field to be ignored during export.

### Configuration Enums

#### `HorizontalAlignment`
- `LEFT` - Left-aligned
- `CENTER` - Center-aligned
- `RIGHT` - Right-aligned
- `AUTO` - Automatic based on data type

#### `VerticalAlignment`
- `TOP` - Top-aligned
- `CENTER` - Center-aligned
- `BOTTOM` - Bottom-aligned

#### `ListRenderStrategy`
- `AGGREGATE` - Uses aggregations (default)
- `EXPAND_ROWS` - Expands into multiple rows
- `EXPAND_ROWS_WITH_MERGED_ROWS` - Expands with merged non-list cells
- `IGNORE` - Ignores the list

### Builder API

```java
ExcelExporter.builder()
  .data(List) // Data to export (required)
  .outputFile(String) // Output file path
  .outputStream(OutputStream) // Alternative output stream
  .rowAccessWindowSize(int) // Streaming window size (default: 100)
  .build()
  .export();
```

---

## 🛡️ Error Handling

The library provides specialized exceptions:

```java
try {
    ExcelExporter.builder()
        .data(sales)
        .outputFile("report.xlsx")
        .build()
        .export();
} catch (ExcelExportException e) { // Error during export
    log.error("Failed to generate Excel: {}", e.getMessage());
} catch (PropertyExtractionException e) { // Error accessing nested properties
    log.error("Invalid property: {}", e.getMessage());
}
```

### Available Exceptions

- `ExcelExportException` - General export error
- `PropertyExtractionException` - Error extracting nested properties

---

##  Requirements

- **Java**: 17 or higher
- **Maven**: 3.6+

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

## 📊 Roadmap

- [ ] Support for multiple sheets in a single file
- [ ] Custom styles via annotations
- [ ] Excel formulas support
- [ ] Data validation in cells
- [ ] Export to CSV and other formats
- [ ] Excel import to POJOs
- [ ] Internationalization (i18n) support

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
- Use descriptive commit messages
- Run all tests before submitting a PR

---


##  Reporting Issues

Found a bug? [Open an issue](https://github.com/calazans/EasyPojo2Sheet/issues) with:

- Clear problem description
- Steps to reproduce
- Java and library version
- Sample code (if possible)
- Screenshots (if relevant)

---

##  License

This project is licensed under the [Apache License 2.0](LICENSE.md) - see the LICENSE.md file for details.

---

## ‍ Author

**Diogo Calazans**

- GitHub: [@calazans](https://github.com/calazans)
- Email: calazans.contato.entering056@passinbox.com

---

## ⭐ Support the Project

If this project was useful to you:
- ⭐ Give it a star on GitHub
- 🐛 Report bugs and suggest improvements
- 🤝 Contribute with code
- 📢 Share it with other developers

---

## 🙏 Acknowledgements

Special thanks to all the [contributors](https://github.com/calazans/EasyPojo2Sheet/graphs/contributors) who helped make this project better!

---

<div align="center">
  <sub>Made with ❤️ by <a href="https://github.com/calazans">Diogo Calazans</a></sub>
</div>
 
