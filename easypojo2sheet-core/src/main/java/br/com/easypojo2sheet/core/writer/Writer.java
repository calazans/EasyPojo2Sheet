package br.com.easypojo2sheet.core.writer;

import br.com.easypojo2sheet.exception.ExportException;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public interface Writer {
    <T> void write(List<T> data) throws ExportException;

    void saveToFile(String outputFile) throws IOException;

    void saveToStream(OutputStream outputStream) throws IOException;
}
