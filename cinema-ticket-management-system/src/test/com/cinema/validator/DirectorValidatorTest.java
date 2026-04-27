package com.cinema.validator;

import static org.junit.Assert.*;

import org.junit.Test;

import com.cinema.entity.Director;

public class DirectorValidatorTest {

    @Test
    public void validateForCreate_ShouldNotThrowException_WhenDirectorIsValid() {
        Director director = new Director("Christopher Nolan");

        try {
            DirectorValidator.validateForCreate(director);
        } catch (IllegalArgumentException e) {
            fail("Không được ném exception khi Director hợp lệ");
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void validateForCreate_ShouldThrowException_WhenDirectorIsNull() {
        DirectorValidator.validateForCreate(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void validateForCreate_ShouldThrowException_WhenDirectorNameIsBlank() {
        new Director("   ");
    }

    @Test(expected = IllegalArgumentException.class)
    public void validateForCreate_ShouldThrowException_WhenDirectorNameIsTooLong() {
        new Director(repeat("A", 256));
    }

    @Test(expected = IllegalArgumentException.class)
    public void validateForUpdate_ShouldThrowException_WhenDirectorIdIsInvalid() {
        Director director = new Director("Christopher Nolan");

        DirectorValidator.validateForUpdate(director);
    }

    @Test
    public void validateForUpdate_ShouldNotThrowException_WhenDirectorIsValid() {
        Director director = new Director(1, "Christopher Nolan");

        try {
            DirectorValidator.validateForUpdate(director);
        } catch (IllegalArgumentException e) {
            fail("Không được ném exception khi Director hợp lệ");
        }
    }

    private static String repeat(String value, int times) {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < times; i++) {
            builder.append(value);
        }

        return builder.toString();
    }
}