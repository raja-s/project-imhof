package ch.epfl.imhof;

import java.util.Collections;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;

/**
 * Classe représentant un graphe non orienté composé
 * d'un ensemble de points et de leurs points voisins
 * 
 * @author Raja Soufi (247680)
 * @author Andrey Batasov (250149)
 */
public final class Graph<N> {
    private final Map<N, Set<N>> NEIGHBORS;
    
    /**
     * Construit un graphe non orienté avec l'ensemble
     * de points et de leurs points voisins passé en
     * paramètre
     *
     * @author Raja Soufi (247680)
     * @author Andrey Batasov (250149)
     *
     * @param neighbors
     *      l'ensemble de points et de leurs points voisins
     *      à partir desquels on veut créer un graphe non
     *      orienté
     */
    public Graph(Map<N, Set<N>> neighbors) {
        Map<N, Set<N>> map = new HashMap<N, Set<N>>();
        neighbors.forEach((n,s) -> map.put(n, Collections.unmodifiableSet(new HashSet<N>(s))));
        NEIGHBORS = Collections.unmodifiableMap(map);
    }
    
    /**
     * Getter de l'ensemble des points constituant le graphe
     *
     * @author Raja Soufi (247680)
     * @author Andrey Batasov (250149)
     *
     * @return NEIGHBORS.keySet();
     *       l'ensemble des points constituant le graphe
     */
    public Set<N> nodes() {
        return NEIGHBORS.keySet();
    }
    
    /**
     * Getter de l'ensemble des points voisins à un point
     * du graphe
     *
     * @author Raja Soufi (247680)
     * @author Andrey Batasov (250149)
     *
     * @param node
     *      le point dont on veut retourner les
     *      points voisins
     *
     * @throws IllegalArgumentException
     *       si le point passé en paramètre n'appartient
     *       pas à l'ensemble des points du graphe
     *
     * @return NEIGHBORS.keySet();
     *       l'ensemble des points voisins à un point
     *       du graphe
     */
    public Set<N> neighborsOf(N node) {
        if (!NEIGHBORS.containsKey(node)) {
            throw new IllegalArgumentException("This node is not in the graph!");
        } else {
            return NEIGHBORS.get(node);
        }
    }
    
    /**
     * Bâtisseur de la classe Graph :
     * Permet de construire progressivement un graphe
     * non orienté
     * 
     * @author Raja Soufi (247680)
     * @author Andrey Batasov (250149)
     */
    public static final class Builder<N> {
        private final Map<N, Set<N>> NEIGHBORS;
        
        /**
         * Construit progressivement un graphe
         * non orienté
         * 
         * @author Raja Soufi (247680)
         * @author Andrey Batasov (250149)
         */
        public Builder() {
            NEIGHBORS = new HashMap<N, Set<N>>();
        }
        
        /**
         * Méthode qui ajoute un point donné au graphe
         * en cours de bâtissement
         *
         * @author Raja Soufi (247680)
         * @author Andrey Batasov (250149)
         *
         * @param node
         *      le point qu'on veut ajouter au graphe
         */
        public void addNode(N node) {
            if (!NEIGHBORS.containsKey(node)) {
                NEIGHBORS.put(node, new HashSet<N>());
            }
        }
        
        /**
         * Méthode qui "ajoute un segment" donné au graphe
         * en cours de bâtissement
         *
         * @author Raja Soufi (247680)
         * @author Andrey Batasov (250149)
         *
         * @param n1
         *      le premier point constituant le segment
         *      à ajouter
         * @param n2
         *      le second point constituant le segment
         *      à ajouter
         *
         * @throws IllegalArgumentException
         *       si les deux points qui constituent le segment
         *       à ajouter n'appartiennent pas tous les deux
         *       à l'ensemble des points du graphe
         */
        public void addEdge(N node1, N node2) {
            if ((!NEIGHBORS.containsKey(node1)) || (!NEIGHBORS.containsKey(node2))) {
                throw new IllegalArgumentException("At least one of the nodes is not in the graph!");
            } else {
                NEIGHBORS.get(node1).add(node2);
                NEIGHBORS.get(node2).add(node1);
            }
        }
        
        /**
         * Méthode qui construit une version finale et
         * immuable du graphe bâti
         *
         * @author Raja Soufi (247680)
         * @author Andrey Batasov (250149)
         *
         * @return new Graph<N>(NEIGHBORS)
         *       le graphe construit
         */
        public Graph<N> build() {
            return new Graph<N>(NEIGHBORS);
        }
    }
}
