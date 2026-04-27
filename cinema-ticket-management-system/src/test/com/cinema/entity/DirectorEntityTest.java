package com.cinema.entity;

import static org.junit.Assert.*;

import org.junit.Test;

public class DirectorEntityTest {

    @Test
    public void defaultConstructor_ShouldCreateObject() {
        Director director = new Director();

        assertNotNull(director);
    }

    @Test
    public void constructor_ShouldSetDirectorNameCorrectly() {
        Director director = new Director("Christopher Nolan");

        assertEquals("Christopher Nolan", director.getDirectorName());
    }

    @Test
    public void fullConstructor_ShouldSetDataCorrectly() {
        Director director = new Director(1, "Christopher Nolan");

        assertEquals(1, director.getDirectorId());
        assertEquals("Christopher Nolan", director.getDirectorName());
    }

    @Test
    public void equals_ShouldReturnTrue_WhenSameId() {
        Director director1 = new Director(1, "Christopher Nolan");
        Director director2 = new Director(1, "Tên khác");

        assertEquals(director1, director2);
        assertEquals(director1.hashCode(), director2.hashCode());
    }

    @Test
    public void equals_ShouldReturnFalse_WhenIdInvalid() {
        Director director1 = new Director("Christopher Nolan");
        Director director2 = new Director("Christopher Nolan");

        assertNotEquals(director1, director2);
    }

    @Test
    public void toString_ShouldNotReturnNull() {
        Director director = new Director(1, "Christopher Nolan");

        assertNotNull(director.toString());
    }
}