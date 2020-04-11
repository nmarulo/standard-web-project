package red.softn.standard.objects;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target({FIELD, PARAMETER })
public @interface DateFormatterParamConverter {
    
    String formatter() default "";
    
    String[] formatters() default {};
}
