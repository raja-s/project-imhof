package ch.epfl.imhof.geometry;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Classe abstraite représentant une polyligne constituée
 * d'un ensemble de points
 *
 * @author Raja Soufi (247680)
 * @author Andrey Batasov (250149)
 */
public abstract class PolyLine {
    private final List<Point> POINTS;
    
    /**
     * Construit une polyligne à partir d'une
     * liste de points
     *
     * @author Raja Soufi (247680)
     * @author Andrey Batasov (250149)
     *
     * @param points
     *      La liste des points à partir de laquelle
     *      on voudrait créer une polyligne
     * 
     * @throws IllegalArgumentException
     *      Si la liste de points est vide
     */
    public PolyLine(List<Point> points) {
        if (points.size() == 0) {
            throw new IllegalArgumentException("La liste fournie est vide");
        } else {
            POINTS = Collections.unmodifiableList(new ArrayList<Point>(points));
        }
    }
    
    /**
     * Méthode qui détermine si la polyligne est fermée
     * (abstraite, redéfinie dans les sous-classes)
     *
     * @author Raja Soufi (247680)
     * @author Andrey Batasov (250149)
     */
    public abstract boolean isClosed();
    
    /**
     * Getter de la liste des points de la polyligne
     *
     * @author Raja Soufi (247680)
     * @author Andrey Batasov (250149)
     *
     * @return POINTS
     *            retourne la liste des points
     *            de la polyligne
     */
    public List<Point> points() {
        return POINTS;
    }
    
    /**
     * Getter du premier point de la liste
     *
     * @author Raja Soufi (247680)
     * @author Andrey Batasov (250149)
     *
     * @return POINTS.get(0)
     *       Le premier point de la liste
     */
    public Point firstPoint() {
        return POINTS.get(0);
    }
    
    /**
     * Bâtisseur de la classe PolyLine :
     * Permet de construire progressivement la liste
     * des points de la polyligne
     *
     * @author Raja Soufi (247680)
     * @author Andrey Batasov (250149)
     */
    public static final class Builder {
        private final List<Point> POINTS;
        
        /**
         * Construit progressivement la liste des points
         * de la polyligne
         *
         * @author Raja Soufi (247680)
         * @author Andrey Batasov (250149)
         */
        public Builder() {
            POINTS = new ArrayList<Point>();
        }
        
        /**
         * Méthode qui ajoute un nouveau point à la liste
         * des points
         *
         * @author Raja Soufi (247680)
         * @author Andrey Batasov (250149)
         * 
         * @param point
         *      Le point qu'on voudrait ajouter à la liste
         */
        public void addPoint(Point point) {
            POINTS.add(point);
        }
        
        /**
         * Méthode qui construit une version finale et immuable
         * d'une polyligne ouverte construite à partir de la
         * polyligne bâtie
         *
         * @author Raja Soufi (247680)
         * @author Andrey Batasov (250149)
         * 
         * @return new OpenPolyLine(points)
         *      La polyligne construite (ouverte)
         */
        public OpenPolyLine buildOpen() {
            return new OpenPolyLine(POINTS);
        }
        
        /**
         * Méthode qui construit une version finale et immuable
         * d'une polyligne fermée construite à partir de la
         * polyligne bâtie
         *
         * @author Raja Soufi (247680)
         * @author Andrey Batasov (250149)
         * 
         * @return new OpenPolyLine(points)
         *      La polyligne construite (fermée)
         */
        public ClosedPolyLine buildClosed() {
            return new ClosedPolyLine(POINTS);
        }
    }
}
