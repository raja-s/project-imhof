package ch.epfl.imhof.painting;

import ch.epfl.imhof.bonus.LegendElement;

/**
 * Classe représenant une couleur définit par ses
 * composantes rouge, vert et bleu
 * (cette classe a été modifiée pour le rendu
 * BONUS, et implémente désormais l'interface
 * LegendElement)
 * 
 * @author Raja Soufi (247680)
 * @author Andrey Batasov (250149)
 */
public final class Color implements LegendElement {
    private final double R, G, B;
    
    /**
     * La couleur rouge pûr
     * 
     * @author Raja Soufi (247680)
     * @author Andrey Batasov (250149)
     */
    public static final Color RED = new Color(1, 0, 0);
    
    /**
     * La couleur vert pûr
     * 
     * @author Raja Soufi (247680)
     * @author Andrey Batasov (250149)
     */
    public static final Color GREEN = new Color(0, 1, 0);
    
    /**
     * La couleur bleu pûr
     * 
     * @author Raja Soufi (247680)
     * @author Andrey Batasov (250149)
     */
    public static final Color BLUE = new Color(0, 0, 1);
    
    /**
     * La couleur noir pûr
     * 
     * @author Raja Soufi (247680)
     * @author Andrey Batasov (250149)
     */
    public static final Color BLACK = new Color(0, 0, 0);
    
    /**
     * La couleur blanc pûr
     * 
     * @author Raja Soufi (247680)
     * @author Andrey Batasov (250149)
     */
    public static final Color WHITE = new Color(1, 1, 1);
    
    private Color(double r, double g, double b) {
        R = r;
        G = g;
        B = b;
    }
    
    /**
     * Méthode statique de construction qui construit une
     * nuance du gris, c-à-d qui reçoit un réel entre 0 et 1,
     * et qui construit une couleur avec ce réel pour les trois
     * composantes rouge, vert et bleu
     * 
     * @author Raja Soufi (247680)
     * @author Andrey Batasov (250149)
     * 
     * @param greyLevel
     *      le réel avec lequel on voudrait construire la nuance
     *      de gris
     * 
     * @throws IllegalArgumentException
     *      si la valeur passée en paramètre n'est pas comprise
     *      entre 0 et 1
     * 
     * @return new Color(greyLevel, greyLevel, greyLevel)
     *      la nuance de gris créée
     */
    public static Color gray(double greyLevel) {
        if ((greyLevel < 0) || (greyLevel > 1)) {
            throw new IllegalArgumentException("The given value is out of bounds!");
        } else {
            return new Color(greyLevel, greyLevel, greyLevel);
        }
    }
    
    /**
     * Méthode statique de construction qui construit une
     * couleur à partir des trois composantes passées en
     * paramètre
     * 
     * @author Raja Soufi (247680)
     * @author Andrey Batasov (250149)
     * 
     * @param r
     *      la composante rouge
     * @param g
     *      la composante vert
     * @param b
     *      la composante bleu
     * 
     * @throws IllegalArgumentException
     *      si une des trois valeurs passées en paramètre n'est
     *      pas comprise entre 0 et 1
     * 
     * @return new Color(r, g, b)
     *      la couleur construite
     */
    public static Color rgb(double r, double g, double b) {
        if ((r < 0) || (r > 1) || (g < 0) || (g > 1) || (b < 0) || (b > 1)) {
            throw new IllegalArgumentException("The given value is out of bounds!");
        } else {
            return new Color(r, g, b);
        }
    }
    
    /**
     * Méthode statique de construction qui construit une
     * couleur à partir des trois composantes "empaquetées"
     * dans un entier passé en paramètre
     * 
     * @author Raja Soufi (247680)
     * @author Andrey Batasov (250149)
     * 
     * @param i
     *      l'entier qui "contient" les trois composantes
     * 
     * @throws IllegalArgumentException
     *      si l'entier passé en paramètre est en dehors des
     *      bornes qui définissent la capacité d'un int en Java
     * 
     * @return new Color((i >> 16) & 0xFF, (i >> 8) & 0xFF, i & 0xFF)
     *      la couleur construite
     */
    public static Color rgb(int i) {
        if ((i < -2_147_483_648) || (i > 2_147_483_647)) {
            throw new IllegalArgumentException("The given value is out of bounds!");
        } else {
            return new Color(((i >> 16) & 0xFF) / 256d, ((i >> 8) & 0xFF) / 256d, (i & 0xFF) / 256d);
        }
    }
    
    /**
     * Getter de la composante rouge
     * 
     * @author Raja Soufi (247680)
     * @author Andrey Batasov (250149)
     * 
     * @return R
     *      la composante rouge
     */
    public double r() {
        return R;
    }
    
    /**
     * Getter de la composante vert
     * 
     * @author Raja Soufi (247680)
     * @author Andrey Batasov (250149)
     * 
     * @return G
     *      la composante vert
     */
    public double g() {
        return G;
    }
    
    /**
     * Getter de la composante bleu
     * 
     * @author Raja Soufi (247680)
     * @author Andrey Batasov (250149)
     * 
     * @return B
     *      la composante bleu
     */
    public double b() {
        return B;
    }
    
    /**
     * Méthode qui "multiplie" deux couleurs, c-à-d qui retourne
     * une couleur dont la valeur de chaque composante est le produit
     * de la valeur de la composante de la couleur à laquelle on
     * applique la méthode et de celle de la couleur passée en paramètre
     * 
     * @author Raja Soufi (247680)
     * @author Andrey Batasov (250149)
     * 
     * @param color
     *      la seconde couleur intervenant dans le produit
     * 
     * @return new Color(R * color.r(), G * color.g(), B * color.b())
     *      la couleur correspondant qu produit
     */
    public Color multiply(Color color) {
        return new Color(R * color.r(), G * color.g(), B * color.b());
    }
    
    /**
     * Méthode qui convertit une couleur en une couleur
     * du type prédéfini dans l'API Java
     * 
     * @author Raja Soufi (247680)
     * @author Andrey Batasov (250149)
     * 
     * @return java.awt.Color(R, G, B)
     *      la couleur convertie
     */
    public java.awt.Color convert() {
        return new java.awt.Color((float)R, (float)G, (float)B);
    }
    
    /**
     * Getter de la couleur, utile pour la création
     * de légendes
     * (cette méthode a été ajoutée pour la partie
     * BONUS)
     *
     * @author Raja Soufi (247680)
     * @author Andrey Batasov (250149)
     * 
     * @return this
     *      La couleur
     */
    @Override
    public Color getLegendElement() {
        return this;
    }
}
