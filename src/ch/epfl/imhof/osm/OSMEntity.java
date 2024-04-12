package ch.epfl.imhof.osm;

import ch.epfl.imhof.Attributes;

/**
 * Classe représenant une entité OpenStreetMap
 * 
 * @author Raja Soufi (247680)
 * @author Andrey Batasov (250149)
 */
public abstract class OSMEntity {
    private final long ID;
    private final Attributes ATTRIBUTES;
    
    /**
     * Construit une entité OpenStreetMap avec les
     * paramètres donnés
     * 
     * @author Raja Soufi (247680)
     * @author Andrey Batasov (250149)
     * 
     * @param id
     *      l'identifiant de lentité à construire
     * @param attributes
     *      les attributs qu'on veut associer à
     *      l'entité à construire
     */
    public OSMEntity(long id, Attributes attributes) {
        ID = id;
        ATTRIBUTES = attributes;
    }
    
    /**
     * Getter de l'identifiant de l'entité en question
     * 
     * @author Raja Soufi (247680)
     * @author Andrey Batasov (250149)
     * 
     * @return ID
     *      l'identifiant de l'entité
     */
    public long id() {
        return ID;
    }
    
    /**
     * Getter des attributs de l'entité en question
     * 
     * @author Raja Soufi (247680)
     * @author Andrey Batasov (250149)
     * 
     * @return ATTRIBUTES
     *      les attributs de l'entité
     */
    public Attributes attributes() {
        return ATTRIBUTES;
    }
    
    /**
     * Méthode qui teste si l'entité en question
     * possède un attribut avec la clef passée en
     * paramètre
     * 
     * @author Raja Soufi (247680)
     * @author Andrey Batasov (250149)
     * 
     * @param key
     *      la clef avec laquelle on veut faire le test
     * 
     * @return ATTRIBUTES.contains(key)
     *      le résultat du test
     */
    public boolean hasAttribute(String key) {
        return ATTRIBUTES.contains(key);
    }
    
    /**
     * Méthode qui retourne la valeur associée à la
     * clef donné ou null si celle-ci n'existe pas
     * 
     * @author Raja Soufi (247680)
     * @author Andrey Batasov (250149)
     * 
     * @param key
     *      la clef dont on veut savoir la valeur
     * 
     * @return ATTRIBUTES.get(key)
     *      la valeur souhaitée
     */
    public String attributeValue(String key) {
        return ATTRIBUTES.get(key);
    }
    
    /**
     * Bâtisseur de la classe OSMEntity :
     * Permet de construire progressivement une entité
     * OpenStreetMap
     *
     * @author Raja Soufi (247680)
     * @author Andrey Batasov (250149)
     */
    public static abstract class Builder {
        protected final long ID;
        protected final Attributes.Builder ATTRIBUTES;
        private boolean isIncomplete = false;
        
        /**
         * Construit progressivement une entité
         * OpenStreetMap
         * 
         * @author Raja Soufi (247680)
         * @author Andrey Batasov (250149)
         * 
         * @param id
         *      l'identifiant de l'entité à construire
         */
        public Builder(long id) {
            ID = id;
            ATTRIBUTES = new Attributes.Builder();
        }
        
        /**
         * Méthode qui ajoute une nouvelle paire clef/valeur à
         * l'ensemble d'attributs de l'entité
         * 
         * @author Raja Soufi (247680)
         * @author Andrey Batasov (250149)
         * 
         * @param key
         *      la clef de la paire à ajouter
         * @param value
         *      la valeur de la paire à ajouter
         */
        public void setAttribute(String key, String value) {
            ATTRIBUTES.put(key, value);
        }
        
        /**
         * Méthode qui précise que l'entité est incomplète
         * en associant la valeur booléenne true au champ
         * isIncomplete
         *
         * @author Raja Soufi (247680)
         * @author Andrey Batasov (250149)
         */
        public void setIncomplete() {
            isIncomplete = true;
        }
        
        /**
         * Méthode qui teste si l'entité est incomplète
         * 
         * @author Raja Soufi (247680)
         * @author Andrey Batasov (250149)
         * 
         * @return isIncomplete
         *      le résultat du test
         */
        public boolean isIncomplete() {
            return isIncomplete;
        }
    }
}
