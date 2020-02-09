package red.softn.standard.jsf.common;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.WeakHashMap;

@FacesConverter(value = "entityConverter")
public class EntityConverter implements Converter {
    
    private static Map<Object, String> entities = new WeakHashMap<>();
    
    @Override
    public String getAsString(FacesContext context, UIComponent component, Object entity) {
        synchronized (entities) {
            if (!entities.containsKey(entity)) {
                String uuid = UUID.randomUUID()
                                  .toString();
                entities.put(entity, uuid);
                
                return uuid;
            } else {
                return entities.get(entity);
            }
        }
    }
    
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String uuid) {
        return entities.entrySet()
                       .stream()
                       .filter(value -> uuid.equals(value.getValue()))
                       .filter(value -> value.getKey() != null)
                       .map(Entry::getKey)
                       .findFirst()
                       .orElseGet(Object::new);
    }
    
}
