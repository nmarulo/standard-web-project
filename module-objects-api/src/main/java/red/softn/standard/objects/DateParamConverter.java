package red.softn.standard.objects;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import red.softn.standard.objects.annotation.DateFormatterParamConverter;

import javax.ws.rs.ext.ParamConverter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class DateParamConverter implements ParamConverter<Date> {
    
    private static final String[] DATE_FORMATS = new String[] {
        "yyyy-MM-dd HH:mm:ss",
        "yyyy-MM-dd'T'HH:mm:ssXXX",
        "yyyy-MM-dd"
    };
    
    private final List<String> dateFormats;
    
    public DateParamConverter() {
        this.dateFormats = new LinkedList<>();
    }
    
    @Override
    public Date fromString(String s) {
        if (StringUtils.isNumeric(s)) {
            return new Date(Long.parseLong(s));
        }
        
        for (String pattern : this.dateFormats) {
            try {
                return new SimpleDateFormat(pattern).parse(s);
            } catch (ParseException ignored) {
            }
        }
        
        return null;
    }
    
    @Override
    public String toString(Date date) {
        return String.valueOf(date.getTime());
    }
    
    public void setDateFormatterParamConverter(DateFormatterParamConverter dateFormatterParamConverter) {
        if (dateFormatterParamConverter != null) {
            String   formatter  = dateFormatterParamConverter.formatter();
            String[] formatters = dateFormatterParamConverter.formatters();
            
            if (ArrayUtils.isNotEmpty(formatters)) {
                if (StringUtils.isNotBlank(formatter)) {
                    formatters = ArrayUtils.add(formatters, formatter);
                }
                
                this.dateFormats.addAll(Arrays.asList(formatters));
            } else if (StringUtils.isNotBlank(formatter)) {
                this.dateFormats.add(formatter);
            }
        }
        
        if (this.dateFormats.isEmpty()) {
            this.dateFormats.addAll(Arrays.asList(DATE_FORMATS));
        }
    }
}
