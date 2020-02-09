package red.softn.standard.rest;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class MultiDateDeserializer extends StdDeserializer<Date> {
    
    private static final long serialVersionUID = 1L;
    
    private static final String[] DATE_FORMATS = new String[] {
            "yyyy-MM-dd HH:mm:ss",
            "yyyy-MM-dd'T'HH:mm:ssXXX",
            "yyyy-MM-dd"
    };
    
    public MultiDateDeserializer() {
        this(null);
    }
    
    public MultiDateDeserializer(Class<?> vc) {
        super(vc);
    }
    
    @Override
    public Date deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        JsonNode node = jp.getCodec()
                          .readTree(jp);
        final String date = node.textValue();
        
        if (date.matches("[0-9]+")) {
            return new Date(Long.parseLong(date));
        }
        
        for (String dateFormat : DATE_FORMATS) {
            try {
                return new SimpleDateFormat(dateFormat).parse(date);
            } catch (ParseException ignored) {
            }
        }
        
        throw new JsonParseException(jp, "Unparseable date: \"" + date + "\". Supported formats: " + Arrays.toString(DATE_FORMATS));
    }
}
