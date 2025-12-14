package br.com.easypojo2sheet.api;

import br.com.easypojo2sheet.annotation.SheetColumn;
import br.com.easypojo2sheet.annotation.Spreadsheet;
import br.com.easypojo2sheet.exception.ExcelExportException;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

public class ExcelExporterBuilderTest {

    @Spreadsheet
    static class P {
        @SheetColumn long id;
        @SheetColumn String n;
        @SheetColumn LocalDate d;
        P(long id, String n, LocalDate d){ this.id=id; this.n=n; this.d=d; }
    }

    @Test(expected = ExcelExportException.class)
    public void validate_mustFail_whenDataNull() throws Exception {
        ExcelExporter.<P>builder()
                .outputStream(new ByteArrayOutputStream())
                .build();
    }

    @Test(expected = ExcelExportException.class)
    public void validate_mustFail_whenDataEmpty() throws Exception {
        ExcelExporter.<P>builder()
                .data(Collections.emptyList())
                .outputStream(new ByteArrayOutputStream())
                .build();
    }

    @Test(expected = ExcelExportException.class)
    public void validate_mustFail_whenNoOutputProvided() throws Exception {
        ExcelExporter.<P>builder()
                .data(List.of(new P(1,"a", LocalDate.now())))
                .build();
    }

    @Test
    public void validate_success_withStream() throws Exception {
        ExcelExporter.<P>builder()
                .data(List.of(new P(1,"a", LocalDate.now())))
                .outputStream(new ByteArrayOutputStream())
                .build();
    }
}
