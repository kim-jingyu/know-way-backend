package com.knowway.user.annotation;

import com.knowway.user.validator.PasswordValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is for checking the nickname validation check which is bound the NickNameValidator.class
 * @see PasswordValidator
 */

@Constraint(validatedBy = PasswordValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface Password {
    String message() default "최소8자 이상 최대 20자 이하의 길이 적어도 하나의 소문자, 대문자, 숫자를 포함해야합니다.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
