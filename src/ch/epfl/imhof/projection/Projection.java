package ch.epfl.imhof.projection;

import ch.epfl.imhof.geometry.Point;
import ch.epfl.imhof.PointGeo;

/**
 * Interface qui définit les méthodes de projection nécessaires
 * aux classes qui implémentent cette interface
 *
 * @author Raja Soufi (247680)
 * @author Andrey Batasov (250149)
 */
public interface Projection {
    
    /**
     * Méthode projettant un point de la surface de la
     * terre sur un système donné (abstraite, redéfinie
     * dans les classes qui implémentent l'interface)
     *
     * @author Raja Soufi (247680)
     * @author Andrey Batasov (250149)
     *
     * @param point
     *      un PointGeo en coordonnées sphériques
     */
    public Point project(PointGeo point);
    
    /**
     * Méthode transformant un point d'un système donné
     * en un point sur la surface de la terre en coordonnées
     * sphériques (abstraite, redéfinie dans les classes qui
     * implémentent l'interface)
     *
     * @author Raja Soufi (247680)
     * @author Andrey Batasov (250149)
     *
     * @param point
     *      un Point du système donné
     */
    public PointGeo inverse(Point point);
}
