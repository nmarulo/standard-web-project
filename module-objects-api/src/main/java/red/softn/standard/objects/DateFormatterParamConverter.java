package red.softn.standard.objects;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER })
public @interface DateFormatterParamConverter {
    
    String formatter() default "";
    
    String[] formatters() default {};
}
