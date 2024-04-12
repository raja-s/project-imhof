package ch.epfl.imhof.projection;

import ch.epfl.imhof.geometry.Point;
import ch.epfl.imhof.PointGeo;
import static java.lang.Math.*;

/**
 * Classe représentant la projection sur le système CH1903
 *
 * @author Raja Soufi (247680)
 * @author Andrey Batasov (250149)
 */
public final class CH1903Projection implements Projection {
    
    /**
     * Méthode projettant un point de la surface de la
     * terre sur le système CH1903
     *
     * @author Raja Soufi (247680)
     * @author Andrey Batasov (250149)
     *
     * @param point
     *      un PointGeo en coordonnées sphériques
     *
     * @return new Point(x, y)
     *       retourne un Point en coordonnées cartésiennes
     *       dans le système CH1903
     */
    @Override
    public Point project(PointGeo point) {
        double lambda = (toDegrees(point.longitude()) * 3600 - 26782.5) / 10000;
        double phi = (toDegrees(point.latitude()) * 3600 - 169028.66) / 10000;
        double x = 600072.37 + 211455.93 * lambda - 10938.51 * lambda * phi - 0.36 * lambda * pow(phi, 2) - 44.54 * pow(lambda, 3);
        double y = 200147.07 + 308807.95 * phi + 3745.25 * pow(lambda, 2) + 76.63 * pow(phi, 2) - 194.56 * pow(lambda, 2) * phi + 119.79 * pow(phi, 3);
        return new Point(x, y);
    }
    
    /**
     * Méthode transformant un point du système CH1903
     * en un point sur la surface de la terre en coordonnées
     * sphériques
     *
     * @author Raja Soufi (247680)
     * @author Andrey Batasov (250149)
     *
     * @param point
     *      un Point du système CH1903
     *
     * @return new PointGeo(toRadians((longitude * 100) / 36), toRadians((latitude * 100) / 36))
     *       retourne un PointGeo en coordonnées sphériques
     *       sur la surface de la terre
     */
    @Override
    public PointGeo inverse(Point point) {
        double x = (point.x() - 600000) / 1000000;
        double y = (point.y() - 200000) / 1000000;
        double longitude = 2.6779094 + 4.728982 * x + 0.791484 * x * y + 0.1306 * x * pow(y, 2) - 0.0436 * pow(x, 3);
        double latitude = 16.9023892 + 3.238272 * y - 0.270978 * pow(x, 2) - 0.002528 * pow(y, 2) - 0.0447 * pow(x, 2) * y - 0.0140 * pow(y, 3);
        return new PointGeo(toRadians((longitude * 100) / 36), toRadians((latitude * 100) / 36));
    }
}
