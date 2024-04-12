package ch.epfl.imhof.osm;

import ch.epfl.imhof.PointGeo;
import ch.epfl.imhof.Attributes;

/**
 * Classe représantant un noeud OpenStreetMap
 * 
 * @author Andrey Batasov (250149)
 * @author Raja Soufi (247680)
 */
public final class OSMNode extends OSMEntity {
    private final PointGeo POSITION;
    
    /**
     * Construit un noeud
     * 
     * @author Andrey Batasov (250149)
     * @author Raja Soufi (247680)
     * 
     * @param id
     *      Le numéro identificateur du noeud
     * @param position
     *      La position du noeud
     * @param attributes
     *      Les attributs du noeud
     */
    public OSMNode(long id, PointGeo position, Attributes attributes) {
        super(id, attributes);
        POSITION = position;
    }
    
    /**
     * Méthode qui retourne la position d'un noeud OSM
     * 
     * @author Andrey Batasov (250149)
     * @author Raja Soufi (247680)
     * 
     * @return POSITION
     *      La position du noeud
     */
    public PointGeo position() {
        return POSITION;
    }
    
    /**
     * Bâtisseur de la classe OSMNode. Permet de construire
     * progressivement un noeud OSM
     * 
     * @author Andrey Batasov (250149)
     * @author Raja Soufi (247680)
     */
    public final static class Builder extends OSMEntity.Builder {
        private final PointGeo POSITION;
        
        /**
         * Construit progressivement le noeud OSM
         *
         * @author Andrey Batasov (250149)
         * @author Raja Soufi (247680)
         *
         * @param id
         *      L'identificateur du noeud
         * @param position
         *      La position du noeud
         */
        public Builder(long id, PointGeo position) {
            super(id);
            POSITION = position;
        }
        
        /**
         * Méthode qui construit le noeud OSM ssi
         * ce dernier est complet. Sinon, lance une exception
         * "IllegalStateException"
         * 
         * @author Andrey Batasov (250149)
         * @author Raja Soufi (247680)
         * 
         * @throws IllegalStateException
         *      Si le noeud est incomplet
         * 
         * @return new OSMNode(ID, POSITION, ATTRIBUTES.build())
         *      Un nouveau noeud bâti avec l'identificateur,
         *      la position et les attributs passes au constructeur
         */
        public OSMNode build() {
            if (isIncomplete()) {
                throw new IllegalStateException("The node is incomplete.");
            } else {
                return new OSMNode(ID, POSITION, ATTRIBUTES.build());
            }
        }
    }
}