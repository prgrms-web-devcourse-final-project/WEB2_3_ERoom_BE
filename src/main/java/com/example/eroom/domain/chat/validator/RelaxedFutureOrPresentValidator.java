package com.example.eroom.domain.chat.validator;

import com.example.eroom.domain.chat.customannotation.RelaxedFutureOrPresent;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDateTime;

public class RelaxedFutureOrPresentValidator implements ConstraintValidator<RelaxedFutureOrPresent, LocalDateTime> {

    @Override
    public boolean isValid(LocalDateTime value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // null 검증 x
        }

        LocalDateTime yesterday = LocalDateTime.now().minusDays(1);
        return !value.isBefore(yesterday); // 어제 이후면 통과
    }
}
