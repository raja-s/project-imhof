package ch.epfl.imhof.dem;

import ch.epfl.imhof.PointGeo;
import ch.epfl.imhof.Vector3;

/**
 * Interface qui représente un modèle numérique du terrain
 *
 * @author Raja Soufi (247680)
 * @author Andrey Batasov (250149)
 */
public interface DigitalElevationModel extends AutoCloseable {
    
    /**
     * Méthode qui retourne une approximation du vecteur normal
     * à la terre en le point (en coordonnées sphériques) passé
     * en paramètre (abstraite, redéfinie dans les classes qui
     * implémentent l'interface)
     * 
     * @author Raja Soufi (247680)
     * @author Andrey Batasov (250149)
     * 
     * @param point
     *      Le point où on cherche une approximation du vecteur
     *      normal à la terre
     */
    public Vector3 normalAt(PointGeo point);
}
