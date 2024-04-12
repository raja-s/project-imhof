package ch.epfl.imhof.geometry;

import java.util.List;

/**
 * Classe représentant une polyligne ouverte constituée
 * d'un ensemble de points
 *
 * @author Raja Soufi (247680)
 * @author Andrey Batasov (250149)
 */
public final class OpenPolyLine extends PolyLine {
    
    /**
     * Construit une polyligne ouverte à partir d'une
     * liste de points
     *
     * @author Raja Soufi (247680)
     * @author Andrey Batasov (250149)
     *
     * @param points
     *      la liste des points à partir de laquelle
     *      on voudrait créer une polyligne ouverte
     */
    public OpenPolyLine(List<Point> points) {
        super(points);
    }
    
    /**
     * Méthode qui détermine si la polyligne est fermée
     *
     * @author Raja Soufi (247680)
     * @author Andrey Batasov (250149)
     * 
     * @return false
     *      retourne false car la polyligne est ouverte
     */
    public boolean isClosed() {
        return false;
    }
}
