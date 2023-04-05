package com.hy.general_board_project.validator.validation.setting;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = NewPasswordValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface NewPassword {
    String message() default "기존과 동일한 비밀번호 입니다.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
