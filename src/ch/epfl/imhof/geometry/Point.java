package ch.epfl.imhof.geometry;

import java.util.function.Function;

/**
 * Classe représentant un point dans le plan, en
 * coordonnées cartésiennes
 *
 * @author Raja Soufi (247680)
 * @author Andrey Batasov (250149)
 */
public class Point {
    private final double X, Y;
    
    /**
     * Construit un point avec les coordonnées cartésiennes
     * x e y données
     *
     * @author Raja Soufi (247680)
     * @author Andrey Batasov (250149)
     *
     * @param x
     *      La composante x du point
     * @param y
     *      La composante y du point
     */
    public Point(double x, double y) {
        X = x;
        Y = y;
    }
    
    /**
     * Getter de la composante x du point
     *
     * @author Raja Soufi (247680)
     * @author Andrey Batasov (250149)
     *
     * @return X
     *       retourne le X du Point
     */
    public double x() {
        return X;
    }
    
    /**
     * Getter de la composante y du point
     *
     * @author Raja Soufi (247680)
     * @author Andrey Batasov (250149)
     *
     * @return Y
     *       retourne le Y du Point
     */
    public double y() {
        return Y;
    }
    
    /**
     * Méthode statique qui calcule et fournit un changement de repère,
     * c-à-d qui, à partir de deux paires de points (chacune composée d'un
     * point en les coordonnées du premier repère et du même point en les
     * coordonnées du second) calcule la fonction qui transforme les coordonnées
     * de n'importe quel point du premier repère en des coordonnées dans le
     * second, et retourne cette fonction
     *
     * @author Raja Soufi (247680)
     * @author Andrey Batasov (250149)
     * 
     * @param p1
     *      Le premier point de la première paire
     * @param p2
     *      Le second point de la première paire
     * @param p3
     *      Le premier point de la seconde paire
     * @param p4
     *      Le second point de la seconde paire
     * 
     * @return p -> new Point(a * p.x() + b, c * p.y() + d)
     *      la fonction de transformation
     */
    public static Function<Point, Point> alignedCoordinateChange(Point p1, Point p2, Point p3, Point p4) {
        if ((p1.x() == p3.x()) || (p1.y() == p3.y())) {
            throw new IllegalArgumentException("Une des paires de points passées en paramètre est composée de deux points au même niveau vertical ou horizontal!");
        } else {
            double a = (p4.x() - p2.x()) / (p3.x() - p1.x());
            double b = p2.x() - p1.x() * a;
            double c = (p4.y() - p2.y()) / (p3.y() - p1.y());
            double d = p2.y() - p1.y() * c;
            return p -> new Point(a * p.x() + b, c * p.y() + d);
        }
    }
}
