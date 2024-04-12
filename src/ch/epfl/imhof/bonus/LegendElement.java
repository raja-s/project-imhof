package ch.epfl.imhof.bonus;

/**
 * Interface représentant un élément à légender, qui
 * peut être un style de ligne ou bien une couleur (types
 * qu'on a définis dans ce projet)
 * (cette classe fait partie du code BONUS)
 *
 * @author Raja Soufi (247680)
 * @author Andrey Batasov (250149)
 */
public interface LegendElement {
    
    /**
     * Getter de l'élément à légender (abstrait, redéfini
     * dans les classes qui implémentent l'interface)
     *
     * @author Raja Soufi (247680)
     * @author Andrey Batasov (250149)
     */
    public LegendElement getLegendElement();
    
}
