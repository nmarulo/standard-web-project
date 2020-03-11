package red.softn.standard.common;

import com.google.gson.*;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import red.softn.standard.common.gson.GsonExclusionStrategy;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class GsonUtil {
    
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
    
    public static Map<String, Object> convertObjectToMap(Object object) {
        if (object == null) {
            return null;
        }
        
        Gson gson = gsonBuilder();
        
        return gson.fromJson(gson.toJson(object), Map.class);
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
            try {
                String primitiveJson = json.getAsJsonPrimitive()
                                           .getAsString();
                
                return DateUtils.parseDate(primitiveJson, "yyyy-MM-dd HH:mm:ss");
            } catch (ParseException ex) {
                return null;
            }
        });
        
        builder.registerTypeAdapter(Date.class, (JsonSerializer<Date>) (src, typeOfSrc, context) -> new JsonPrimitive(DateFormatUtils.format(src, "yyyy-MM-dd HH:mm:ss")));
    }
}
