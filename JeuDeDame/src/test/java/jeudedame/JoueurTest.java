/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package jeudedame;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author bachirmasroua
 */


public class JoueurTest {
    @Test
    void testCreationJoueurBlanc() {
        Joueur j1 = new Joueur("Alice", false);

        assertEquals("Alice", j1.getNom());
        assertEquals(false, j1.isNoir());
    }

    @Test
    void testCreationJoueurNoir() {
        Joueur j2 = new Joueur("Bob", true);

        assertEquals("Bob", j2.getNom());
        assertEquals(true, j2.isNoir());
    }

    @Test
    void testToString() {
        Joueur j2 = new Joueur("Bob", true);

        assertEquals("Bob [Noir]", j2.toString());
    }

    @Test
    void testSerializable() {
        Joueur j1 = new Joueur("Alice", false);

        assertEquals(true, j1 instanceof java.io.Serializable);
    }
}
