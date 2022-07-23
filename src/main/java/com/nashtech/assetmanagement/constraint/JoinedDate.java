package com.nashtech.assetmanagement.constraint;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

import static java.lang.annotation.ElementType.*;

@Constraint(validatedBy = CompareDateValidator.class)
@Target({TYPE,FIELD,ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface JoinedDate {
    String message() default "error";
    Class <?> [] groups() default {};
    Class <? extends Payload> [] payload() default {};

    String before();
    String after();
}
