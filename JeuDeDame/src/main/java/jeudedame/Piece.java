package jeudedame;

/**
 * @author vdufo
 */

import java.io.Serializable;

public class Piece implements Serializable {
    private Point2D pos;
    private String couleur;
    private Boolean isKing;
    private static final int TAILLE = 10;

    public enum Direction {
        HAUT_GAUCHE, HAUT_DROIT, BAS_GAUCHE, BAS_DROIT
    }

    public Piece(Point2D pos, String couleur) {
        this.pos = pos;
        this.couleur = couleur;
        this.isKing = false;
    }

    // --- Getters / Setters ---
    public Point2D getPos() { return pos; }
    public String getCouleur() { return couleur; }
    public Boolean getIsKing() { return isKing; }
    public void setPos(Point2D pos) { this.pos = pos; }
    public void setCouleur(String couleur) { this.couleur = couleur; }
    public void setIsKing(Boolean isKing) { this.isKing = isKing; }
    public void passerDame() { this.isKing = true; }

    // -----------------------------------------------------------
    // DEPLACER
    // -----------------------------------------------------------
    public boolean deplacer(Plateau plateau, Direction direction, int distance) {
        if (plateau == null || direction == null)
            throw new IllegalArgumentException("Plateau ou direction null.");

        int[] delta = getDelta(direction, distance);
        int x2 = pos.getX() + delta[0];
        int y2 = pos.getY() + delta[1];

        if (!verifierCaseArrivee(plateau, x2, y2)) return false;

        plateau.bougerPiece(pos.getX(), pos.getY(), x2, y2);
        return true;
    }

    // -----------------------------------------------------------
    // MANGER
    // -----------------------------------------------------------
    public boolean manger(Plateau plateau, Direction direction, int distance) {
        if (plateau == null || direction == null)
            throw new IllegalArgumentException("Plateau ou direction null.");

        validerDistance(distance);
        int[] delta = getDelta(direction, distance);
        int x2 = pos.getX() + delta[0];
        int y2 = pos.getY() + delta[1];

        if (!verifierCaseArrivee(plateau, x2, y2)) return false;

        return isKing ? mangerDame(plateau, pos.getX(), pos.getY(), x2, y2)
                      : mangerPion(plateau, pos.getX(), pos.getY(), x2, y2);
    }

    // -----------------------------------------------------------
    // Méthodes utilitaires
    // -----------------------------------------------------------
    private int[] getDelta(Direction direction, int distance) {
        if (!Boolean.TRUE.equals(isKing)) distance = 1; // pion : toujours 1 case
        int dx = 0, dy = 0;
        switch (direction) {
            case HAUT_GAUCHE -> { dx = -distance; dy = -distance; }
            case HAUT_DROIT  -> { dx = distance; dy = -distance; }
            case BAS_GAUCHE  -> { dx = -distance; dy = distance; }
            case BAS_DROIT   -> { dx = distance; dy = distance; }
        }
        return new int[]{dx, dy};
    }

    private void validerDistance(int distance) {
        if (!Boolean.TRUE.equals(isKing)) {
            if (distance != 2)
                throw new IllegalArgumentException("Un pion doit sauter exactement 2 cases pour manger.");
        } else {
            if (distance < 2)
                throw new IllegalArgumentException("Une dame doit sauter au moins 2 cases pour manger.");
        }
    }

    private boolean verifierCaseArrivee(Plateau plateau, int x2, int y2) {
        if (x2 < 0 || x2 >= TAILLE || y2 < 0 || y2 >= TAILLE) return false;
        return plateau.getPiece(x2, y2) == null;
    }

    private boolean mangerPion(Plateau plateau, int x1, int y1, int x2, int y2) {
        int mx = (x1 + x2)/2;
        int my = (y1 + y2)/2;
        Piece victime = plateau.getPiece(mx, my);
        if (victime == null || victime.getCouleur().equals(this.couleur)) return false;
        plateau.bougerPiece(x1, y1, x2, y2);
        return true;
    }

    private boolean mangerDame(Plateau plateau, int x1, int y1, int x2, int y2) {
        int stepX = Integer.signum(x2 - x1);
        int stepY = Integer.signum(y2 - y1);
        int cx = x1 + stepX;
        int cy = y1 + stepY;
        Piece victime = null;
        while (cx != x2 && cy != y2) {
            Piece p = plateau.getPiece(cx, cy);
            if (p != null) {
                if (victime != null) return false; // plus d'une pièce sur le chemin
                victime = p;
            }
            cx += stepX;
            cy += stepY;
        }
        if (victime == null || victime.getCouleur().equals(this.couleur)) return false;
        plateau.bougerPiece(x1, y1, x2, y2);
        return true;
    }
}
