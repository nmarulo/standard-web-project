package red.softn.standard.common.files;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public abstract class ABaseXLS {
    
    private List<Builder> rowBuilderList;
    
    protected ABaseXLS() {
        this.rowBuilderList = new LinkedList<>();
    }
    
    protected static Builder init(Builder builder) {
        return builder;
    }
    
    public File generate(String fileName) throws IOException {
        File             file             = new File(String.format("%1$s.xls", fileName));
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        Workbook         workbook         = new HSSFWorkbook();
        Sheet            sheet            = workbook.createSheet("Hoja 1");
        int              rowNum           = 0;
        int              columnIndex      = 0;
        Row              row              = null;
        List<String>     rowDataList;
        
        for (Builder builder : this.rowBuilderList) {
            row = sheet.createRow(rowNum++);
            rowDataList = builder.getRowDataList();
            
            for (String rowData : rowDataList) {
                row.createCell(columnIndex++)
                   .setCellValue(rowData);
            }
            
            columnIndex = 0;
        }
        
        if (row != null) {
            Iterator<Cell> cellIterator = row.cellIterator();
            
            while (cellIterator.hasNext()) {
                sheet.autoSizeColumn(cellIterator.next()
                                                 .getColumnIndex());
            }
        }
        
        workbook.write(fileOutputStream);
        fileOutputStream.close();
        
        return file;
    }
    
    public abstract Builder newRow();
    
    private void addRow(Builder builder) {
        this.rowBuilderList.add(builder);
    }
    
    public static class Builder {
        
        private List<String> rowDataList;
        
        private ABaseXLS aBaseXLS;
        
        protected Builder(ABaseXLS aBaseXLS) {
            this.aBaseXLS = aBaseXLS;
            this.rowDataList = new LinkedList<>();
        }
        
        public <R> R build() {
            this.aBaseXLS.addRow(this);
            
            return (R) this.aBaseXLS;
        }
        
        private List<String> getRowDataList() {
            return rowDataList;
        }
        
        protected void addCell(Date value) {
            if (value == null) {
                addCell("N/A");
            } else {
                addCell(DateFormatUtils.format(value, "dd/MM/yyyy HH:mm:ss"));
            }
        }
        
        protected void addCell(String value) {
            if (StringUtils.isBlank(value)) {
                value = "";
            }
            
            this.rowDataList.add(StringUtils.trim(value));
        }
        
    }
}
