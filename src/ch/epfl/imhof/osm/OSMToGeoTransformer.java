package ch.epfl.imhof.osm;

import java.util.Collections;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Set;
import java.util.HashSet;

import ch.epfl.imhof.Attributes;
import ch.epfl.imhof.Attributed;
import ch.epfl.imhof.Map;
import ch.epfl.imhof.Graph;
import ch.epfl.imhof.osm.OSMRelation.Member;
import ch.epfl.imhof.osm.OSMRelation.Member.Type;
import ch.epfl.imhof.geometry.PolyLine;
import ch.epfl.imhof.geometry.ClosedPolyLine;
import ch.epfl.imhof.geometry.Polygon;
import ch.epfl.imhof.geometry.Point;
import ch.epfl.imhof.projection.Projection;

/**
 * Classe représenant un transformateur de cartes
 * OpenStreetMap en cartes construites avec les entités
 * géométriques qu'on a définies, suivant une projection
 * particulière
 * 
 * @author Raja Soufi (247680)
 * @author Andrey Batasov (250149)
 */
public final class OSMToGeoTransformer {
    private final Projection PROJECTION;
    private static final Set<String> AREA_VALUES = new HashSet<String>(Arrays.asList("yes", "1", "true"));
    private static final Set<String> AREA_KEYS = new HashSet<String>(Arrays.asList("aeroway", "amenity", "building", "harbour", "historic",
            "landuse", "leisure", "man_made", "military", "natural",
            "office", "place", "power", "public_transport", "shop",
            "sport", "tourism", "water", "waterway", "wetland"));
    private static final Set<String> POLYLINE_KEYS = new HashSet<String>(Arrays.asList("bridge", "highway", "layer", "man_made", "railway", "tunnel", "waterway"));
    private static final Set<String> POLYGON_KEYS = new HashSet<String>(Arrays.asList("building", "landuse", "layer", "leisure", "natural", "waterway"));

    /**
     * Construit un transformateur avec la projection
     * passée en paramètre
     *
     * @author Raja Soufi (247680)
     * @author Andrey Batasov (250149)
     *
     * @param projection
     *      la projection avec laquelle on voudrait
     *      construire le transformateur
     */
    public OSMToGeoTransformer(Projection projection) {
        PROJECTION = projection;
    }

    /**
     * Méthode qui transforme une carte OpenStreetMap en
     * une carte construite avec les entités géométriques
     * qu'on a définies
     * 
     * @author Raja Soufi (247680)
     * @author Andrey Batasov (250149)
     * 
     * @param map
     *      la carte OpenStreetMap à transformer
     * 
     * @return incompleteMap.build()
     *      une version finale et immuable d'une carte à
     *      partir de la carte bâtie
     */
    public Map transform(OSMMap map) {
        Map.Builder incompleteMap = new Map.Builder();
        for (OSMWay way : map.ways()) {
            PolyLine.Builder polyLine = new PolyLine.Builder();
            for (OSMNode node : way.nonRepeatingNodes()) {
                polyLine.addPoint(nodeToPoint(node));
            }
            if (way.isClosed() && validArea(way)) {
                Attributes a = way.attributes().keepOnlyKeys(POLYGON_KEYS);
                if (!a.isEmpty()) {
                    incompleteMap.addPolygon(new Attributed<Polygon>(new Polygon(polyLine.buildClosed()), a));
                }
            } else {
                Attributes a = way.attributes().keepOnlyKeys(POLYLINE_KEYS);
                if (!a.isEmpty()) {
                    PolyLine p;
                    if (way.isClosed()) {
                        p = polyLine.buildClosed();
                    } else {
                        p = polyLine.buildOpen();
                    }
                    incompleteMap.addPolyLine(new Attributed<PolyLine>(p, a));
                }
            }
        }
        for (OSMRelation relation : map.relations()) {
            if (relation.attributes().contains("type") && relation.attributes().get("type").equals("multipolygon")) {
                Attributes a = relation.attributes().keepOnlyKeys(POLYGON_KEYS);
                if (!a.isEmpty()) {
                    List<Attributed<Polygon>> attributedPolygons = assemblePolygon(relation, a);
                    for (Attributed<Polygon> p : attributedPolygons) {
                        incompleteMap.addPolygon(p);
                    }
                }
            }
        }
        Map finalMap = incompleteMap.build();
        return finalMap;
    }

    private List<ClosedPolyLine> ringsForRole(OSMRelation relation, String role) {
        List<ClosedPolyLine> list = new ArrayList<ClosedPolyLine>();
        List<OSMWay> ways = new ArrayList<OSMWay>();
        List<OSMNode> unvisitedNodes = new ArrayList<OSMNode>();
        Graph.Builder<OSMNode> incompleteGraph = new Graph.Builder<OSMNode>();
        for (Member m : relation.members()) {
            if (Type.WAY.equals(m.type()) && m.role().equals(role)) {
                ways.add((OSMWay)m.member());
            }
        }
        for (OSMWay w : ways) {
            incompleteGraph.addNode(w.firstNode());
            List<OSMNode> nodes = w.nodes();
            for (int j = 1 ; j < w.nodesCount() ; j++) {
                incompleteGraph.addNode(nodes.get(j));
                incompleteGraph.addEdge(nodes.get(j - 1), nodes.get(j));
            }
        }
        Graph<OSMNode> graph = incompleteGraph.build();
        unvisitedNodes.addAll(graph.nodes());
        while (!unvisitedNodes.isEmpty()) {
            PolyLine.Builder polyLine = new PolyLine.Builder();
            OSMNode iterator = unvisitedNodes.get(0);
            while (unvisitedNodes.contains(iterator)) {
                if (graph.neighborsOf(iterator).size() != 2) {
                    return Collections.emptyList();
                }
                polyLine.addPoint(nodeToPoint(iterator));
                unvisitedNodes.remove(iterator);
                for (OSMNode n : graph.neighborsOf(iterator)) {
                    if (unvisitedNodes.contains(n)) {
                        iterator = n;
                    }
                }
            }
            list.add(polyLine.buildClosed());
        }
        return list;
    }

    private List<Attributed<Polygon>> assemblePolygon(OSMRelation relation, Attributes attributes) {
        List<Attributed<Polygon>> attributedPolygons = new ArrayList<Attributed<Polygon>>();
        List<ClosedPolyLine> outers = ringsForRole(relation, "outer");
        List<ClosedPolyLine> inners = ringsForRole(relation, "inner");
        Comparator<ClosedPolyLine> areaComparator = new Comparator<ClosedPolyLine>() {
            /**
             * Méthode qui redéfinit la méthode compare
             * de Comparator et retourne un entier dont le
             * signe désigne l'ordre des aires entre les deux
             * polylignes passées en paramètre
             * 
             * @author Andrey Batasov (250149)
             * @author Raja Soufi (247680)
             * 
             * @param p1
             *      la première polyligne pour la comparaison
             * @param p2
             *      la deuxième polyligne pour la comparaison
             * 
             * @return -1
             *      si l'aire de p1 est inférieure à celle de p2
             * @return 0
             *      si l'aire de p1 est égale à celle de p2
             * @return 1
             *      si l'aire de p1 est supérieure à celle de p2
             */
            @Override
            public int compare(ClosedPolyLine p1, ClosedPolyLine p2) {
                return Double.compare(p1.area(), p2.area());
            }
        };
        Collections.sort(outers, areaComparator);
        Collections.sort(inners, areaComparator);
        ListIterator<ClosedPolyLine> iterator = inners.listIterator();
        for (ClosedPolyLine outer : outers) {
            List<ClosedPolyLine> holes = new ArrayList<ClosedPolyLine>();
            while (iterator.hasNext()) {
                if (outer.containsPoint(iterator.next().firstPoint()) & (areaComparator.compare(outer,  iterator.previous()) > 0)) {
                    holes.add(iterator.next());
                    iterator.remove();
                } else {
                    iterator.next();
                }
            }
            iterator = inners.listIterator();
            if (holes.isEmpty()) {
                attributedPolygons.add(new Attributed<Polygon>(new Polygon(outer), attributes));
            } else {
                attributedPolygons.add(new Attributed<Polygon>(new Polygon(outer, holes), attributes));
            }
        }
        return attributedPolygons;
    }

    private Point nodeToPoint(OSMNode n) {
        return PROJECTION.project(n.position());
    }
    
    private boolean validArea(OSMWay way) {
        if (way.attributes().contains("area") && AREA_VALUES.contains(way.attributes().get("area"))) {
            return true;
        } else {
            for (String s : AREA_KEYS) {
                if (way.attributes().contains(s)) {
                    return true;
                }
            }
            return false;
        }
    }
}
