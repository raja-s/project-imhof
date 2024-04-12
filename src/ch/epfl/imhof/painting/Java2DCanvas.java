package ch.epfl.imhof.painting;

import static java.awt.RenderingHints.KEY_ANTIALIASING;
import static java.awt.RenderingHints.VALUE_ANTIALIAS_ON;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.geom.Area;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import ch.epfl.imhof.geometry.ClosedPolyLine;
import ch.epfl.imhof.geometry.Point;
import ch.epfl.imhof.geometry.PolyLine;
import ch.epfl.imhof.geometry.Polygon;

/**
 * Classe représenant une toile sur laquelle on
 * peut dessiner les éléments d'une carte
 * 
 * @author Raja Soufi (247680)
 * @author Andrey Batasov (250149)
 */
public final class Java2DCanvas implements Canvas {
    private final Function<Point, Point> COORDINATE_CHANGE;
    private final BufferedImage IMAGE;
    private final Graphics2D CONTEXT;
    
    /**
     * Construit une toile avec les paramètres donnés
     * 
     * @author Raja Soufi (247680)
     * @author Andrey Batasov (250149)
     * 
     * @param bl
     *      Le point le plus bas et le plus à gauche
     * @param tr
     *      Le point le plus haut et le plus à droite
     * @param width
     *      La largeur en pixels de la toile
     * @param height
     *      La hauteur en pixels de la toile
     * @param res
     *      La résolution de l'image à dessiner sur la toile
     * @param color
     *      La couleur de fond de la toile
     * 
     * @throws IllegalArgumentException
     *      Si la résolution passée en paramètre n'est pas
     *      strictement positive
     */
    public Java2DCanvas(Point bl, Point tr, int width, int height, double res, Color color) {
        if (res <= 0) {
            throw new IllegalArgumentException("The entered resolution must be strictly positive!");
        } else {
            COORDINATE_CHANGE = Point.alignedCoordinateChange(bl, new Point(0, height * 72 / res), tr, new Point(width * 72 / res, 0));
            IMAGE = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            CONTEXT = IMAGE.createGraphics();
            CONTEXT.setRenderingHint(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON);
            CONTEXT.setColor(color.convert());
            CONTEXT.fillRect(0, 0, width, height);
            CONTEXT.scale(res / 72, res / 72);
        }
    }
    
    /**
     * Méthode qui dessine la polyligne passée en paramètre
     * avec le style de ligne passé en paramètre sur la toile
     * 
     * @author Raja Soufi (247680)
     * @author Andrey Batasov (250149)
     * 
     * @param polyLine
     *      La polyLigne à dessiner
     * @param lineStyle
     *      Le style de ligne à utiliser lors du dessin
     */
    @Override
    public void drawPolyLine(PolyLine polyLine, LineStyle style) {
        List<Point> list = new ArrayList<Point>(polyLine.points());
        Path2D path = new Path2D.Double();
        list.replaceAll(p -> COORDINATE_CHANGE.apply(p));
        path.moveTo(list.get(0).x(), list.get(0).y());
        for (int i = 1 ; i < list.size() ; i++) {
            path.lineTo(list.get(i).x(), list.get(i).y());
        }
        if (polyLine.isClosed()) {
            path.closePath();
        }
        CONTEXT.setColor(style.color().convert());
        CONTEXT.setStroke(new BasicStroke(style.width(), style.cap().ordinal(), style.join().ordinal(), 10.0f, style.dash(), 0f));
        CONTEXT.draw(path);
    }
    
    /**
     * Méthode qui dessine la polygone passée en paramètre sur
     * la toile et la remplit avec la couleur passé en paramètre
     *
     * @author Raja Soufi (247680)
     * @author Andrey Batasov (250149)
     *
     * @param polygone
     *      La polygone à dessiner
     * @param lineStyle
     *      La couleur à utiliser pour remplir la polygone
     */
    @Override
    public void drawPolygon(Polygon polygon, Color color) {
        List<Point> list = new ArrayList<Point>(polygon.shell().points());
        Path2D path = new Path2D.Double();
        list.replaceAll(p -> COORDINATE_CHANGE.apply(p));
        path.moveTo(list.get(0).x(), list.get(0).y());
        for (int i = 1 ; i < list.size() ; i++) {
            path.lineTo(list.get(i).x(), list.get(i).y());
        }
        path.closePath();
        Area area = new Area(path);
        for (ClosedPolyLine polyLine : polygon.holes()) {
            list = new ArrayList<Point>(polyLine.points());
            path = new Path2D.Double();
            list.replaceAll(p -> COORDINATE_CHANGE.apply(p));
            path.moveTo(list.get(0).x(), list.get(0).y());
            for (int i = 1 ; i < list.size() ; i++) {
                path.lineTo(list.get(i).x(), list.get(i).y());
            }
            path.closePath();
            area.subtract(new Area(path));
        }
        CONTEXT.setColor(color.convert());
        CONTEXT.fill(area);
    }
    
    /**
     * Getter de la toile sous forme d'image du type
     * BufferedImage, prédéfini dans l'API Java
     *
     * @author Raja Soufi (247680)
     * @author Andrey Batasov (250149)
     *
     * @return IMAGE
     *      L'image correspondant à la toile
     */
    public BufferedImage image() {
        return IMAGE;
    }
}
