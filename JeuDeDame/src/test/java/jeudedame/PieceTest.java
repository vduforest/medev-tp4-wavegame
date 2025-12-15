package jeudedame;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;

public class PieceTest {

    private Piece[][] getGrille(Plateau plateau) throws Exception {
        Field f = Plateau.class.getDeclaredField("grille");
        f.setAccessible(true);
        return (Piece[][]) f.get(plateau);
    }

    private void viderPlateau(Plateau plateau) throws Exception {
        Piece[][] grille = getGrille(plateau);
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                grille[x][y] = null;
            }
        }
    }

    @Test
    public void testSetCouleur() {
        Piece piece = new Piece(new Point2D(2, 3), "BLANC");
        piece.setCouleur("NOIR");
        assertEquals("NOIR", piece.getCouleur());
    }

    @Test
    public void testSetIsKing() {
        Piece piece = new Piece(new Point2D(1, 1), "BLANC");
        assertFalse(piece.getIsKing());
        piece.setIsKing(true);
        assertTrue(piece.getIsKing());
    }

    @Test
    public void testPasserDame() {
        Piece piece = new Piece(new Point2D(4, 5), "NOIR");
        piece.passerDame();
        assertTrue(piece.getIsKing());
    }

    @Test
    public void testDeplacerPionCaseLibre() throws Exception {
        Plateau plateau = new Plateau();
        viderPlateau(plateau);
        Piece[][] grille = getGrille(plateau);

        Piece piece = new Piece(new Point2D(2, 3), "BLANC");
        grille[2][3] = piece;

        boolean ok = piece.deplacer(plateau, Piece.Direction.HAUT_DROIT, 5);
        assertTrue(ok);
        assertEquals(3, piece.getPos().getX());
        assertEquals(2, piece.getPos().getY());
        assertNull(grille[2][3]);
        assertSame(piece, grille[3][2]);
    }

    @Test
    public void testDeplacerCaseOccupee() throws Exception {
        Plateau plateau = new Plateau();
        viderPlateau(plateau);
        Piece[][] grille = getGrille(plateau);

        Piece piece = new Piece(new Point2D(2, 3), "BLANC");
        grille[2][3] = piece;

        Piece bloqueur = new Piece(new Point2D(3, 2), "NOIR");
        grille[3][2] = bloqueur;

        boolean ok = piece.deplacer(plateau, Piece.Direction.HAUT_DROIT, 1);
        assertFalse(ok);
        assertEquals(2, piece.getPos().getX());
        assertEquals(3, piece.getPos().getY());
        assertSame(piece, grille[2][3]);
        assertSame(bloqueur, grille[3][2]);
    }

    @Test
    public void testDeplacerHorsPlateau() throws Exception {
        Plateau plateau = new Plateau();
        viderPlateau(plateau);
        Piece[][] grille = getGrille(plateau);

        Piece piece = new Piece(new Point2D(0, 0), "BLANC");
        grille[0][0] = piece;

        boolean ok = piece.deplacer(plateau, Piece.Direction.HAUT_GAUCHE, 1);
        assertFalse(ok);
        assertEquals(0, piece.getPos().getX());
        assertEquals(0, piece.getPos().getY());
    }

    @Test
    public void testMangerPionSimple() throws Exception {
        Plateau plateau = new Plateau();
        viderPlateau(plateau);
        Piece[][] grille = getGrille(plateau);

        Piece attaquant = new Piece(new Point2D(2, 3), "BLANC");
        Piece victime = new Piece(new Point2D(3, 2), "NOIR");
        grille[2][3] = attaquant;
        grille[3][2] = victime;

        boolean ok = attaquant.manger(plateau, Piece.Direction.HAUT_DROIT, 2);
        assertTrue(ok);
        assertEquals(4, attaquant.getPos().getX());
        assertEquals(1, attaquant.getPos().getY());
        assertSame(attaquant, grille[4][1]);
    }

    @Test
    public void testMangerSansVictime() throws Exception {
        Plateau plateau = new Plateau();
        viderPlateau(plateau);
        Piece[][] grille = getGrille(plateau);

        Piece attaquant = new Piece(new Point2D(2, 3), "BLANC");
        grille[2][3] = attaquant;

        boolean ok = attaquant.manger(plateau, Piece.Direction.HAUT_DROIT, 2);
        assertFalse(ok);
        assertEquals(2, attaquant.getPos().getX());
        assertEquals(3, attaquant.getPos().getY());
    }

    @Test
    public void testMangerDameSimple() throws Exception {
        Plateau plateau = new Plateau();
        viderPlateau(plateau);
        Piece[][] grille = getGrille(plateau);

        Piece dame = new Piece(new Point2D(5, 5), "NOIR");
        dame.passerDame();
        Piece victime = new Piece(new Point2D(6, 6), "BLANC");
        grille[5][5] = dame;
        grille[6][6] = victime;

        boolean ok = dame.manger(plateau, Piece.Direction.BAS_DROIT, 2);
        assertTrue(ok);
        assertEquals(7, dame.getPos().getX());
        assertEquals(7, dame.getPos().getY());
        assertSame(dame, grille[7][7]);
    }

    @Test
    public void testMangerDamePlusieursPieces() throws Exception {
        Plateau plateau = new Plateau();
        viderPlateau(plateau);
        Piece[][] grille = getGrille(plateau);

        Piece dame = new Piece(new Point2D(5, 5), "NOIR");
        dame.passerDame();
        Piece p1 = new Piece(new Point2D(6, 6), "BLANC");
        Piece p2 = new Piece(new Point2D(7, 7), "BLANC");
        grille[5][5] = dame;
        grille[6][6] = p1;
        grille[7][7] = p2;

        boolean ok = dame.manger(plateau, Piece.Direction.BAS_DROIT, 3);
        assertFalse(ok, "Impossible de manger si plusieurs pièces sur la diagonale");
        assertEquals(5, dame.getPos().getX());
        assertEquals(5, dame.getPos().getY());
    }

    @Test
    public void testDistanceNonValide() throws Exception {
        Plateau plateau = new Plateau();
        viderPlateau(plateau);
        Piece pion = new Piece(new Point2D(2, 2), "BLANC");
        plateau.getGrille()[2][2] = pion;

        // Pion avec distance != 2 pour manger
        Exception ex = assertThrows(IllegalArgumentException.class, () ->
            pion.manger(plateau, Piece.Direction.BAS_DROIT, 1)
        );
        assertEquals("Un pion doit sauter exactement 2 cases pour manger.", ex.getMessage());
    }
}

