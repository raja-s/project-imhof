package ch.epfl.imhof.painting;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import ch.epfl.imhof.Map;
import ch.epfl.imhof.Attributed;
import ch.epfl.imhof.geometry.ClosedPolyLine;
import ch.epfl.imhof.geometry.Polygon;
import ch.epfl.imhof.geometry.PolyLine;
import ch.epfl.imhof.painting.LineStyle.LineCap;
import ch.epfl.imhof.painting.LineStyle.LineJoin;
import ch.epfl.imhof.painting.Filters;

/**
 * Interface fonctionnelle qui représente un peintre
 *
 * @author Raja Soufi (247680)
 * @author Andrey Batasov (250149)
 */
public interface Painter {
    
    /**
     * Méthode fonctionnelle de l'interface qui permet de
     * dessiner une carte sur une toile (abstraite)
     * 
     * @author Raja Soufi (247680)
     * @author Andrey Batasov (250149)
     * 
     * @param map
     *      La carte à dessiner
     * @param canvas
     *      La toile à dessiner dessus
     */
    public void drawMap(Map map, Canvas canvas);
    
    /**
     * Méthode statique qui retourne un peintre (c-à-d une
     * définition de la méthode fonctionnelle) qui pour toute
     * carte et toile qu'il reçoit, dessine tous les polygones
     * de la carte sur la toile en remplissant leur intérieur
     * avec la couleur passée en paramètre
     * 
     * @author Raja Soufi (247680)
     * @author Andrey Batasov (250149)
     * 
     * @param color
     *      La couleur à utiliser pour remplir les polygones
     * 
     * @return (map, canvas) -> {}
     *      Le peintre dessinant les polygones
     */
    public static Painter polygon(Color color) {
        return (map, canvas) -> {
            for (Attributed<Polygon> attributedPolygon : map.polygons()) {
                canvas.drawPolygon(attributedPolygon.value(), color);
            }
        };
    }
    
    /**
     * Méthode statique qui retourne un peintre (c-à-d une
     * définition de la méthode fonctionnelle) qui pour toute
     * carte et toile qu'il reçoit, dessine tous les polylignes
     * de la carte sur la toile avec le style de ligne passé
     * en paramètre
     * 
     * @author Raja Soufi (247680)
     * @author Andrey Batasov (250149)
     * 
     * @param style
     *      Le style de ligne à utiliser pour dessiner les polylignes
     * 
     * @return (map, canvas) -> {}
     *      Le peintre dessinant les polylignes
     */
    public static Painter line(LineStyle style) {
        return (map, canvas) -> {
            for (Attributed<PolyLine> attributedPolyLine : map.polyLines()) {
                canvas.drawPolyLine(attributedPolyLine.value(), style);
            }
        };
    }
    
    /**
     * Méthode statique qui appelle la méthode line() et retourne
     * donc un peintre qui pour toute carte et toile qu'il reçoit,
     * dessine tous les polylignes de la carte sur la toile avec
     * le style de ligne construit à partir des données passées
     * en paramètre
     * 
     * @author Raja Soufi (247680)
     * @author Andrey Batasov (250149)
     * 
     * @param width
     *      L'épaisseur des lignes du style à passer à line()
     * @param color
     *      La couleur du style à passer à line()
     * @param cap
     *      Le modèle de terminaison des lignes du style à
     *      passer à line()
     * @param join
     *      Le modèle de jointure des segments du style à
     *      passer à line()
     * @param dash
     *      Le modèle d'alternance des sections opaques et transparentes
     *      des lignes du style à passer à line()
     * 
     * @return line(new LineStyle(color, cap, join, width, dash))
     *      Le peintre dessinant les polylignes
     */
    public static Painter line(float width, Color color, LineCap cap, LineJoin join, float... dash) {
        return line(new LineStyle(color, cap, join, width, dash));
    }
    
    /**
     * Méthode statique qui appelle la méthode line() et retourne
     * donc un peintre qui pour toute carte et toile qu'il reçoit,
     * dessine tous les polylignes de la carte sur la toile avec
     * le style de ligne construit à partir des données passées
     * en paramètre (quelques une n'étant pas passées pour en
     * prendre les valeurs par défaut)
     * 
     * @author Raja Soufi (247680)
     * @author Andrey Batasov (250149)
     * 
     * @param width
     *      L'épaisseur des lignes du style à passer à line()
     * @param color
     *      La couleur du style à passer à line()
     * 
     * @return line(new LineStyle(color, cap))
     *      Le peintre dessinant les polylignes
     */
    public static Painter line(float width, Color color) {
        return line(new LineStyle(width, color));
    }
    
    /**
     * Méthode statique qui retourne un peintre (c-à-d une
     * définition de la méthode fonctionnelle) qui pour toute
     * carte et toile qu'il reçoit, dessine les pourtours de
     * l'enveloppe et des trous de tous les polygones de la
     * carte sur la toile avec le style de ligne passé en
     * paramètre
     * 
     * @author Raja Soufi (247680)
     * @author Andrey Batasov (250149)
     * 
     * @param style
     *      Le style de ligne à utiliser pour dessiner les polylignes
     * 
     * @return (map, canvas) -> {}
     *      Le peintre dessinant les pourtours de l'enveloppe et
     *      des trous des polygones
     */
    public static Painter outline(LineStyle style) {
        return (map, canvas) -> {
            for (Attributed<Polygon> attributedPolygon : map.polygons()) {
                Polygon polygon = attributedPolygon.value();
                canvas.drawPolyLine(polygon.shell(), style);
                for (ClosedPolyLine hole : polygon.holes()) {
                    canvas.drawPolyLine(hole, style);
                }
            }
        };
    }
    
    /**
     * Méthode statique qui appelle la méthode outline() et retourne
     * donc un peintre qui pour toute carte et toile qu'il reçoit,
     * dessine les pourtours de l'enveloppe et des trous de tous
     * les polygones de la carte sur la toile avec le style de ligne
     * construit à partir des données passées en paramètre
     * 
     * @author Raja Soufi (247680)
     * @author Andrey Batasov (250149)
     * 
     * @param width
     *      L'épaisseur des lignes du style à passer à outline()
     * @param color
     *      La couleur du style à passer à outline()
     * @param cap
     *      Le modèle de terminaison des lignes du style à
     *      passer à outline()
     * @param join
     *      Le modèle de jointure des segments du style à
     *      passer à outline()
     * @param dash
     *      Le modèle d'alternance des sections opaques et transparentes
     *      des lignes du style à passer à outline()
     * 
     * @return outline(new LineStyle(color, cap, join, width, dash))
     *      Le peintre dessinant les pourtours de l'enveloppe et
     *      des trous des polygones
     */
    public static Painter outline(float width, Color color, LineCap cap, LineJoin join, float... dash) {
        return outline(new LineStyle(color, cap, join, width, dash));
    }
    
    /**
     * Méthode statique qui appelle la méthode outline() et retourne
     * donc un peintre qui pour toute carte et toile qu'il reçoit,
     * dessine les pourtours de l'enveloppe et des trous de tous
     * les polygones de la carte sur la toile avec le style de ligne
     * construit à partir des données passées en paramètre (quelques
     * une n'étant pas passées pour en prendre les valeurs par défaut)
     * 
     * @author Raja Soufi (247680)
     * @author Andrey Batasov (250149)
     * 
     * @param width
     *      L'épaisseur des lignes du style à passer à outline()
     * @param color
     *      La couleur du style à passer à outline()
     * 
     * @return outline(new LineStyle(width, color))
     *      Le peintre dessinant les pourtours de l'enveloppe et
     *      des trous des polygones
     */
    public static Painter outline(float width, Color color) {
        return outline(new LineStyle(width, color));
    }
    
    /**
     * Méthode par défaut qui retourne un peintre identique à celui
     * auquel on l'applique, mais qui ne dessine que les éléments
     * de la carte qui satisfont le prédicat passé en paramètre
     * 
     * @author Raja Soufi (247680)
     * @author Andrey Batasov (250149)
     * 
     * @param predicate
     *      Le prédicat qui filtre les éléments à dessiner
     * 
     * @return (map, canvas) -> {}
     *      Le peintre dessinant les éléments filtrés
     */
    public default Painter when(Predicate<Attributed<?>> predicate) {
        return (map, canvas) -> {
            List<Attributed<Polygon>> polygons = new ArrayList<>(map.polygons());
            List<Attributed<PolyLine>> polyLines = new ArrayList<>(map.polyLines());
            polygons.removeIf(p -> !predicate.test(p));
            polyLines.removeIf(p -> !predicate.test(p));
            drawMap(new Map(polyLines, polygons), canvas);
        };
    }
    
    /**
     * Méthode par défaut qui retourne un peintre qui correspond à
     * l'empilement du peintre auquel on l'applique sur le peintre
     * passé en paramètre, et qui effectue donc les dessins d'abord
     * avec le peintre passé en paramètre puis avec le peintre
     * auquel on applique la méthode
     * 
     * @author Raja Soufi (247680)
     * @author Andrey Batasov (250149)
     * 
     * @param first
     *      Le peintre sur lequel on veut empiler le peintre auquel
     *      on applique la méthode
     * 
     * @return (map, canvas) -> {}
     *      Le peintre composite
     */
    public default Painter above(Painter first) {
        return (map, canvas) -> {
            first.drawMap(map, canvas);
            drawMap(map, canvas);
        };
    }
    
    /**
     * Méthode par défaut qui retourne un peintre qui utilise le
     * peintre auquel on l'applique pour dessiner la carte passée
     * à celui-ci par couches, c-à-d en dessinant d'abord tous les
     * éléments de la couche -5, puis tous ceux de -4, et ainsi de
     * suite jusqu'à la couche +5
     * 
     * @author Raja Soufi (247680)
     * @author Andrey Batasov (250149)
     * 
     * @return (map, canvas) -> {}
     *      Le peintre dessinant une carte par couches
     */
    public default Painter layered() {
        return (map, canvas) -> {
            Painter painter = when(Filters.onLayer(-5));
            for (int i = -4; i <= 5; i++) {
                painter = when(Filters.onLayer(i)).above(painter);
            }
            painter.drawMap(map, canvas);
        };
    }
}
