package ch.epfl.imhof;

/**
 * Classe représentant un vecteur tridimensionnel
 * 
 * @author Andrey Batasov (250149)
 * @author Raja Soufi (247680)
 *
 */
public final class Vector3 {
    private final double X, Y, Z;
    
    /**
     * Construit un vecteur
     * 
     * @author Andrey Batasov (250149)
     * @author Raja Soufi (247680)
     * 
     * @param x
     *      La composante x
     * @param y
     *      La composante y
     * @param z
     *      La composante z
     */
    public Vector3(double x, double y, double z) {
        X = x;
        Y = y;
        Z = z;
    }
    
    /**
     * Getter de la composante x
     * 
     * @author Raja Soufi (247680)
     * @author Andrey Batasov (250149)
     * 
     * @return X
     *      La composante x du vecteur
     */
    public double x() {
        return X;
    }
    
    /**
     * Getter de la composante y
     * 
     * @author Raja Soufi (247680)
     * @author Andrey Batasov (250149)
     * 
     * @return Y
     *      La composante y du vecteur
     */
    public double y() {
        return Y;
    }
    
    /**
     * Getter de la composante z
     * 
     * @author Raja Soufi (247680)
     * @author Andrey Batasov (250149)
     * 
     * @return Z
     *      La composante z du vecteur
     */
    public double z() {
        return Z;
    }
    
    /**
     * Méthode qui calcule la norme du vecteur
     * 
     * @author Raja Soufi (247680)
     * @author Andrey Batasov (250149)
     * 
     * @return Math.sqrt(Math.pow(X, 2) + Math.pow(Y, 2) + Math.pow(Z, 2))
     *      La norme du vecteur
     */
    public double norm() {
        return Math.sqrt(Math.pow(X, 2) + Math.pow(Y, 2) + Math.pow(Z, 2));
    }
    
    /**
     * Méthode qui retourne le vecteur unitaire associé
     * au vecteur
     * 
     * @author Raja Soufi (247680)
     * @author Andrey Batasov (250149)
     * 
     * @return new Vector3(X/norm, Y/norm, Z/norm)
     *      Le vecteur unitaire associé au vecteur
     */
    public Vector3 normalized() {
        double norm = norm();
        return new Vector3(X / norm, Y / norm, Z / norm);
    }
    
    /**
     * Méthode qui retourne le produit scalaire du vecteur
     * et d'un autre passé en paramètre
     * 
     * @author Raja Soufi (247680)
     * @author Andrey Batasov (250149)
     * 
     * @param v
     *      Le second facteur du produit scalaire
     * 
     * @return (X * v.x() + Y * v.y() + Z * v.z())
     *      Le resultat du produit scalaire
     */
    public double scalarProduct(Vector3 v) {
        return (X * v.x() + Y * v.y() + Z * v.z());
    }
}