package ch.epfl.imhof.osm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Classe représentant une carte OpenStreetMap
 * 
 * @author Andrey Batasov (250149)
 * @author Raja Soufi (247680)
 *
 */
public final class OSMMap {
    private final List<OSMWay> WAYS;
    private final List<OSMRelation> RELATIONS;
    
    /**
     * Construit une carte OSM
     * 
     * @author Andrey Batasov (250149)
     * @author Raja Soufi (247680)
     * 
     * @param ways
     *      Une collection de chemins
     * @param relations
     *      Une collection de relations
     */
    public OSMMap(Collection<OSMWay> ways, Collection<OSMRelation> relations) {
        WAYS = Collections.unmodifiableList(new ArrayList<OSMWay>(ways));
        RELATIONS = Collections.unmodifiableList(new ArrayList<OSMRelation>(relations));
    }
    
    /**
     * Méthode qui retourne une liste non modifiable des chemins de la carte
     * 
     * @author Andrey Batasov (250149)
     * @author Raja Soufi (247680)
     * 
     * @return WAYS
     *      Les chemins de la carte
     */
    public List<OSMWay> ways() {
        return WAYS;
    }
    
    /**
     * Méthode qui retourne une liste non modifiable des relations de la carte
     * 
     * @author Andrey Batasov (250149)
     * @author Raja Soufi (247680)
     * 
     * @return RELATIONS
     *      Les relations de la carte
     */
    public List<OSMRelation> relations() {
        return RELATIONS;
    }
    
    /**
     * Bâtisseur de la classe OSMMap. Permet de construire
     * progressivement une carte OSM
     * 
     * @author Andrey Batasov (250149)
     * @author Raja Soufi (247680)
     */
    public final static class Builder {
        private final Map<Long, OSMWay> WAYS;
        private final Map<Long, OSMRelation> RELATIONS;
        private final Map<Long, OSMNode> NODES;
        
        /**
         * Construit progressivement la carte OSM
         * 
         * @author Andrey Batasov (250149)
         * @author Raja Soufi (247680)
         */
        public Builder() {
            WAYS = new HashMap<Long, OSMWay>();
            RELATIONS = new HashMap<Long, OSMRelation>();
            NODES = new HashMap<Long, OSMNode>();
        }
        
        /**
         * Méthode qui ajoute le nœud donné au bâtisseur
         * 
         * @author Andrey Batasov (250149)
         * @author Raja Soufi (247680)
         * 
         * @param newNode
         *      Le nœud à rajouter
         */
        public void addNode(OSMNode node) {
            NODES.put(node.id(), node);
        }
        
        /**
         * Méthode qui retourne le nœud dont l'identifiant unique
         * est égal à celui donné, ou null
         * si ce nœud n'a pas été ajouté précédemment au bâtisseur
         * 
         * @param id
         *      L'identifiant dont on va trouver le nœud correspondant
         * @return NODES.get(id)
         *      Le nœud dont on veut trouver avec l'identifiant passé
         *      en paramètre, ou null si l'identifiant n'est associé à
         *      aucun nœud
         */
        public OSMNode nodeForId(long id) {
            return NODES.get(id);
        }
        
        /**
         * Méthode qui ajoute le chemin donné au bâtisseur
         * 
         * @author Andrey Batasov (250149)
         * @author Raja Soufi (247680)
         * 
         * @param way
         *      Le chemin à rajouter
         */
        public void addWay(OSMWay way) {
            WAYS.put(way.id(), way);
        }
        
        /**
         * Méthode qui retourne le chemin dont l'identifiant unique
         * est égal à celui donné, ou null
         * si ce chemin n'a pas été ajouté précédemment au bâtisseur
         * 
         * @param id
         *      L'identifiant dont on va trouver le chemin correspondant
         * @return WAYS.get(id)
         *      Le chemin dont on veut trouver avec l'identifiant passé
         *      en paramètre, ou null si l'identifiant n'est associé à
         *      aucun chemin
         */
        public OSMWay wayForId(long id) {
            return WAYS.get(id);
        }
        
        /**
         * Méthode qui ajoute la relation donnée au bâtisseur
         * 
         * @author Andrey Batasov (250149)
         * @author Raja Soufi (247680)
         * 
         * @param relation
         *      La relation à rajouter
         */
        public void addRelation(OSMRelation relation) {
            RELATIONS.put(relation.id(), relation);
        }
        
        /**
         * Méthode qui retourne la relation dont l'identifiant unique
         * est égal à celui donné, ou null
         * si cette relation n'a pas été ajoutée précédemment au bâtisseur
         * 
         * @param id
         *      L'identifiant dont on va trouver la relation correspondante
         * @return RELATIONS.get(id)
         *      La relation dont on veut trouver avec l'identifiant passé
         *      en paramètre, ou null si l'identifiant n'est associé à
         *      aucune relation
         */
        public OSMRelation relationForId(long id) {
            return RELATIONS.get(id);
        }
        
        /**
         * Méthode qui construit la carte OSM
         * 
         * @author Andrey Batasov (250149)
         * @author Raja Soufi (247680)
         * 
         * @return new OSMMap(WAYS.values(), RELATIONS.values());
         *      Une nouvelle carte OSM avec la liste des chemins et
         *      la liste des relations passées au constructeur
         */
        public OSMMap build() {
            return new OSMMap(WAYS.values(), RELATIONS.values());
        }
    }
}