package br.com.easypojsheet.api;

import br.com.easypojsheet.annotation.SheetColumn;
import br.com.easypojsheet.annotation.Spreadsheet;
import br.com.easypojsheet.exception.ExcelExportException;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ExcelExporterTest {

    @Spreadsheet
    private static class Pojo {
        @SheetColumn
        private long id;
        @SheetColumn
        private String name;
        @SheetColumn
        private LocalDate date;

        Pojo(long id, String name, LocalDate date) {
            this.id = id;
            this.name = name;
            this.date = date;
        }
    }

    @Test(expected = ExcelExportException.class)
    public void shouldThrowWhenExceedingRowLimit() throws Exception {
        // 524_288 + 1 elementos
        List<Pojo> data = new ArrayList<>(524_289);
        for (int i = 0; i < 524_289; i++) {
            data.add(new Pojo(i, "n" + i, LocalDate.now()));
        }

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ExcelExporter.<Pojo>builder()
                .data(data)
                .outputStream(bos)
                .build()
                .export();
    }

    @Test
    public void happyPath_nonStreaming_and_streaming() throws Exception {
        List<Pojo> data = List.of(
                new Pojo(1, "Alice", LocalDate.of(2024, 1, 1)),
                new Pojo(2, "Bob", LocalDate.of(2024, 2, 2))
        );

        // não streaming
        ByteArrayOutputStream bos1 = new ByteArrayOutputStream();
        ExcelExporter.<Pojo>builder()
                .data(data)
                .outputStream(bos1)
                .build()
                .export();
        Assert.assertTrue(bos1.size() > 0);

        // streaming
        ByteArrayOutputStream bos2 = new ByteArrayOutputStream();
        ExcelExporter.<Pojo>builder()
                .data(data)
                .outputStream(bos2)
                .streamingMode(true)
                .rowAccessWindowSize(10)
                .build()
                .export();
        Assert.assertTrue(bos2.size() > 0);
    }
}
