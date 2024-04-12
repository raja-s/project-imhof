package ch.epfl.imhof.osm;

import ch.epfl.imhof.Attributes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Classe représentant un chemin OpenStreetMap,
 * c-à-d un ensemble de noeuds OSM
 * 
 * @author Andrey Batasov (250149)
 * @author Raja Soufi (247680)
 */
public final class OSMWay extends OSMEntity {
    private final List<OSMNode> NODES;
    
    /**
     * Construit un chemin OSM seulement s'il existe au moins 2 noeuds
     * à partir des quels on veut le construire. Sinon, "IllegalArgumentException"
     * est lancé
     * 
     * @author Andrey Batasov (250149)
     * @author Raja Soufi (247680)
     * 
     * @param id
     *      L'identificateur du chemin
     * @param nodes
     *      La liste de noeuds qui composent le chemin
     * @param attributes
     *      Les attributs qui sont associés au chemin
     *      
     * @throws IllegalArgumentException
     *      Si la liste "nodes" contient moins que 2 noeuds
     */
    public OSMWay(long id, List<OSMNode> nodes, Attributes attributes) {
        super(id, attributes);
        if (nodes.size() < 2) {
            throw new IllegalArgumentException("A way must have at least 2 nodes.");
        } else {
            NODES = Collections.unmodifiableList(new ArrayList<OSMNode>(nodes));
        }
    }
    
    /**
     * Méthode qui retourne le nombre de noeuds du chemin
     * 
     * @author Andrey Batasov (250149)
     * @author Raja Soufi (247680)
     * 
     * @return NODES.size()
     *      Le nombre de noeuds qui composent le chemin
     */
    public int nodesCount() {
        return NODES.size();
    }
    
    /**
     * Méthode qui retourne une vue
     * non modifiable de la liste des noeuds du chemin
     * 
     * @author Andrey Batasov (250149)
     * @author Raja Soufi (247680)
     * 
     * @return NODES
     *      La liste des noeuds du chemin
     */
    public List<OSMNode> nodes() {
        return NODES;
    }
    
    /**
     * Méthode qui retourne une vue non modifiable de la liste des noeuds du chemin ssi
     * ce dernier est ouvert, c-à-d que le premier et le dernier noeuds
     * ne sont pas égaux. Sinon, retourne une vue non modifiable de la liste
     * sans le dernier noeud
     * 
     * @author Andrey Batasov (250149)
     * @author Raja Soufi (247680)
     * 
     * @return NODES.subList(0, nodesCount() - 1) ou nodes
     *      La liste des noeuds sans le dernier
     * @return NODES
     *      La liste des noeuds
     */
    public List<OSMNode> nonRepeatingNodes() {
        return isClosed() ? NODES.subList(0, nodesCount() - 1) : NODES;
    }
    
    /**
     * Méthode qui retourne le premier noeud du chemin
     * 
     * @author Andrey Batasov (250149)
     * @author Raja Soufi (247680)
     * 
     * @return NODES.get(0)
     *      Le premier noeud du chemin
     */
    public OSMNode firstNode() {
        return NODES.get(0);
    }
    
    /**
     * Méthode qui retourne le dernier noeud du chemin
     * 
     * @author Andrey Batasov (250149)
     * @author Raja Soufi (247680)
     * 
     * @return NODES.get(nodesCount() - 1)
     *      Le dernier noeud du chemin
     */
    public OSMNode lastNode() {
        return NODES.get(nodesCount() - 1);
    }
    
    /**
     * Méthode qui retourne la valeur vraie ssi le premier noeud
     * du chemin est égal au dernier. Sinon, retourne false
     * 
     * @author Andrey Batasov (250149)
     * @author Raja Soufi (247680)
     * 
     * @return firstNode().equals(lastNode())
     *      Valeur true si le noeud est bien fermé.
     *      Valeur false si le noeud est ouvert
     */
    public boolean isClosed() {
        return firstNode().equals(lastNode());
    }
    
    /**
     * Bâtisseur de la classe OSMWay. Construit progressivement
     * un chemin OSM
     * 
     * @author Andrey Batasov (250149)
     * @author Raja Soufi (247680)
     */
    public final static class Builder extends OSMEntity.Builder {
        private final List<OSMNode> NODES;
        
        /**
         * Construit un bâtisseur pour un chemin ayant l'identifiant donné
         * 
         * @author Andrey Batasov (250149)
         * @author Raja Soufi (247680)
         * 
         * @param id
         *      L'identificateur du chemin
         */
        public Builder(long id) {
            super(id);
            NODES = new ArrayList<OSMNode>();
        }
        
        /**
         * Méthode qui ajoute un noeud à un chemin
         * en cours de construction
         * 
         * @author Andrey Batasov (250149)
         * @author Raja Soufi (247680)
         * 
         * @param node
         *      Le noeud qu'on voudrait ajouter au chemin
         */
        public void addNode(OSMNode node) {
            NODES.add(node);
        }
        
        /**
         * Méthode qui construit une version finale et immuable du chemin
         * bati ssi ce dernier est complet. Sinon,
         * lance "IllegalStateException"
         * 
         * @author Andrey Batasov (250149)
         * @author Raja Soufi (247680)
         * 
         * @throws IllegalStateException
         *      Si le chemin est incomplet
         * 
         * @return new OSMWay(ID, NODES, ATTRIBUTES.build())
         *      Le nouveau chemin bâti avec l'identificateur, la liste de
         *      noeuds et les attributs passés au constructeur
         */
        public OSMWay build() {
            if (isIncomplete()) {
                throw new IllegalStateException("The way is incomplete.");
            } else {
                return new OSMWay(ID, NODES, ATTRIBUTES.build());
            }
        }
        
        /**
         * Méthode qui redéfinit la méthode isIncomplete
         * de la superclasse OSMEntity.Builder et qui retourne true
         * s'il n'y a pas au moins 2 noeuds dans le chemin en construction
         * 
         * @author Andrey Batasov (250149)
         * @author Raja Soufi (247680)
         * 
         * @return (super.isIncomplete() || (NODES.size() < 2))
         *      Retourne true si la taille du chemin est plus petite que 2
         *      et false si le chemin est complet, c-à-d s'il contient 2 noeuds
         *      ou plus
         */
        public boolean isIncomplete() {
            return (super.isIncomplete() || (NODES.size() < 2));
        }
    }
}
