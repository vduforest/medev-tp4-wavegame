/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package jeudedame;

import java.lang.reflect.Field;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author vdufo
 */
public class PlateauTest {
    
    private static final int TAILLE = 10;

    // Accès aux cases privées pour certains tests
    private Piece[][] getGrille(Plateau plateau) throws Exception {
        Field f = Plateau.class.getDeclaredField("grille");
        f.setAccessible(true);
        return (Piece[][]) f.get(plateau);
    }
    
    public PlateauTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() {
    }
    
    @AfterEach
    public void tearDown() {
    }

     @Test
    public void testConstructeurInitialisation() throws Exception {
        Plateau plateau = new Plateau();
        Piece[][] grille = getGrille(plateau);
        assertNotNull(grille);
        assertEquals(TAILLE, grille.length);
        assertEquals(TAILLE, grille[0].length);

        // Vérifie que les cases NOIR et BLANC sont bien initialisées
        int nbNoir = 0, nbBlanc = 0;
        for (int y = 0; y < TAILLE; y++) {
            for (int x = 0; x < TAILLE; x++) {
                Piece p = grille[x][y];
                if (p != null) {
                    if (p.getCouleur().equals("NOIR")) nbNoir++;
                    else if (p.getCouleur().equals("BLANC")) nbBlanc++;
                }
            }
        }
        assertTrue(nbNoir > 0, "Il doit y avoir des pièces noires");
        assertTrue(nbBlanc > 0, "Il doit y avoir des pièces blanches");
    }

    @Test
    public void testGetPieceHorsBords() {
        Plateau plateau = new Plateau();
        assertNull(plateau.getPiece(-1, 0));
        assertNull(plateau.getPiece(0, -1));
        assertNull(plateau.getPiece(TAILLE, 0));
        assertNull(plateau.getPiece(0, TAILLE));
    }

    @Test
    public void testBougerPiece() throws Exception {
        Plateau plateau = new Plateau();
        Piece[][] grille = getGrille(plateau);

        // Placer un pion blanc en (7,6)
        Piece pion = new Piece(new Point2D(7,6), "BLANC");
        grille[7][6] = pion;

        plateau.bougerPiece(7, 6, 6, 5);
        assertSame(pion, grille[6][5], "La pièce doit être déplacée");
        assertNull(grille[7][6], "Ancienne case doit être vide");
        assertEquals(6, pion.getPos().getX());
        assertEquals(5, pion.getPos().getY());
    }

    @Test
    public void testBougerPieceVide() throws Exception {
        Plateau plateau = new Plateau();
        Piece[][] grille = getGrille(plateau);

        // Déplacer une case vide → rien ne se passe
        plateau.bougerPiece(0, 0, 1, 1);
        assertNull(grille[1][1]);
        assertNull(grille[0][0]);
    }

    @Test
    public void testAfficherEtSymboleCase() throws Exception {
        Plateau plateau = new Plateau();
        Piece[][] grille = getGrille(plateau);

        // Placer une dame blanche et une noire
        Piece dameBlanc = new Piece(new Point2D(0,0), "BLANC");
        dameBlanc.passerDame();
        grille[0][0] = dameBlanc;

        Piece dameNoir = new Piece(new Point2D(1,1), "NOIR");
        dameNoir.passerDame();
        grille[1][1] = dameNoir;

        // Utilisation indirecte via afficher → pas de crash et bonnes positions
        plateau.afficher();
        assertEquals(dameBlanc, grille[0][0]);
        assertEquals(dameNoir, grille[1][1]);
    }

    @Test
    public void testGetSymboleCase() throws Exception {
        Plateau plateau = new Plateau();
        Piece[][] grille = getGrille(plateau);

        // Case vide pair : doit renvoyer ". "
        assertEquals(". ", invokeGetSymboleCase(plateau, 0,0));

        // Case vide impair : "_ "
        assertEquals("_ ", invokeGetSymboleCase(plateau, 1,0));

        // Pion blanc : "O "
        Piece pBlanc = new Piece(new Point2D(2,2), "BLANC");
        grille[2][2] = pBlanc;
        assertEquals("O ", invokeGetSymboleCase(plateau, 2,2));

        // Pion noir : "X "
        Piece pNoir = new Piece(new Point2D(3,3), "NOIR");
        grille[3][3] = pNoir;
        assertEquals("X ", invokeGetSymboleCase(plateau, 3,3));

        // Dame blanche : "# "
        pBlanc.passerDame();
        assertEquals("# ", invokeGetSymboleCase(plateau, 2,2));

        // Dame noire : "@ "
        pNoir.passerDame();
        assertEquals("@ ", invokeGetSymboleCase(plateau, 3,3));
    }

    // Méthode utilitaire pour accéder à la méthode privée getSymboleCase
    private String invokeGetSymboleCase(Plateau plateau, int x, int y) throws Exception {
        java.lang.reflect.Method m = Plateau.class.getDeclaredMethod("getSymboleCase", int.class, int.class);
        m.setAccessible(true);
        return (String) m.invoke(plateau, x, y);
    }
    
}
