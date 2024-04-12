package ch.epfl.imhof.projection;

import ch.epfl.imhof.geometry.Point;
import ch.epfl.imhof.PointGeo;

/**
 * Classe représentant la projection sur le système équirectangulaire
 *
 * @author Raja Soufi (247680)
 * @author Andrey Batasov (250149)
 */
public final class EquirectangularProjection implements Projection {
    
    /**
     * Méthode projettant un point de la surface de la
     * terre sur le système équirectangulaire
     *
     * @author Raja Soufi (247680)
     * @author Andrey Batasov (250149)
     *
     * @param point
     *      un PointGeo en coordonnées sphériques
     *
     * @return new Point(point.longitude(), point.latitude())
     *       retourne un Point en coordonnées cartésiennes
     *       dans le système équirectangulaire
     */
    @Override
    public Point project(PointGeo point) {
        return new Point(point.longitude(), point.latitude());
    }
    
    /**
     * Méthode transformant un point du système équirectangulaire
     * en un point sur la surface de la terre en coordonnées
     * sphériques
     *
     * @author Raja Soufi (247680)
     * @author Andrey Batasov (250149)
     *
     * @param point
     *      un Point du système équirectangulaire
     *
     * @return new PointGeo(point.x(), point.y())
     *       retourne un PointGeo en coordonnées sphériques
     *       sur la surface de la terre
     */
    @Override
    public PointGeo inverse(Point point) {
        return new PointGeo(point.x(), point.y());
    }
}
