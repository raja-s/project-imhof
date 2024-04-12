package ch.epfl.imhof.painting;

import ch.epfl.imhof.geometry.PolyLine;
import ch.epfl.imhof.geometry.Polygon;

/**
 * Interface qui représente une toile
 *
 * @author Raja Soufi (247680)
 * @author Andrey Batasov (250149)
 */
public interface Canvas {
    
    /**
     * Méthode qui dessine la polyligne passée en paramètre
     * avec le style de ligne passé en paramètre sur la toile
     * (abstraite, redéfinie dans les classes qui implémentent
     * l'interface)
     *
     * @author Raja Soufi (247680)
     * @author Andrey Batasov (250149)
     *
     * @param polyLine
     *      la polyLigne à dessiner
     * @param lineStyle
     *      le style de ligne à utiliser lors du dessin
     */
    public void drawPolyLine(PolyLine polyLine, LineStyle lineStyle);
    
    /**
     * Méthode qui dessine la polygone passée en paramètre sur
     * la toile et la remplit avec la couleur passé en paramètre
     * (abstraite, redéfinie dans les classes qui implémentent
     * l'interface)
     *
     * @author Raja Soufi (247680)
     * @author Andrey Batasov (250149)
     *
     * @param polygone
     *      la polygone à dessiner
     * @param lineStyle
     *      la couleur à utiliser pour remplir la polygone
     */
    public void drawPolygon(Polygon polygon, Color color);
}
