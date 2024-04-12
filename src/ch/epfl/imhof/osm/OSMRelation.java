package ch.epfl.imhof.osm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ch.epfl.imhof.Attributes;

/**
 * Classe représenant une relation OpenStreetMap
 * 
 * @author Raja Soufi (247680)
 * @author Andrey Batasov (250149)
 */
public final class OSMRelation extends OSMEntity {
    private final List<Member> MEMBERS;
    
    /**
     * Construit une relation OpenStreetMap avec les
     * paramètres donnés
     * 
     * @author Raja Soufi (247680)
     * @author Andrey Batasov (250149)
     * 
     * @param id
     *      L'identifiant de lentité à construire
     * @param members
     *      La liste des membres de la relation à
     *      construire
     * @param attributes
     *      Les attributs qu'on veut associer à
     *      la relation à construire
     */
    public OSMRelation(long id, List<Member> members, Attributes attributes) {
        super(id, attributes);
        MEMBERS = Collections.unmodifiableList(new ArrayList<Member>(members));
    }
    
    /**
     * Méthode qui retourne une vue non modifiable
     * de la liste des membres de la relation
     * 
     * @author Raja Soufi (247680)
     * @author Andrey Batasov (250149)
     * 
     * @return MEMBERS
     *      La liste des membres
     */
    public List<Member> members() {
        return MEMBERS;
    }
    
    /**
     * Classe imbriquée statiquement qui représente un membre
     * appartenant à la relation
     * 
     * @author Raja Soufi (247680)
     * @author Andrey Batasov (250149)
     */
    public static final class Member {
        private final Type TYPE;
        private final String ROLE;
        private final OSMEntity MEMBER;

        /**
         * Construit un membre avec les paramètres donnés
         * 
         * @author Raja Soufi (247680)
         * @author Andrey Batasov (250149)
         * 
         * @param type
         *      Le type du membre à construire
         * @param role
         *      Le rôle du membre à construire
         * @param member
         *      Le membre (entité OpenStreetMap) du membre à construire
         */
        public Member(Type type, String role, OSMEntity member) {
            TYPE = type;
            ROLE = role;
            MEMBER = member;
        }
        
        /**
         * Méthode qui retourne le type du membre
         * 
         * @author Raja Soufi (247680)
         * @author Andrey Batasov (250149)
         * 
         * @return TYPE
         *      Le type du membre
         */
        public Type type() {
            return TYPE;
        }
        
        /**
         * Méthode qui retourne le role du membre
         * 
         * @author Raja Soufi (247680)
         * @author Andrey Batasov (250149)
         * 
         * @return ROLE
         *      Le role du membre
         */
        public String role() {
            return ROLE;
        }
        
        /**
         * Méthode qui retourne le membre (entité OpenStreetMap)
         * du membre
         * 
         * @author Raja Soufi (247680)
         * @author Andrey Batasov (250149)
         * 
         * @return MEMBER
         *      Le membre (entité OpenStreetMap) du membre
         */
        public OSMEntity member() {
            return MEMBER;
        }
        
        /**
         * Énumération qui énumère les types possibles
         * du membre
         * 
         * @author Raja Soufi (247680)
         * @author Andrey Batasov (250149)
         */
        public enum Type {
            NODE, WAY, RELATION;
        }
    }
    
    /**
     * Bâtisseur de la classe OSMRelation :
     * Permet de construire progressivement une relation
     * OpenStreetMap
     * 
     * @author Raja Soufi (247680)
     * @author Andrey Batasov (250149)
     */
    public static final class Builder extends OSMEntity.Builder {
        private final List<Member> MEMBERS;
        
        /**
         * Construit progressivement une relation
         * OpenStreetMap
         * 
         * @author Raja Soufi (247680)
         * @author Andrey Batasov (250149)
         * 
         * @param id
         *      L'identifiant de la relation à construire
         */
        public Builder(long id) {
            super(id);
            MEMBERS = new ArrayList<Member>();
        }
        
        /**
         * Méthode qui ajoute un nouveau membre à la liste des
         * membres de la relation 
         * 
         * @author Raja Soufi (247680)
         * @author Andrey Batasov (250149)
         * 
         * @param type
         *      Le type du membre à ajouter
         * @param role
         *      Le role du membre à ajouter
         * @param member
         *      Le membre (entité OpenStreetMap) du membre à ajouter
         */
        public void addMember(Member.Type type, String role, OSMEntity member) {
            MEMBERS.add(new Member(type, role, member));
        }
        
        /**
         * Méthode qui construit une version finale et immuable
         * de la relation bâtie, sinon lance l'exception
         * IllegalStateException
         * 
         * @author Raja Soufi (247680)
         * @author Andrey Batasov (250149)
         * 
         * @throws IllegalStateException
         *      Si la relation est incomplète
         * 
         * @return return new OSMRelation(ID, MEMBERS, ATTRIBUTES.build())
         *      La relation construite
         */
        public OSMRelation build() {
            if (isIncomplete()) {
                throw new IllegalStateException("The relation is incomplete!");
            } else {
                return new OSMRelation(ID, MEMBERS, ATTRIBUTES.build());
            }
        }
    }
}
