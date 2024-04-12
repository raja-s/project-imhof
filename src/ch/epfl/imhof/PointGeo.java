package ch.epfl.imhof;

/**
 * Classe représentant un point à la surface de la Terre,
 * en coordonnées sphériques
 *
 * @author Raja Soufi (247680)
 * @author Andrey Batasov (250149)
 */
public final class PointGeo {
    private final double LONGITUDE, LATITUDE;
    
    /**
     * Construit un point avec la latitude et la longitude
     * données
     *
     * @author Raja Soufi (247680)
     * @author Andrey Batasov (250149)
     *
     * @param longitude
     *      La longitude du point, en radians
     * @param latitude
     *      La latitude du point, en radians
     * 
     * @throws IllegalArgumentException
     *      Si la longitude est invalide, c-à-d hors
     *      de l'intervalle [-π; π]
     * @throws IllegalArgumentException
     *      Si la latitude est invalide, c-à-d hors
     *      de l'intervalle [-π/2; π/2]
     */
    public PointGeo(double longitude, double latitude) {
        if ((longitude < -Math.PI) || (longitude > Math.PI) || (latitude < -(Math.PI / 2)) || (latitude > (Math.PI / 2))) {
            throw new IllegalArgumentException("Longitude/latitude hors domaine acceptable");
        } else {
            LONGITUDE = longitude;
            LATITUDE = latitude;
        }
    }
    
    /**
     * Getter de la longitude du point
     *
     * @author Raja Soufi (247680)
     * @author Andrey Batasov (250149)
     *
     * @return LONGITUDE
     *       La longitude du point
     */
    public double longitude() {
        return LONGITUDE;
    }
    
    /**
     * Getter de la latitude du PointGeo
     *
     * @author Raja Soufi (247680)
     * @author Andrey Batasov (250149)
     *
     * @return LATITUDE
     *       retourne la latitude du PointGeo
     */
    public double latitude() {
        return LATITUDE;
    }
}
