package com.example.eroom.domain.chat.customannotation;

import com.example.eroom.domain.chat.validator.RelaxedFutureOrPresentValidator;
import jakarta.validation.Constraint;
import org.springframework.messaging.handler.annotation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = RelaxedFutureOrPresentValidator.class)
public @interface RelaxedFutureOrPresent {

    String message() default "시작 날짜는 어제 이후여야 합니다.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
