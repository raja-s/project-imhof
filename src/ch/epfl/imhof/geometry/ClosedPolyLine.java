package ch.epfl.imhof.geometry;

import java.util.List;

import static java.lang.Math.*;

/**
 * Classe représentant une polyligne fermée constituée
 * d'un ensemble de points
 *
 * @author Raja Soufi (247680)
 * @author Andrey Batasov (250149)
 */
public final class ClosedPolyLine extends PolyLine {
    
    /**
     * Construit une polyligne fermée à partir d'une
     * liste de points
     *
     * @author Raja Soufi (247680)
     * @author Andrey Batasov (250149)
     *
     * @param points
     *      la liste des points à partir de laquelle
     *      on voudrait créer une polyligne fermée
     */
    public ClosedPolyLine(List<Point> points) {
        super(points);
    }
    
    /**
     * Méthode qui détermine si la polyligne est fermée
     *
     * @author Raja Soufi (247680)
     * @author Andrey Batasov (250149)
     * 
     * @return true
     *      retourne true car la polyligne est bien fermée
     */
    public boolean isClosed() {
        return true;
    }
    
    /**
     * Méthode qui calcule l'aire de la polyligne fermée
     *
     * @author Raja Soufi (247680)
     * @author Andrey Batasov (250149)
     * 
     * @return somme
     *      retourne l'aire calculée
     */
    public double area() {
        double somme = 0;
        for (int i = 0 ; i < points().size() ; i++) {
            somme += vertex(i).x() * (vertex(i + 1).y() - vertex(i - 1).y());
        }
        somme = (abs(somme) / 2);
        return somme;
    }
    
    /**
     * Méthode qui détermine si un point est contenu
     * dans une polyligne fermée
     *
     * @author Raja Soufi (247680)
     * @author Andrey Batasov (250149)
     * 
     * @param p
     *     un point qu'on veut tester avec la méthode
     * 
     * @return (windingNumber != 0)
     *      retourne le résultat du test
     */
    public boolean containsPoint(Point p) {
        int windingNumber = 0;
        for (int i = 0 ; i < points().size() ; i++) {
            if (vertex(i).y() <= p.y()) {
                if ((vertex(i + 1).y() > p.y()) && (isToTheLeft(p, vertex(i), vertex(i + 1)))) {
                    windingNumber++;
                }
            } else {
                if ((vertex(i + 1).y() <= p.y()) && (isToTheLeft(p, vertex(i + 1), vertex(i)))) {
                    windingNumber--;
                }
            }
        }
        return (windingNumber != 0);
    }
    
    /**
     * Méthode qui détermine si un point est situé à
     * gauche d'une droite formée par deux points
     *
     * @author Raja Soufi (247680)
     * @author Andrey Batasov (250149)
     * 
     * @param p
     *     un point qu'on veut tester avec la méthode
     * @param end1
     *     le premier point qui définit la droite
     * @param end2
     *     le deuxième point qui définit la droite
     * 
     * @return ((end1.x() - p.x()) * (end2.y() - p.y()) > (end2.x() - p.x()) * (end1.y() - p.y()))
     *      retourne le résultat du test
     */
    private boolean isToTheLeft(Point p, Point end1, Point end2) {
        return ((end1.x() - p.x()) * (end2.y() - p.y()) > (end2.x() - p.x()) * (end1.y() - p.y()));
    }
    
    /**
     * Getter d'un certain point de la polyligne fermée
     *
     * @author Raja Soufi (247680)
     * @author Andrey Batasov (250149)
     * 
     * @param i
     *      la position du point qui nous intéresse
     * 
     * @return points().get(floorMod(i, points().size()))
     *      retourne le point qui nous intéresse
     */
    private Point vertex(int i) {
        return points().get(floorMod(i, points().size()));
    }
}
