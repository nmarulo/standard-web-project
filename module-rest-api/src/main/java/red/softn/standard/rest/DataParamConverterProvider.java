package red.softn.standard.rest;

import red.softn.standard.objects.DateFormatterParamConverter;
import red.softn.standard.objects.DateParamConverter;

import javax.ws.rs.ext.ParamConverter;
import javax.ws.rs.ext.ParamConverterProvider;
import javax.ws.rs.ext.Provider;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Date;

@Provider
public class DataParamConverterProvider implements ParamConverterProvider {
    
    @Override
    public <T> ParamConverter<T> getConverter(Class<T> aClass, Type type, Annotation[] annotations) {
        if (aClass.isAssignableFrom(Date.class)) {
            DateParamConverter dateParamConverter = new DateParamConverter();
            DateFormatterParamConverter annotation = Arrays.stream(annotations)
                                                           .filter(value -> value.annotationType()
                                                                                 .isAssignableFrom(DateFormatterParamConverter.class))
                                                           .map(value -> (DateFormatterParamConverter) value)
                                                           .findFirst()
                                                           .orElse(null);
            dateParamConverter.setDateFormatterParamConverter(annotation);
            
            return (ParamConverter<T>) dateParamConverter;
        }
        
        return null;
    }
}