package br.com.easypojsheet.core.writer;

import br.com.easypojsheet.exception.ExcelExportException;
import br.com.easypojsheet.exception.ExportException;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public interface Writer {
    <T> void write(List<T> data) throws ExportException;

    void saveToFile(String outputFile) throws IOException;

    void saveToStream(OutputStream outputStream) throws IOException;
}
