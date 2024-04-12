package ch.epfl.imhof.painting;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import ch.epfl.imhof.Attributed;

/**
 * Classe représenant un filtre, qui détermine,
 * étant donnée une entité attribuée, si elle doit
 * être gardée ou non
 * 
 * @author Raja Soufi (247680)
 * @author Andrey Batasov (250149)
 */
public final class Filters {
    
    private Filters() {}
    
    /**
     * Méthode statique qui rend un prédicat qui, pour toute valeure
     * attribuée qu'il reçoit, n'est satisfait que si cette dernière
     * possède un attribut portant le nom passé en paramètre
     * 
     * @author Raja Soufi (247680)
     * @author Andrey Batasov (250149)
     * 
     * @param attribute
     *      le nom de l'attribut à tester dans le prédicat
     * 
     * @return attributedEntity -> attributedEntity.hasAttribute(attribute)
     *      le prédicat construit
     */
    public static Predicate<Attributed<?>> tagged(String attribute) {
        return attributedEntity -> attributedEntity.hasAttribute(attribute);
    }
    
    /**
     * Méthode statique qui rend un prédicat qui, pour toute valeure
     * attribuée qu'il reçoit, n'est satisfait que si cette dernière
     * possède un attribut portant le nom passé en paramètre, et si
     * la valeur de cet attribut, s'il existe, est parmi les valeurs
     * passées en paramètre
     * 
     * @author Raja Soufi (247680)
     * @author Andrey Batasov (250149)
     * 
     * @param attribute
     *      le nom de l'attribut à tester dans le prédicat
     * @param values
     *      les valeurs à tester dans le prédicat
     * 
     * @return attributedEntity -> attributedEntity.hasAttribute(attribute)
     *      le prédicat construit
     */
    public static Predicate<Attributed<?>> tagged(String attribute, String... values) {
        List<String> allowedValues = new ArrayList<>();
        for (String value : values) {
            allowedValues.add(value);
        }
        return attributedEntity -> (attributedEntity.hasAttribute(attribute) && (allowedValues.contains(attributedEntity.attributeValue(attribute))));
    }
    
    /**
     * Méthode statique qui rend un prédicat qui, pour toute valeure
     * attribuée qu'il reçoit, n'est satisfait que si cette dernière
     * possède l'entier passé en paramètre comme valeur de l'attribut
     * "layer" (en considérant cette dernière 0 si la valeur de "layer"
     * n'est pas un entier ou si la valeur attribuée ne possède même pas
     * cet attribut)
     * 
     * @author Raja Soufi (247680)
     * @author Andrey Batasov (250149)
     * 
     * @param layerNo
     *      l'entier à tester dans le prédicat (qui correspond au niveau de
     *      la couche dont on veut garder les éléments)
     * 
     * @return attributedEntity -> attributedEntity.hasAttribute(attribute)
     *      le prédicat construit
     */
    public static Predicate<Attributed<?>> onLayer(int layerNo) {
        if ((layerNo < -5) || (layerNo > 5)) {
            throw new IllegalArgumentException("The layer you have entered is out of range!");
        }
        return attributedEntity -> (attributedEntity.attributeValue("layer", 0) == layerNo);
    }
}
