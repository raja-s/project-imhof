package ch.epfl.imhof;

import java.util.Collections;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;

/**
 * Classe qui représente un ensemble de
 * paires clef/valeur
 * 
 * @author Raja Soufi (247680)
 * @author Andrey Batasov (250149)
 */
public final class Attributes {
    private final Map<String, String> ATTRIBUTES;
    
    /**
     * Construit un ensemble de paires clef/valeur
     * 
     * @author Raja Soufi (247680)
     * @author Andrey Batasov (250149)
     * 
     * @param attributes
     *      un ensemble de paires clef/valeur
     */
    public Attributes(Map<String, String> attributes) {
        ATTRIBUTES = Collections.unmodifiableMap(new HashMap<String, String>(attributes));
    }
    
    /**
     * Méthode qui teste si l'ensemble est
     * vide ou non
     * 
     * @author Raja Soufi (247680)
     * @author Andrey Batasov (250149)
     * 
     * @return ATTRIBUTES.isEmpty()
     *      la valeur booléenne du test
     */
    public boolean isEmpty() {
        return ATTRIBUTES.isEmpty();
    }
    
    /**
     * Méthode qui teste si l'ensemble contient
     * une certaine clef ou non
     * 
     * @author Raja Soufi (247680)
     * @author Andrey Batasov (250149)
     * 
     * @param key
     *      la clef qu'on veut tester
     * 
     * @return ATTRIBUTES.containsKey(key)
     *      la valeur booléenne du test
     */
    public boolean contains(String key) {
        return ATTRIBUTES.containsKey(key);
    }
    
    /**
     * Méthode qui retourne la valeur associée
     * à une certaine clef ou null si l'ensemble
     * ne contient pas cette clef
     * 
     * @author Raja Soufi (247680)
     * @author Andrey Batasov (250149)
     * 
     * @param key
     *      la clef dont on veut obtenir la
     *      valeur
     * 
     * @return ATTRIBUTES.get(key)
     *      la valeur associée à la clef ou null
     */
    public String get(String key) {
        return ATTRIBUTES.get(key);
    }
    
    /**
     * Méthode qui retourne la valeur associée
     * à une certaine clef ou la valeur par défaut
     * (passée en paramètre) si l'ensemble ne
     * contient pas cette clef
     * 
     * @author Raja Soufi (247680)
     * @author Andrey Batasov (250149)
     * 
     * @param key
     *      la clef dont on veut obtenir la
     *      valeur
     * @param defaultValue
     *      la valeur par défaut
     * 
     * @return ATTRIBUTES.getOrDefault(key, defaultValue)
     *      la valeur associée à la clef ou la
     *      valeur par défaut
     */
    public String get(String key, String defaultValue) {
        return ATTRIBUTES.getOrDefault(key, defaultValue);
    }
    
    /**
     * Méthode qui retourne la valeur associée
     * à une certaine clef ou la valeur par défaut
     * sous forme d'un entier (passée en paramètre)
     * si l'ensemble ne contient pas cette clef
     * 
     * @author Raja Soufi (247680)
     * @author Andrey Batasov (250149)
     * 
     * @param key
     *      la clef dont on veut obtenir la
     *      valeur
     * @param defaultValue
     *      la valeur par défaut
     * 
     * @return Integer.parseInt(ATTRIBUTES.get(key))
     *      la valeur associée à la clef
     * @return defaultValue
     *      la valeur par défaut
     */
    public int get(String key, int defaultValue) {
        try {
            if (ATTRIBUTES.containsKey(key)) {
                return Integer.parseInt(ATTRIBUTES.get(key));
            } else {
                return defaultValue;
            }
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
    
    /**
     * Méthode qui supprime toutes les paires
     * clef/valeur dont les clefs ne sont
     * pas contenues dans l'ensemble passé en
     * paramètre
     * 
     * @author Raja Soufi (247680)
     * @author Andrey Batasov (250149)
     * 
     * @param keysToKeep
     *      l'ensemble des clefs dont on veut garder
     *      les paires clef/valeur
     * 
     * @return attributes.build()
     *      l'ensemble filtré des paires clef/valeur
     */
    public Attributes keepOnlyKeys(Set<String> keysToKeep) {
        Builder attributes = new Builder();
        for (Map.Entry<String, String> e: ATTRIBUTES.entrySet()) {
            if (keysToKeep.contains(e.getKey())) {
                attributes.put(e.getKey(), e.getValue());
            }
        }
        return attributes.build();
    }
    
    /**
     * Bâtisseur de la classe Attributes :
     * Permet de construire progressivement l'ensemble
     * des paires clef/valeur
     *
     * @author Raja Soufi (247680)
     * @author Andrey Batasov (250149)
     */
    public static final class Builder {
        private final Map<String, String> ATTRIBUTES;
        
        /**
         * Construit progressivement l'ensemble
         * des paires clef/valeur
         *
         * @author Raja Soufi (247680)
         * @author Andrey Batasov (250149)
         */
        public Builder() {
            ATTRIBUTES = new HashMap<String, String>();
        }
        
        /**
         * Méthode qui ajoute une nouvelle paire clef/valeur
         * 
         * @author Raja Soufi (247680)
         * @author Andrey Batasov (250149)
         * 
         * @param key
         *      la clef
         * @param value
         *      la valeur
         */
        public void put(String key, String value) {
            ATTRIBUTES.put(key, value);
        }
        
        /**
         * Méthode qui construit une version finale et immuable
         * d'un ensemble de paires clef/valeur construit à partir
         * de l'ensemble bâti
         *
         * @author Raja Soufi (247680)
         * @author Andrey Batasov (250149)
         * 
         * @return new Attributes(ATTRIBUTES)
         *      retourne un ensemble construit de paires clef/valeur
         */
        public Attributes build() {
            return new Attributes(ATTRIBUTES);
        }
    }
}
