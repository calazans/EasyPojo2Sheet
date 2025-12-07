package br.com.easypojo2sheet.benchmarks;

import br.com.easypojo2sheet.api.ExcelExporter;
import com.alibaba.excel.EasyExcel;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.dhatim.fastexcel.Workbook;
import org.dhatim.fastexcel.Worksheet;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;


import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Mede alocação de memória e GC overhead.
 */
@BenchmarkMode(Mode.SingleShotTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Thread)
@Fork(value = 1, jvmArgs = {"-Xms2g", "-Xmx2g"})
@Measurement(iterations = 3)
public class MemoriaBenchmark {

    @Param({"100","1000", "10000", "50000", "100000", "500000"})
    int rows;

    List<SampleData> data;
    List<AlibabaExcelData> alibabaData;

    @Setup(Level.Trial)
    public void setup() {
        data = new ArrayList<>(rows);
        alibabaData = new ArrayList<>(rows);

        for (int i = 0; i < rows; i++) {
            LocalDate date = LocalDate.now().minusDays(i % 365);
            double value = Math.random() * 1000;
            String name = "Item " + i;

            data.add(new SampleData((long) i, name, value, date));
            alibabaData.add(new AlibabaExcelData((long) i, name, value, date));
        }
    }


    @Benchmark
    public byte[] easyPojo2Sheet() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ExcelExporter.<SampleData>builder()
                .data(data)
                .outputStream(out)
                .streamingMode(true)
                .rowAccessWindowSize(100)
                .build()
                .export();
        return out.toByteArray();
    }

    @Benchmark
    public byte[]  APachePOI() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try (org.apache.poi.ss.usermodel.Workbook wb = new XSSFWorkbook()) {
            Sheet sheet = wb.createSheet("Benchmark");

            // Header
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("ID");
            headerRow.createCell(1).setCellValue("Nome");
            headerRow.createCell(2).setCellValue("Valor");
            headerRow.createCell(3).setCellValue("Data");

            // Data
            int rowNum = 1;
            for (SampleData item : data) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(item.id());
                row.createCell(1).setCellValue(item.nome());
                row.createCell(2).setCellValue(item.valor());
                row.createCell(3).setCellValue(item.data());
            }

            // Auto-size
            for (int i = 0; i < 4; i++) {
                sheet.autoSizeColumn(i);
            }

            wb.write(out);
        }
        return out.toByteArray();
    }

    @Benchmark
    public byte[] easyExcel() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        EasyExcel.write(out, AlibabaExcelData.class)
                .sheet("Sheet1")
                .doWrite(alibabaData);
        return out.toByteArray();
    }


    @Benchmark
    public byte[] fastExcel() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try (Workbook wb = new Workbook(out, "MemoriaBenchmark", "1.0")) {
            Worksheet ws = wb.newWorksheet("Sheet1");

            // Header
            ws.value(0, 0, "ID");
            ws.value(0, 1, "Nome");
            ws.value(0, 2, "Valor");
            ws.value(0, 3, "Data");

            // Data
            int rowNum = 1;
            for (SampleData item : data) {
                ws.value(rowNum, 0, item.id());
                ws.value(rowNum, 1, item.nome());
                ws.value(rowNum, 2, item.valor());
                ws.value(rowNum, 3, item.data());
                rowNum++;
            }

            ws.finish();
        }

        return out.toByteArray();
    }
}