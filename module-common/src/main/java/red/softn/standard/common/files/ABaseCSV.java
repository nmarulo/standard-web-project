package red.softn.standard.common.files;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.io.File;
import java.io.FileReader;
import java.text.ParseException;
import java.util.*;

public abstract class ABaseCSV {
    
    private static final char SPLIT_DELIMITER_SEMICOLON = ';';
    
    private static final char SPLIT_DELIMITER_VERTICAL_BAR = '|';
    
    private static final char CUALIFICADOR_VALIDO_DOUBLE_QUOTE = '"';
    
    private static final char CUALIFICADOR_VALIDO_SINGLE_QUOTES = '\'';
    
    private Map<Integer, Object> dataMap;
    
    public ABaseCSV() {
        this.dataMap = new HashMap<>();
    }
    
    public static <T> Map<Integer, T> getData(File file, ABaseCSV object) throws Exception {
        object.loadFile(file);
        
        return object.getDataMap();
    }
    
    private void loadFile(File file) throws Exception {
        int numLine = 1;//La primera linea es el encabezado.
        
        try (CSVParser csvParser = initCSVParse(file)) {
            
            for (CSVRecord csvRecord : csvParser) {
                rowIterator(csvRecord.iterator(), ++numLine);
            }
        }
    }
    
    protected abstract String getEncoding();
    
    protected abstract void rowIterator(Iterator<String> values, int numLine) throws Exception;
    
    protected abstract String getColumnHeader();
    
    protected String getString(Iterator<String> values) {
        if (values.hasNext()) {
            return values.next();
        }
        
        return null;
    }
    
    protected Integer getInteger(Iterator<String> values) {
        String value = getString(values);
        
        if (StringUtils.isBlank(value)) {
            return null;
        }
        
        return Integer.valueOf(value);
    }
    
    protected Date getDate(Iterator<String> values) throws Exception {
        String value = getString(values);
        
        if (StringUtils.isBlank(value)) {
            return null;
        }
        
        try {
            return DateUtils.parseDate(value, "dd/MM/yyyy HH:mm:ss");
        } catch (ParseException ex) {
            throw new Exception(String.format("La fecha [%s] no tiene un formato valido. Formato [dia/mes/año hora:min:seg]", value));
        }
    }
    
    protected Integer getBooleanInt(Iterator<String> values) {
        int    response = 0;
        String value    = getString(values);
        
        if (StringUtils.isNumeric(value)) {
            response = Integer.parseInt(value);
        }
        
        return response > 1 || response < 0 ? 1 : response;
    }
    
    protected void putDataMap(Integer index, Object object) {
        this.dataMap.put(index, object);
    }
    
    private <T> Map<Integer, T> getDataMap() {
        return (Map<Integer, T>) this.dataMap;
    }
    
    private void checkHeader(String line, char currentDelimiter, char currentQuote) throws Exception {
        char delimiter = getCurrentDelimiter(getColumnHeader());
        line = removeAllTextQualifier(line, currentQuote);
        
        if (CharUtils.compare(delimiter, currentDelimiter) != 0) {
            line = StringUtils.replace(line, StringUtils.join("\\", currentDelimiter), CharUtils.toString(delimiter));
        }
        
        if (!StringUtils.equalsIgnoreCase(line, getColumnHeader())) {
            throw new Exception(String.format("El encabezado del fichero no es valido. columnHeader<%s> delimiter<%s> line<%s>", getColumnHeader(), delimiter, line));
        }
    }
    
    private String removeAllTextQualifier(String line, char currentDelimiter) {
        return StringUtils.replace(line, CharUtils.toString(currentDelimiter), "");
    }
    
    private CSVParser initCSVParse(File file) throws Exception {
        List<String> lineList         = FileUtils.readLines(file, getEncoding());
        String       firstLine        = lineList.get(0);
        char         currentDelimiter = getCurrentDelimiter(firstLine);
        char         currentQuote     = getCurrentQuote(firstLine);
        
        checkLinesFileCSV(lineList, currentDelimiter, currentQuote);
        checkHeader(firstLine, currentDelimiter, currentQuote);
        
        return CSVFormat.DEFAULT.withDelimiter(currentDelimiter)
                                .withQuote(currentQuote == 0 ? null : currentQuote)
                                .withFirstRecordAsHeader()
                                .parse(new FileReader(file));
    }
    
    private void checkLinesFileCSV(List<String> lineList, char currentDelimiter, char currentQuote) throws Exception {
        if (currentQuote != 0) {
            return;
        }
        
        int numColumns = StringUtils.countMatches(getColumnHeader(), getCurrentDelimiter(getColumnHeader()));
        int currentNumColumns;
        
        for (String line : lineList) {
            currentNumColumns = StringUtils.countMatches(line, currentDelimiter);
            
            if (currentNumColumns != numColumns) {
                throw new Exception("Error al comprobar el formato del archivo. Se recomienda usar un cualificador de texto en el archivo a importar.");
            }
        }
    }
    
    private char getCurrentQuote(String encabezado) {
        char currentQuote = CharUtils.toChar(StringUtils.left(encabezado, 1));
        char quote        = 0;
        
        if (CharUtils.compare(currentQuote, CUALIFICADOR_VALIDO_SINGLE_QUOTES) == 0) {
            quote = CUALIFICADOR_VALIDO_SINGLE_QUOTES;
        } else if (CharUtils.compare(currentQuote, CUALIFICADOR_VALIDO_DOUBLE_QUOTE) == 0) {
            quote = CUALIFICADOR_VALIDO_DOUBLE_QUOTE;
        }
        
        return quote;
    }
    
    private char getCurrentDelimiter(String line) throws Exception {
        char delimiter;
        
        if (StringUtils.contains(line, SPLIT_DELIMITER_VERTICAL_BAR)) {
            delimiter = SPLIT_DELIMITER_VERTICAL_BAR;
        } else if (StringUtils.contains(line, SPLIT_DELIMITER_SEMICOLON)) {
            delimiter = SPLIT_DELIMITER_SEMICOLON;
        } else {
            throw new Exception(String.format("El delimitador no es valido. Son validos: punto y coma (;) y barra vertical (|). line<%s>", line));
        }
        
        return delimiter;
    }
    
}
