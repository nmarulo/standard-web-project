package red.softn.standard.common.gson;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

public class GsonExclusionStrategy implements ExclusionStrategy {
    
    public static GsonExclusionStrategy getInstance() {
        return new GsonExclusionStrategy();
    }
    
    @Override
    public boolean shouldSkipField(FieldAttributes f) {
        return f.getAnnotation(GsonExclude.class) != null;
    }
    
    @Override
    public boolean shouldSkipClass(Class<?> clazz) {
        return false;
    }
}
