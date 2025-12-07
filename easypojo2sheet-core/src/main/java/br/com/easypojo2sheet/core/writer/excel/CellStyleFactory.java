package br.com.easypojo2sheet.core.writer.excel;

import br.com.easypojo2sheet.core.metadata.ColumnMetadata;
import org.apache.poi.ss.usermodel.*;

import java.util.HashMap;
import java.util.Map;


 class CellStyleFactory {
    private final Workbook workbook;
    private final Map<String, CellStyle> styleCache;

    public CellStyleFactory(Workbook workbook) {
        this.workbook = workbook;
        this.styleCache = new HashMap<>();
    }


    public CellStyle createHeaderStyle() {
        return styleCache.computeIfAbsent("header", k -> {
            CellStyle style = workbook.createCellStyle();
            Font font = workbook.createFont();
            font.setBold(true);
            style.setFont(font);
            style.setAlignment(HorizontalAlignment.CENTER);
            style.setVerticalAlignment(VerticalAlignment.CENTER);
            return style;
        });
    }


    public CellStyle getOrCreateCellStyle(ColumnMetadata column) {
        String key = buildStyleKey("cell", column.getHorizontalAlignment(), column.getVerticalAlignment());
        return styleCache.computeIfAbsent(key, k -> {
            CellStyle style = workbook.createCellStyle();
            style.setAlignment(column.getHorizontalAlignment().toPoiAlignment());
            style.setVerticalAlignment(column.getVerticalAlignment().toPoiAlignment());
            return style;
        });
    }


    public CellStyle getOrCreateNumberStyle(ColumnMetadata column) {
        String format = column.getNumberFormat();
        if (format == null || format.isEmpty()) {
            return getOrCreateCellStyle(column);
        }

        String key = buildStyleKey("number", format, column.getHorizontalAlignment(), column.getVerticalAlignment());
        return styleCache.computeIfAbsent(key, k -> {
            CellStyle style = workbook.createCellStyle();
            style.setAlignment(column.getHorizontalAlignment().toPoiAlignment());
            style.setVerticalAlignment(column.getVerticalAlignment().toPoiAlignment());

            DataFormat dataFormat = workbook.createDataFormat();
            style.setDataFormat(dataFormat.getFormat(format));
            return style;
        });
    }


    public CellStyle getOrCreateDateStyle(ColumnMetadata column) {
        String pattern = getDatePattern(column);
        String key = buildStyleKey("date", pattern, column.getHorizontalAlignment(), column.getVerticalAlignment());
        
        return styleCache.computeIfAbsent(key, k -> {
            CellStyle style = workbook.createCellStyle();
            style.setAlignment(column.getHorizontalAlignment().toPoiAlignment());
            style.setVerticalAlignment(column.getVerticalAlignment().toPoiAlignment());

            DataFormat dataFormat = workbook.createDataFormat();
            String excelPattern = convertJavaDatePatternToExcel(pattern);
            style.setDataFormat(dataFormat.getFormat(excelPattern));
            return style;
        });
    }

    private String getDatePattern(ColumnMetadata column) {
        String format = column.getDateFormat();
        return (format != null && !format.isEmpty()) ? format : "dd/MM/yyyy";
    }

    private String convertJavaDatePatternToExcel(String javaPattern) {

        return javaPattern
                .replace("MM", "mm")
                .replace("HH", "hh");
    }

    private String buildStyleKey(String prefix, Object... parts) {
        StringBuilder key = new StringBuilder(prefix);
        for (Object part : parts) {
            key.append("_").append(part);
        }
        return key.toString();
    }

    public Map<String, CellStyle> getStyleCache() {
        return new HashMap<>(styleCache);
    }
}
