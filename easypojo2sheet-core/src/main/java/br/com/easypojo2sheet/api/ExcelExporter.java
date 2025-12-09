package br.com.easypojo2sheet.api;

import br.com.easypojo2sheet.core.metadata.SheetMetadata;
import br.com.easypojo2sheet.core.processor.MetadataExtractor;
import br.com.easypojo2sheet.core.writer.Writer;
import br.com.easypojo2sheet.core.writer.excel.ExcelStreamingWriter;
import br.com.easypojo2sheet.core.writer.excel.ExcelWriter;
import br.com.easypojo2sheet.exception.ExcelExportException;

import java.io.IOException;
import java.util.List;

/**
 * Classe principal para exportar POJOs para Excel.
 */
public class ExcelExporter<T> {
    
    private final ExcelExporterBuilder<T> config;

    ExcelExporter(ExcelExporterBuilder<T> config) {
        this.config = config;
    }

    /**
     * Cria um novo builder.
     */
    public static <T> ExcelExporterBuilder<T> builder() {
        return new ExcelExporterBuilder<>();
    }

    /**
     * Exporta os dados para Excel.
     */
    public void export() throws ExcelExportException {
        try {
            List<T> data = config.getData();
            
            // Valida limite de linhas (524k)
            if (data.size() > 1048576) {
                throw new ExcelExportException(
                    "Limite de 524.288 linhas excedido (" + data.size() + " linhas). Considere usar modo streaming ou paginar os dados."
                );
            }

            // Extrai metadados da classe
            Class<?> entityClass = data.get(0).getClass();
            SheetMetadata metadata = MetadataExtractor.extractMetadata(entityClass);

            // TODO: Aplicar override de sheetName se configurado
            // TODO: Aplicar locale para formatação

            // Cria writer e escreve
            Writer writer = (config.isStreamingMode()? new ExcelStreamingWriter(metadata, config.getRowAccessWindowSize())
                    :new ExcelWriter(metadata));
            writer.write(data);

            // Salva em arquivo ou stream
            if (config.getOutputFile() != null) {
                writer.saveToFile(config.getOutputFile());
            } else {
                writer.saveToStream(config.getOutputStream());
            }

        } catch (IOException e) {
            throw new ExcelExportException("Erro ao salvar arquivo Excel", e);
        }
    }
}
