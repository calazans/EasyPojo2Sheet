package br.com.easypojo2sheet.benchmarks;

import br.com.easypojo2sheet.api.ExcelExporter;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.format.NumberFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.dhatim.fastexcel.Worksheet;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


/**
 * Compara EasyPojo2Sheet vs Apache POI direto.
 */
@BenchmarkMode({Mode.Throughput, Mode.AverageTime})
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
@Fork(2)
@Warmup(iterations = 2)
@Measurement(iterations = 5)
public class ComparacaoBenchmark {



    @State(Scope.Thread)
    public static class DataState {
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
    }

    /**
     * Usando EasyPojo2Sheet.
     */
    @Benchmark
    public void easyPojo2Sheet(DataState state, Blackhole bh) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        ExcelExporter.<SampleData>builder()
                .data(state.data)
                .streamingMode(true)
                .rowAccessWindowSize(50)
                .outputStream(out)
                .build()
                .export();

        bh.consume(out.toByteArray());
    }

    /**
     * Usando Apache POI direto (código manual).
     */
    @Benchmark
    public void APachePOI(DataState state, Blackhole bh) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try (Workbook wb = new XSSFWorkbook()) {
            Sheet sheet = wb.createSheet("Benchmark");

            // Header
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("ID");
            headerRow.createCell(1).setCellValue("Nome");
            headerRow.createCell(2).setCellValue("Valor");
            headerRow.createCell(3).setCellValue("Data");

            // Data
            int rowNum = 1;
            for (SampleData item : state.data) {
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

        bh.consume(out.toByteArray());
    }

    @Benchmark
    public void fastExcel(DataState state, Blackhole bh) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try (org.dhatim.fastexcel.Workbook wb = new org.dhatim.fastexcel.Workbook(out, "Benchmark", "1.0")) {
            Worksheet ws = wb.newWorksheet("Sheet1");
            ws.value(0, 0, "ID");
            ws.value(0, 1, "Nome");
            ws.value(0, 2, "Valor");
            ws.value(0, 3, "Data");

            int rowNum = 1;
            for (SampleData item : state.data) {
                ws.value(rowNum, 0, item.id());
                ws.value(rowNum, 1, item.nome());
                ws.value(rowNum, 2, item.valor());
                ws.value(rowNum, 3, item.data());
                rowNum++;
            }
            ws.finish();
        }
        bh.consume(out.toByteArray());
    }

    @Benchmark
    public void easyExcel(DataState state, Blackhole bh) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        EasyExcel.write(out, AlibabaExcelData.class)
                .sheet("Sheet1")
                .doWrite(state.alibabaData);

        bh.consume(out.toByteArray());
    }


}