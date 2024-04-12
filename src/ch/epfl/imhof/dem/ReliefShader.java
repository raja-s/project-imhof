package ch.epfl.imhof.dem;

import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.util.function.Function;

import ch.epfl.imhof.Vector3;
import ch.epfl.imhof.geometry.Point;
import ch.epfl.imhof.projection.Projection;
import static java.awt.image.BufferedImage.TYPE_INT_RGB;
import static java.awt.image.ConvolveOp.EDGE_NO_OP;
import static java.lang.Math.*;

/**
 * Classe représentant un générateur de reliefs
 * ombrés colorés
 *
 * @author Raja Soufi (247680)
 * @author Andrey Batasov (250149)
 */
public final class ReliefShader {
    private final Projection PROJECTION;
    private final DigitalElevationModel MODEL;
    private final Vector3 VECTOR;
    
    /**
     * Construit un générateur de reliefs ombrés colorés avec les
     * paramètres passés
     * 
     * @author Andrey Batasov (250149)
     * @author Raja Soufi (247680)
     * 
     * @param projection
     *      La projection à utiliser pour générer les reliefs ombrés
     * @param model
     *      Le modèle numérique du terrain à utiliser pour générer
     *      les reliefs ombrés
     * @param vector
     *      Le vecteur qui pointe dans la direction de la
     *      source lumineuse, et qui détermine donc, pour
     *      les reliefs à dessiner, de quel côté arrivent
     *      les rayons lumineux
     */
    public ReliefShader(Projection projection, DigitalElevationModel model, Vector3 vector) {
        PROJECTION = projection;
        MODEL = model;
        VECTOR = vector;
    }
    
    /**
     * Méthode qui génère et retourne un relief ombré coloré
     * selon les points et les dimensions passés en paramètre
     * et les propriétés du générateur auquel on applique la
     * méthode
     * 
     * @author Andrey Batasov (250149)
     * @author Raja Soufi (247680)
     * 
     * @param bl
     *      Le point bas-gauche du relief ombré à générer,
     *      en coordonnées du plan
     * @param tr
     *      Le point haut-droite du relief ombré à générer,
     *      en coordonnées du plan
     * @param width
     *      La largeur du relief ombré à générer
     * @param height
     *      La hauteur du relief ombré à générer
     * @param radius
     *      Le rayon de floutage du relief ombré à générer
     * 
     * @return ----------
     *      Le relief ombré généré (flouté ou pas selon le rayon
     *      de floutage)
     */
    public BufferedImage shadedRelief(Point bl, Point tr, int width, int height, double radius) {
        if (radius == 0) {
            return unblurred(width, height, Point.alignedCoordinateChange(new Point(0, height), bl, new Point(width, 0), tr));
        } else {
            float[] coefs = coefs(radius);
            int out = (coefs.length - 1) / 2;
            return blurred(unblurred(width + 2 * out, height + 2 * out, Point.alignedCoordinateChange(new Point(out, height + out), bl, new Point(width + out, out),  tr)), coefs).getSubimage(out, out, width, height);
        }
    }
    
    /**
     * Méthode qui dessine et retourne un relief ombré coloré
     * brut, c-à-d un relief non flouté
     * 
     * @author Andrey Batasov (250149)
     * @author Raja Soufi (247680)
     * 
     * @param width
     *      La largeur du relief ombré à générer
     * @param height
     *      La hauteur du relief ombré à générer
     * @param coordinateChange
     *      Le changement de repère qui permet de passer du repère
     *      de l'image à celui du plan
     * 
     * @return image
     *      Le relief ombré coloré brut
     */
    private BufferedImage unblurred(int width, int height, Function<Point, Point> coordinateChange) {
        BufferedImage image = new BufferedImage(width, height, TYPE_INT_RGB);
        for (int i = 0 ; i < width ; i++) {
            for (int j = 0 ; j < height ; j++) {
                Vector3 vector = MODEL.normalAt(PROJECTION.inverse(coordinateChange.apply(new Point(i, j))));
                double cos = VECTOR.scalarProduct(vector) / (VECTOR.norm() * vector.norm());
                int rg = (int)((cos + 1) * 255.9999 / 2);
                int b = (int)((0.7 * cos + 1) * 255.9999 / 2);
                image.setRGB(i, j, (rg << 16) + (rg << 8) + b);
            }
        }
        return image;
    }
    
    /**
     * Méthode qui retourne un "noyau" (en réalité un vecteur
     * qui sera utilisé pour en créer un noyau) dont la taille
     * est déterminée selon le rayon de floutage passé en
     * paramètre, et dont les coefficients sont calculés au
     * moyen de la fonction gaussienne à une dimension
     * 
     * @author Andrey Batasov (250149)
     * @author Raja Soufi (247680)
     * 
     * @param radius
     *      Le rayon de floutage
     * 
     * @return coefs
     *      Le "noyau" (vecteur) calculé
     */
    private float[] coefs(double radius) {
        int r = (int)ceil(radius);
        int n = 2 * r + 1;
        double sigma = radius / 3;
        float[] coefs = new float[n];
        double sum = 0;
        for (int i = - r ; i <= r ; i++) {
            float coef = (float)pow(E, - (pow(i, 2) / (2 * pow(sigma, 2))));
            coefs[i + r] = coef;
            sum += coef;
        }
        for (int i = 0 ; i < n ; i++) {
            coefs[i] /= sum;
        }
        return coefs;
    }
    
    /**
     * Méthode qui réalise le floutage gaussien du relief ombré passé
     * en paramètre, et retourne le relief flouté sous forme d'un
     * nouveau relief ombré (le floutage est constitué de deux
     * floutages gaussiens unidimensionnels, l'un horizontal, l'autre
     * vertical)
     * 
     * @author Andrey Batasov (250149)
     * @author Raja Soufi (247680)
     * 
     * @param image
     *      Le relief ombré à flouter
     * @param coefs
     *      Le vecteur à partir duquel on veut obtenir un noyau
     *      qui déterminera le "poids" du floutage à réaliser
     * 
     * @return yOp.filter(xOp.filter(image, null), null)
     *      Le relief ombré flouté
     */
    private BufferedImage blurred(BufferedImage image, float[] coefs) {
        Kernel horizontal = new Kernel(coefs.length, 1, coefs);
        Kernel vertical = new Kernel(1, coefs.length, coefs);
        ConvolveOp xOp = new ConvolveOp(horizontal, EDGE_NO_OP, null);
        ConvolveOp yOp = new ConvolveOp(vertical, EDGE_NO_OP, null);
        return yOp.filter(xOp.filter(image, null), null);
    }
}
