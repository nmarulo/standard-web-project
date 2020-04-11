package red.softn.standard.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.*;
import red.softn.standard.common.gson.GsonExclusionStrategy;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class GsonUtil {
    
    private static final String[] DATE_FORMATS = new String[] {
        "yyyy-MM-dd HH:mm:ss",
        "yyyy-MM-dd'T'HH:mm:ssXXX",
        "yyyy-MM-dd"
    };
    
    /**
     * Convertir un objeto a otro (deben tener atributos con nombres iguales)
     *
     * @param object   Objeto a convertir.
     * @param classOfT Clase del tipo de objeto.
     * @param <T>      Tipo de objeto.
     *
     * @return un tipo de objeto T.
     */
    public static <T> T convertObjectTo(Object object, Class<T> classOfT) {
        if (object == null) {
            return null;
        }
        
        Gson gson = gsonBuilder();
        
        return gson.fromJson(gson.toJson(object), classOfT);
    }
    
    /**
     * Método que convierte una lista de objetos a una lista de tipo T.
     *
     * @param list     Lista de objetos.
     * @param classOfT Clase del tipo de objeto.
     * @param <T>      Tipo de objeto.
     * @param <O>      Tipo de la lista de objetos.
     *
     * @return una lista de tipo T.
     */
    public static <T, O> List<T> convertObjectListTo(List<O> list, Class<T[]> classOfT) {
        if (list == null) {
            return null;
        }
        
        Gson gson = gsonBuilder();
        
        return Arrays.asList(gson.fromJson(gson.toJson(list), classOfT));
    }
    
    public static Map<String, Object> convertObjectToMap(Object object) throws IOException {
        if (object == null) {
            return null;
        }
        
        Gson         gson         = gsonBuilder();
        ObjectMapper objectMapper = new ObjectMapper();
        
        return objectMapper.readValue(gson.toJson(object), Map.class);
    }
    
    public static Gson gsonBuilder() {
        GsonBuilder builder = new GsonBuilder();
        registerAdapterDateTime(builder);
        builder.setExclusionStrategies(GsonExclusionStrategy.getInstance());
        
        return builder.create();
    }
    
    private static void registerAdapterDateTime(GsonBuilder builder) {
        builder.registerTypeAdapter(Timestamp.class, (JsonDeserializer<Timestamp>) (json, typeOfT, context) -> {
            long primitive = json.getAsJsonPrimitive()
                                 .getAsLong();
            
            return new Timestamp(primitive);
        });
        
        builder.registerTypeAdapter(Date.class, (JsonDeserializer<Date>) (json, typeOfT, context) -> {
            JsonPrimitive asJsonPrimitive = json.getAsJsonPrimitive();
            
            if (asJsonPrimitive.isNumber()) {
                return new Date(asJsonPrimitive.getAsLong());
            }
            
            String asString = asJsonPrimitive.getAsString();
            
            for (String pattern : DATE_FORMATS) {
                try {
                    return new SimpleDateFormat(pattern).parse(asString);
                } catch (ParseException ignored) {
                }
            }
            
            return null;
        });
        
        builder.registerTypeAdapter(Date.class, (JsonSerializer<Date>) (src, typeOfSrc, context) -> new JsonPrimitive(src.getTime()));
    }
}
