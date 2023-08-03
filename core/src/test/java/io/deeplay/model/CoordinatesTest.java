package io.deeplay.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CoordinatesTest {
    Coordinates coordinates;

    @BeforeEach
    void setUp() {
        coordinates = new Coordinates(5, 10);
    }

    @Test
    void getX() {
        assertEquals(5, coordinates.getX());
    }

    @Test
    void getY() {
        assertEquals(10, coordinates.getY());
    }

    @Test
    void setX() {
        coordinates.setX(15);
        assertEquals(15, coordinates.getX());
    }

    @Test
    void setY() {
        coordinates.setY(15);
        assertEquals(15, coordinates.getY());
    }

    @Test
    void testEquals() {
        Coordinates coordinates2 = new Coordinates(5, 10);
        Coordinates coordinates3 = new Coordinates(10, 5);

        assertEquals(coordinates, coordinates2);
        assertNotEquals(coordinates, coordinates3);
    }

    @Test
    void testHashCode() {
        Coordinates coordinates2 = new Coordinates(5, 10);

        assertEquals(coordinates.hashCode(), coordinates2.hashCode());
    }

    @Test
    void testToString() {
        assertEquals("(x = 5, y = 10)", coordinates.toString());
    }
}