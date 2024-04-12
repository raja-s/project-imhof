package ch.epfl.imhof.painting;

import ch.epfl.imhof.bonus.LegendElement;

/**
 * Classe représenant un style de ligne
 * (cette classe a été modifiée pour le rendu
 * BONUS, et implémente désormais l'interface
 * LegendElement)
 * 
 * @author Raja Soufi (247680)
 * @author Andrey Batasov (250149)
 */
public final class LineStyle implements LegendElement {
    private final Color COLOR;
    private final LineCap CAP;
    private final LineJoin JOIN;
    private final float WIDTH;
    private final float[] DASH;
    
    /**
     * Construit un style de ligne avec les paramètres donnés
     * 
     * @author Raja Soufi (247680)
     * @author Andrey Batasov (250149)
     * 
     * @param color
     *      La couleur du style à construire
     * @param cap
     *      Le modèle de terminaison des lignes du style à construire
     * @param join
     *      Le modèle de jointure des segments du style à construire
     * @param width
     *      L'épaisseur des lignes du style à construire
     * @param dash
     *      Le modèle d'alternance des sections opaques et transparentes
     *      des lignes du style à construire
     * 
     * @throws IllegalArgumentException
     *      Si une des valeurs dans le tableau représentant la séquence
     *      d'alternance des segments n'est pas strictement positive, ou
     *      bien si la valeur représentant l'épaisseur des lignes passée
     *      en paramètre n'est pas positive
     */
    public LineStyle(Color color, LineCap cap, LineJoin join, float width, float[] dash) {
        for (int i = 0 ; i < dash.length ; i++) {
            if (dash[i] <= 0) {
                throw new IllegalArgumentException("Invalid dashing pattern!");
            }
        }
        if (width < 0) {
            throw new IllegalArgumentException("Invalid width!");
        }
        COLOR = color;
        CAP = cap;
        JOIN = join;
        WIDTH = width;
        if (dash.length == 0) {
            DASH = null;
        } else {
            DASH = new float[dash.length];
            for (int i = 0 ; i < dash.length ; i++) {
                DASH[i] = dash[i];
            }
        }
    }
    
    /**
     * Construit un style de ligne avec les deux paramètres donnés,
     * et en remplaçant les valeurs manquant à la construction par
     * Cap.CAP_BUTT, Join.JOIN_MITER et new float[0] respectivement
     * 
     * @author Raja Soufi (247680)
     * @author Andrey Batasov (250149)
     * 
     * @param color
     *      La couleur du style à construire
     * @param width
     *      L'épaisseur des lignes du style à construire
     */
    public LineStyle(float width, Color color) {
        this(color, LineCap.Butt, LineJoin.Miter, width, new float[0]);
    }
    
    /**
     * Getter de la couleur
     * 
     * @author Raja Soufi (247680)
     * @author Andrey Batasov (250149)
     * 
     * @return COLOR
     *      La couleur du style
     */
    public Color color() {
        return COLOR;
    }
    
    /**
     * Getter du modèle de terminaison des lignes
     * 
     * @author Raja Soufi (247680)
     * @author Andrey Batasov (250149)
     * 
     * @return CAP
     *      Le modèle de terminaison des lignes du style
     */
    public LineCap cap() {
        return CAP;
    }
    
    /**
     * Getter du modèle de jointure des segments
     * 
     * @author Raja Soufi (247680)
     * @author Andrey Batasov (250149)
     * 
     * @return JOIN
     *      Le modèle de jointure des segments du style
     */
    public LineJoin join() {
        return JOIN;
    }
    
    /**
     * Getter de l'épaisseur des lignes
     * 
     * @author Raja Soufi (247680)
     * @author Andrey Batasov (250149)
     * 
     * @return WIDTH
     *      L'épaisseur des lignes du style
     */
    public float width() {
        return WIDTH;
    }
    
    /**
     * Getter de la séquence d'alternance des segments
     * 
     * @author Raja Soufi (247680)
     * @author Andrey Batasov (250149)
     * 
     * @return DASH
     *      La séquence d'alternance des segments du style
     */
    public float[] dash() {
        return DASH;
    }
    
    /**
     * Méthode qui retourne un style de ligne avec les mêmes
     * propriétés que celui auquel on applique la méthode, sauf
     * la couleur, qui est passée en paramètre
     * 
     * @author Raja Soufi (247680)
     * @author Andrey Batasov (250149)
     * 
     * @param color
     *      La couleur avec laquelle on veut construire un style
     * 
     * @return new LineStyle(color, CAP, JOIN, WIDTH, DASH);
     *      Le style de ligne construit
     */
    public LineStyle withColor(Color color) {
        return new LineStyle(color, CAP, JOIN, WIDTH, DASH);
    }
    
    /**
     * Méthode qui retourne un style de ligne avec les mêmes
     * propriétés que celui auquel on applique la méthode, sauf
     * le modèle de terminaison des lignes, qui est passé en
     * paramètre
     * 
     * @author Raja Soufi (247680)
     * @author Andrey Batasov (250149)
     * 
     * @param cap
     *      Le modèle de terminaison des lignes avec lequel
     *      on veut construire un style
     * 
     * @return new LineStyle(COLOR, cap, JOIN, WIDTH, DASH);
     *      Le style de ligne construit
     */
    public LineStyle withLineCap(LineCap cap) {
        return new LineStyle(COLOR, cap, JOIN, WIDTH, DASH);
    }
    
    /**
     * Méthode qui retourne un style de ligne avec les mêmes
     * propriétés que celui auquel on applique la méthode, sauf
     * le modèle de jointure des segments, qui est passé en
     * paramètre
     * 
     * @author Raja Soufi (247680)
     * @author Andrey Batasov (250149)
     * 
     * @param join
     *      Le modèle de jointure des segments avec lequel
     *      on veut construire un style
     * 
     * @return new LineStyle(COLOR, CAP, join, WIDTH, DASH);
     *      Le style de ligne construit
     */
    public LineStyle withLineJoin(LineJoin join) {
        return new LineStyle(COLOR, CAP, join, WIDTH, DASH);
    }
    
    /**
     * Méthode qui retourne un style de ligne avec les mêmes
     * propriétés que celui auquel on applique la méthode, sauf
     * l'épaisseur des lignes, qui est passée en paramètre
     * 
     * @author Raja Soufi (247680)
     * @author Andrey Batasov (250149)
     * 
     * @param width
     *      L'épaisseur des lignes avec laquelle on veut
     *      construire un style
     * 
     * @return new LineStyle(COLOR, CAP, JOIN, width, DASH);
     *      Le style de ligne construit
     */
    public LineStyle withWidth(float width) {
        return new LineStyle(COLOR, CAP, JOIN, width, DASH);
    }
    
    /**
     * Méthode qui retourne un style de ligne avec les mêmes
     * propriétés que celui auquel on applique la méthode, sauf
     * la séquence d'alternance des segments, qui est passée
     * en paramètre
     * 
     * @author Raja Soufi (247680)
     * @author Andrey Batasov (250149)
     * 
     * @param dash
     *      La séquence d'alternance des segments avec laquelle
     *      on veut construire un style
     * 
     * @return new LineStyle(COLOR, CAP, JOIN, WIDTH, dash);
     *      Le style de ligne construit
     */
    public LineStyle withDashingPattern(float[] dash) {
        return new LineStyle(COLOR, CAP, JOIN, WIDTH, dash);
    }
    
    /**
     * Énumération qui représente un modèle de terminaison
     * des lignes
     * 
     * @author Raja Soufi (247680)
     * @author Andrey Batasov (250149)
     */
    public enum LineCap {
        Butt, Round, Square;
    }
    
    /**
     * Énumération qui représente un modèle de jointure
     * des segments
     * 
     * @author Raja Soufi (247680)
     * @author Andrey Batasov (250149)
     */
    public enum LineJoin {
        Bevel, Miter, Round;
    }
    
    /**
     * Getter du style de ligne, utile pour la création
     * de légendes
     * (cette méthode a été ajoutée pour la partie
     * BONUS)
     *
     * @author Raja Soufi (247680)
     * @author Andrey Batasov (250149)
     * 
     * @return this
     *      Le style de ligne
     */
    @Override
    public LineStyle getLegendElement() {
        return this;
    }
}
