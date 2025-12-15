package jeudedame;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;

public class Point2DTest {

    private Point2D point;

    @BeforeEach
    void setUp() {
        point = new Point2D(1, 2);
    }

    @AfterEach
    void tearDown() {
        point = null;
    }

    @Test
    void testConstructeurs() {
        Point2D p1 = new Point2D(); // constructeur par défaut
        assertEquals(0, p1.getX());
        assertEquals(0, p1.getY());

        Point2D p2 = new Point2D(5, 6);
        assertEquals(5, p2.getX());
        assertEquals(6, p2.getY());

        Point2D p3 = new Point2D(p2); // constructeur par copie
        assertEquals(5, p3.getX());
        assertEquals(6, p3.getY());
    }

    @Test
    void testGettersSetters() {
        point.setX(10);
        point.setY(20);
        assertEquals(10, point.getX());
        assertEquals(20, point.getY());
    }

    @Test
    void testSetPosition() {
        point.setPosition(7, 8);
        assertEquals(7, point.getX());
        assertEquals(8, point.getY());
    }

    @Test
    void testTranslate() {
        point.translate(3, 4);
        assertEquals(4, point.getX()); // 1+3
        assertEquals(6, point.getY()); // 2+4
    }

    @Test
    void testEquals() {
        assertFalse(point.equals(null));

        Point2D same = new Point2D(1,2);
        Point2D diff = new Point2D(2,3);
        Point2D copy = new Point2D(same);

        assertTrue(point.equals(same));
        assertFalse(point.equals(diff));
        assertTrue(same.equals(copy));
    }

    @Test
    void testDistance() {
        assertEquals(-1.0, point.distance(null), 0);

        Point2D same = new Point2D(1,2);
        assertEquals(0.0, point.distance(same), 0);

        Point2D p2 = new Point2D(4,6);
        assertEquals(5.0, point.distance(p2), 0);
    }

    @Test
    void testToString() {
        assertEquals("[1;2]", point.toString());
    }
}
