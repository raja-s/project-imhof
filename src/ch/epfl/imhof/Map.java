package ch.epfl.imhof;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

import ch.epfl.imhof.geometry.PolyLine;
import ch.epfl.imhof.geometry.Polygon;

/**
 * Classe représentant une carte
 * 
 * @author Andrey Batasov (250149)
 * @author Raja Soufi (247680)
 *
 */
public final class Map {
    private final List<Attributed<PolyLine>> POLYLINES;
    private final List<Attributed<Polygon>> POLYGONS;
    
    /**
     * Construit une carte
     * 
     * @author Andrey Batasov (250149)
     * @author Raja Soufi (247680)
     * 
     * @param polyLines
     *      Une liste de polylignes
     * @param polygons
     *      Une liste de relations
     */
    public Map(List<Attributed<PolyLine>> polyLines, List<Attributed<Polygon>> polygons) {
        POLYLINES = Collections.unmodifiableList(new ArrayList<Attributed<PolyLine>>(polyLines));
        POLYGONS = Collections.unmodifiableList(new ArrayList<Attributed<Polygon>>(polygons));
    }
    
    /**
     * Méthode qui retourne une liste non modifiable des polylignes de la carte
     * 
     * @author Andrey Batasov (250149)
     * @author Raja Soufi (247680)
     * 
     * @return POLYLINES
     *      Liste non modifiable des polylignes de la carte
     */
    public List<Attributed<PolyLine>> polyLines() {
        return POLYLINES;
    }
    
    /**
     * Méthode qui retourne une liste non modifiable des polygones de la carte
     * 
     * @author Andrey Batasov (250149)
     * @author Raja Soufi (247680)
     * 
     * @return POLYGONS
     *      Liste non modifiable des polygones de la carte
     */
    public List<Attributed<Polygon>> polygons() {
        return POLYGONS;
    }
    
    /**
     * Bâtisseur de la classe Map. Permet de construire
     * progressivement une carte
     * 
     * @author Andrey Batasov (250149)
     * @author Raja Soufi (247680)
     */
    public static class Builder {
        private final List<Attributed<PolyLine>> POLYLINES;
        private final List<Attributed<Polygon>> POLYGONS;
        
        /**
         * Construit progressivement une carte
         * 
         * @author Andrey Batasov (250149)
         * @author Raja Soufi (247680)
         */
        public Builder() {
            POLYLINES = new ArrayList<Attributed<PolyLine>>();
            POLYGONS = new ArrayList<Attributed<Polygon>>();
        }
        
        /**
         * Méthode qui ajoute la polyligne donnée au bâtisseur
         * 
         * @author Andrey Batasov (250149)
         * @author Raja Soufi (247680)
         * 
         * @param polyLine
         *      La polyligne à rajouter
         */
        public void addPolyLine(Attributed<PolyLine> polyLine) {
            POLYLINES.add(polyLine);
        }
        
        /**
         * Méthode qui ajoute le polygone donné au bâtisseur
         * 
         * @author Andrey Batasov (250149)
         * @author Raja Soufi (247680)
         * 
         * @param polygon
         *      Le polygone à rajouter
         */
        public void addPolygon(Attributed<Polygon> polygon) {
            POLYGONS.add(polygon);
        }
        
        /**
         * Méthode qui construit la carte
         * 
         * @author Andrey Batasov (250149)
         * @author Raja Soufi (247680)
         * 
         * @return new Map(POLYLINES, POLYGONS)
         *      Une nouvelle carte avec la liste des polylignes et
         *      la liste des polygones passés au constructeur
         */
        public Map build() {
            return new Map(POLYLINES, POLYGONS);
        }
    }
}