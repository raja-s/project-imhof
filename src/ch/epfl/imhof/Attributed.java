package ch.epfl.imhof;

/**
 * Classe générique qui représente un
 * objet et son ensemble d'attributs
 * 
 * @author Raja Soufi (247680)
 * @author Andrey Batasov (250149)
 */
public final class Attributed<T> {
    private final T VALUE;
    private final Attributes ATTRIBUTES;
    
    /**
     * Construit un objet et son ensemble d'attributs
     * 
     * @author Raja Soufi (247680)
     * @author Andrey Batasov (250149)
     * 
     * @param value
     *      l'objet auquel on veut associer les attributs
     * @param attributes
     *      l'ensemble d'attributs
     */
    public Attributed(T value, Attributes attributes) {
        VALUE = value;
        ATTRIBUTES = attributes;
    }
    
    /**
     * Méthode qui retourne la valeur à laquelle les
     * attributs sont attachés
     * 
     * @author Raja Soufi (247680)
     * @author Andrey Batasov (250149)
     * 
     * @return VALUE
     *      la valeur de l'objet
     */
    public T value() {
        return VALUE;
    }
    
    /**
     * Méthode qui retourne l'ensemble des attributs
     * de l'objet
     * 
     * @author Raja Soufi (247680)
     * @author Andrey Batasov (250149)
     * 
     * @return ATTRIBUTES
     *      l'ensemble des attributs
     */
    public Attributes attributes() {
        return ATTRIBUTES;
    }
    
    /**
     * Méthode qui teste si l'ensemble contient une certaine
     * clef ou non. Correspond à la méthode contains(String key)
     * de Attributes
     * 
     * @author Raja Soufi (247680)
     * @author Andrey Batasov (250149)
     * 
     * @param attributeName
     *      la clef qu'on veut tester
     * 
     * @return ATTRIBUTES.contains(attributeName);
     *      la valeur booléenne du test
     */
    public boolean hasAttribute(String attributeName) {
        return ATTRIBUTES.contains(attributeName);
    }
    
    /**
     * Méthode qui retourne la valeur associée à une certaine
     * clef ou null si l'ensemble ne contient pas cette clef.
     * Correspond à la méthode get(String key) de Attributes
     * 
     * @author Raja Soufi (247680)
     * @author Andrey Batasov (250149)
     * 
     * @param attributeName
     *      la clef dont on veut obtenir la
     *      valeur
     * 
     * @return ATTRIBUTES.get(attributeName)
     *      la valeur associée à la clef ou null
     */
    public String attributeValue(String attributeName) {
        return ATTRIBUTES.get(attributeName);
    }
    
    /**
     * Méthode qui retourne la valeur associée à une certaine
     * clef ou la valeur par défaut (passée en paramètre) si
     * l'ensemble ne contient pas cette clef. Correspond à la
     * méthode get(String key, String defaultValue) de Attributes
     * 
     * @author Raja Soufi (247680)
     * @author Andrey Batasov (250149)
     * 
     * @param attributeName
     *      la clef dont on veut obtenir la
     *      valeur
     * @param defaultValue
     *      la valeur par défaut
     * 
     * @return ATTRIBUTES.get(attributeName, defaultValue)
     *      la valeur associée à la clef ou la
     *      valeur par défaut
     */
    public String attributeValue(String attributeName, String defaultValue) {
        return ATTRIBUTES.get(attributeName, defaultValue);
    }
    
    /**
     * Méthode qui retourne la valeur associée à une certaine
     * clef ou la valeur par défaut sous forme d'un entier
     * (passée en paramètre) si l'ensemble ne contient pas cette
     * clef. Correspond à la méthode get(String key, int defaultValue)
     * de Attributes
     * 
     * @author Raja Soufi (247680)
     * @author Andrey Batasov (250149)
     * 
     * @param attributeName
     *      la clef dont on veut obtenir la
     *      valeur
     * @param defaultValue
     *      la valeur par défaut
     * 
     * @return ATTRIBUTES.get(attributeName, defaultValue)
     *      la valeur associée à la clef ou la
     *      valeur par défaut
     */
    public int attributeValue(String attributeName, int defaultValue) {
        return ATTRIBUTES.get(attributeName, defaultValue);
    }
}