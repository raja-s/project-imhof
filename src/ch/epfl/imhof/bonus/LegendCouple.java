package ch.epfl.imhof.bonus;

import java.util.Arrays;
import java.util.List;

/**
 * Classe représentant un couple constitué d'un élément à
 * légender et de sa description. De tels couples sont utilisés
 * pour la construction d'une légende
 * (cette classe fait partie du code BONUS)
 *
 * @author Raja Soufi (247680)
 * @author Andrey Batasov (250149)
 */
public class LegendCouple {
    private final List<LegendElement> ELEMENTS;
    private final String MESSAGE;
    
    /**
     * Construit un couple de légende
     *
     * @author Raja Soufi (247680)
     * @author Andrey Batasov (250149)
     * 
     * @param message
     *      La description de l'élément à légender
     * @param elements
     *      La liste des sous-éléments constituant l'élément à
     *      légender
     */
    public LegendCouple(String message, LegendElement... elements) {
        MESSAGE = message;
        ELEMENTS = Arrays.asList(elements);
    }
    
    /**
     * Getter de l'élément du couple, qui est constitué de la
     * liste des sous-éléments
     *
     * @author Raja Soufi (247680)
     * @author Andrey Batasov (250149)
     * 
     * @return ELEMENTS
     *      La liste des sous-éléments
     */
    public List<LegendElement> elements() {
        return ELEMENTS;
    }
    
    /**
     * Getter de la description de l'élément du couple
     *
     * @author Raja Soufi (247680)
     * @author Andrey Batasov (250149)
     * 
     * @return MESSAGE
     *      La description de l'élément
     */
    public String message() {
        return MESSAGE;
    }
}
